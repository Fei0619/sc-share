package com.test.share.common.retry

import com.test.share.common.retry.Retryer.Companion.reactiveRetryer
import com.test.share.common.retry.stop.StopPolicy
import com.test.share.common.retry.wait.WaitPolicy
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2021/2/2 15:30
 */
fun main() {
  val num = 0

  Retryer
//      .blockRetryer<Int>()
      .reactiveRetryer<String>()
//      .asyncRetryer<Int>(Executors.newCachedThreadPool())
      .waitPolicy(WaitPolicy.incrementWait(Duration.ofSeconds(2), Duration.ofSeconds(1)))
      .stopPolicy(StopPolicy.stopAfterAttempt(3))
      .conditions(listOf<Class<out Throwable>>(Exception::class.java))
      .failureCallback { i, t -> System.err.println("失败回调日志：$i -> ${t.message}") }
      .execute {
        System.err.println(1 / num)
        Mono.just("")
      }

}
