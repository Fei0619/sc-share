package com.test.share.common.retry

import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2021/2/1 14:18
 */
class BlockRetryer<R> : AbstractRetryer<R, Optional<R>>() {

  private val log = LoggerFactory.getLogger(BlockRetryer::class.java)

  /**
   * 当前重试次数
   */
  private var attempt = 0
  private var execute = 0

  override fun execute(): Optional<R> {
    require(task != null) { "任务不能为空！" }
    require(execute++ == 0) { "任务不能重复执行！" }
    return doExecute()
  }

  private fun doExecute(): Optional<R> {
    try {
      return Optional.ofNullable(task!!.call())
    } catch (exception: Exception) {
      var flag = false
      for (con in conditions) {
        if (con.isAssignableFrom(exception::class.java)) {
          flag = true
          break
        }
      }
      if (flag) {
        failureCallback?.accept(attempt, exception)
        // 异常在重试条件内，需要重试
        if (stopPolicy.stopRetry()) {
          // 已达重试上限
          log.info("已达重试上限，停止重试，重试次数 -> {}", attempt)
          if (fallback != null) {
            return Optional.ofNullable(fallback!!.call())
          }
          throw exception
        } else {
          // 继续重试
          attempt++
          val delayMillis = waitPolicy.delayMillis()
          if (delayMillis > 0) {
            log.info("即将在 {}ms 后进行第 {} 次重试...", delayMillis, attempt)
            TimeUnit.MILLISECONDS.sleep(delayMillis)
          } else {
            log.info("即将进行第 {} 次尝试...", attempt)
          }
          return doExecute()
        }
      } else {
        //异常不在重试条件内，不需要重试
        throw exception
      }
    }
  }

}
