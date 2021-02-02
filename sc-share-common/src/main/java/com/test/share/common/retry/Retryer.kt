package com.test.share.common.retry

import com.test.share.common.retry.stop.StopPolicy
import com.test.share.common.retry.wait.WaitPolicy
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.function.BiConsumer
import java.util.function.Supplier

/**
 * @author 费世程
 * @date 2021/2/2 15:35
 */
@Suppress("unused")
interface Retryer<R, FR> {

  companion object {
    /**
     * 异步重试器
     */
    fun <R> asyncRetryer(executor: ExecutorService): Retryer<R, Future<R?>> {
      return AsyncRetryer(executor)
    }

    /**
     * 反应式重试器
     */
    fun <R> reactiveRetryer(): Retryer<Mono<R>, Mono<R>> {
      return ReactiveRetryer()
    }

    /**
     * 阻塞重试器
     */
    fun <R> blockRetryer(): Retryer<R, Optional<R>> {
      return BlockRetryer()
    }
  }

  /**
   * 设置终止策略
   */
  fun stopPolicy(stopPolicy: StopPolicy): Retryer<R, FR>

  /**
   * 设置等待策略
   */
  fun waitPolicy(waitPolicy: WaitPolicy): Retryer<R, FR>

  /**
   * 设置重试条件
   */
  fun conditions(conditions: List<Class<out Throwable>>): Retryer<R, FR>

  /**
   * 每一次执行失败的回调
   */
  fun failureCallback(failureCallback: BiConsumer<Int, Throwable>): Retryer<R, FR>

  fun failureCallback(failureCallback: (Int, Throwable) -> Unit): Retryer<R, FR>

  /**
   * 最终失败后执行的回调
   */
  fun fallback(fallback: Callable<R>): Retryer<R, FR>

  fun fallback(fallback: () -> R): Retryer<R, FR>

  /**
   * 需要执行的任务
   */
  fun task(task: Callable<R>): Retryer<R, FR>

  fun task(task: () -> R): Retryer<R, FR>

  /**
   * 设置任务并执行
   */
  fun execute(task: Callable<R>): FR

  fun execute(task: () -> R): FR

  /**
   * 执行任务
   */
  fun execute(): FR
}
