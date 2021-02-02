package com.test.share.common.retry

import com.test.share.common.intf.Destroyable
import com.test.share.common.intf.Initable
import org.slf4j.LoggerFactory
import java.lang.UnsupportedOperationException
import java.util.concurrent.*

/**
 * @author 费世程
 * @date 2021/2/1 14:18
 */
class AsyncRetryer<R> internal constructor(private var executor: ExecutorService?)
  : AbstractRetryer<R, Future<R?>>(), RunnableFuture<R?>, Delayed {

  init {
    AsyncRetryDelayer.init()
    Runtime.getRuntime().addShutdownHook(Thread { AsyncRetryDelayer.destroy() })
  }

  private val log = LoggerFactory.getLogger(AsyncRetryer::class.java)
  private var attempt = 0
  private var execute = 0
  private val countDownLatch = CountDownLatch(1)
  private var nextExecuteTimestamp = -1L

  private var exception: Throwable? = null
  private var result: R? = null

  override fun execute(): Future<R?> {
    require(task != null) { "执行的任务不能为空！" }
    require(execute++ == 0) { "任务不能重复执行！" }
    doExecute()
    return this
  }

  fun doExecute() {
    return executor!!.execute(this)
  }

  override fun isDone(): Boolean {
    return countDownLatch.count == 0L
  }

  override fun get(): R? {
    countDownLatch.await()
    if (exception != null) {
      throw exception!!
    } else {
      return result
    }
  }

  override fun get(timeout: Long, unit: TimeUnit): R? {
    countDownLatch.await(timeout, unit)
    if (exception != null) {
      throw exception!!
    } else {
      return result
    }
  }

  override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
    throw UnsupportedOperationException("不支持此操作!")
  }

  override fun isCancelled(): Boolean {
    return false
  }

  override fun run() {
    try {
      result = task!!.call()
      // 成功执行
      countDownLatch.countDown()
      executor = null
    } catch (e: Throwable) {
      var flag = false
      for (condition in conditions) {
        if (condition.isAssignableFrom(e::class.java)) {
          flag = true
          break
        }
      }
      if (flag) {
        // 满足重试条件
        failureCallback?.accept(attempt, e)
        if (stopPolicy.stopRetry()) {
          // 已达重试上限
          log.info("已达重试上限，放弃重试，重试次数 -> {}", attempt)
          if (fallback != null) {
            result = fallback!!.call()
          } else {
            exception = e
          }
          countDownLatch.countDown()
          executor = null
          return
        } else {
          attempt++
          // 需继续重试
          val delayMillis = waitPolicy.delayMillis()
          if (delayMillis > 0) {
            // 延迟重试
            log.info("即将在 {}ms 后进行第 {} 次重试...", delayMillis, attempt)
            nextExecuteTimestamp = System.currentTimeMillis() + delayMillis
            AsyncRetryDelayer.delayQueue.offer(this)
            return
          } else {
            log.info("即将进行第 {} 次重试...", attempt)
            doExecute()
            return
          }
        }
      } else {
        // 不满足重试条件
        exception = e
        countDownLatch.countDown()
        executor = null
        throw e
      }
    }
  }

  override fun compareTo(other: Delayed): Int {
    return (this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS)).toInt()
  }

  override fun getDelay(unit: TimeUnit): Long {
    if (nextExecuteTimestamp < 0) {
      throw RuntimeException("nextExecuteTimestamp < 0")
    }
    return unit.convert(nextExecuteTimestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
  }

}

internal object AsyncRetryDelayer : Initable, Destroyable {

  internal val delayQueue = DelayQueue<AsyncRetryer<*>>()
  @Volatile
  private var start = false
  private var consumptionThread: Thread? = null


  override fun init() {
    start = true
    consumptionThread = Thread {
      while (start) {
        delayQueue.poll(5, TimeUnit.SECONDS)?.doExecute()
      }
    }
    consumptionThread!!.start()
  }

  override fun destroy() {
    start = false
  }
}
