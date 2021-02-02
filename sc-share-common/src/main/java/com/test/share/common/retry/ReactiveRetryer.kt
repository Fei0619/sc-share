package com.test.share.common.retry

import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2021/2/1 14:18
 */
class ReactiveRetryer<R> : AbstractRetryer<Mono<R>, Mono<R>>() {

  private val log = LoggerFactory.getLogger(ReactiveRetryer::class.java)
  private var attempt = 0
  private var execute = 0

  override fun execute(): Mono<R> {
    require(task != null) { "执行的任务不能为空！" }
    require(execute++ == 0) { "任务不能重复执行！" }
    return doExecute()
  }

  private fun doExecute(): Mono<R> {
    return task!!.call()
        .onErrorResume { throwable ->
          var flag = false
          for (con in conditions) {
            if (con.isAssignableFrom(throwable::class.java)) {
              flag = true
              break
            }
          }
          if (flag) {
            // 在重试条件内，需要重试
            attempt++
            failureCallback?.accept(attempt, throwable)
            if (stopPolicy.stopRetry()) {
              // 达到最大重试上限
              log.info("一达到最大重试上限，放弃重试，重试次数 -> {}", attempt)
              if (fallback != null) {
                fallback!!.call()
              } else {
                Mono.error(throwable)
              }
            } else {
              // 继续重试
              val delayMillis = waitPolicy.delayMillis()
              if (delayMillis > 0) {
                log.info("即将在 {}ms 后进行第 {} 次重试...", delayMillis, attempt)
                Mono.delay(Duration.ofMillis(delayMillis)).flatMap { doExecute() }
              } else {
                log.info("即将进行第 {} 次重试...", attempt)
                doExecute()
              }
            }
          } else {
            // 不在重试条件内，无需重试
            Mono.error(throwable)
          }
        }
  }

}
