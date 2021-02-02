package com.test.share.common.retry

import com.test.share.common.retry.stop.StopPolicy
import com.test.share.common.retry.wait.WaitPolicy
import java.util.concurrent.Callable
import java.util.function.BiConsumer

/**
 * @author 费世程
 * @date 2021/2/2 15:43
 */
abstract class AbstractRetryer<R, FR> : Retryer<R, FR> {

  companion object {
    private val defaultConditions = listOf(Throwable::class.java)
    private const val defaultAttempt = 3
  }

  // 停止策略
  var stopPolicy: StopPolicy = StopPolicy.stopAfterAttempt(defaultAttempt)
  // 等待策略
  var waitPolicy: WaitPolicy = WaitPolicy.noWait()
  // 重试条件
  var conditions: List<Class<out Throwable>> = defaultConditions
  // 失败回调
  var failureCallback: BiConsumer<Int, Throwable>? = null
  // 最终失败回调
  var fallback: Callable<R>? = null
  // 任务
  internal var task: Callable<R>? = null

  override fun stopPolicy(stopPolicy: StopPolicy): Retryer<R, FR> {
    this.stopPolicy = stopPolicy
    return this
  }

  override fun waitPolicy(waitPolicy: WaitPolicy): Retryer<R, FR> {
    this.waitPolicy = waitPolicy
    return this
  }

  override fun conditions(conditions: List<Class<out Throwable>>): Retryer<R, FR> {
    this.conditions = conditions
    return this
  }

  override fun failureCallback(failureCallback: BiConsumer<Int, Throwable>): Retryer<R, FR> {
    this.failureCallback = failureCallback
    return this
  }

  override fun failureCallback(failureCallback: (Int, Throwable) -> Unit): Retryer<R, FR> {
    this.failureCallback = BiConsumer(failureCallback)
    return this
  }

  override fun fallback(fallback: Callable<R>): Retryer<R, FR> {
    this.fallback = fallback
    return this
  }

  override fun fallback(fallback: () -> R): Retryer<R, FR> {
    this.fallback = Callable(fallback)
    return this
  }

  override fun task(task: Callable<R>): Retryer<R, FR> {
    this.task = task
    return this
  }

  override fun task(task: () -> R): Retryer<R, FR> {
    this.task = Callable(task)
    return this
  }

  override fun execute(task: Callable<R>): FR {
    this.task = task
    return execute()
  }

  override fun execute(task: () -> R): FR {
    this.task = Callable(task)
    return execute()
  }

}
