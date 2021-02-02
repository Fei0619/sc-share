package com.test.share.common.retry.wait;

import java.time.Duration;

/**
 * 等待策略
 *
 * @author 费世程
 * @date 2021/1/28 9:27
 */
@SuppressWarnings("unused")
public abstract class WaitPolicy {

  /**
   * 指数等待时长策略
   *
   * @param initial     初始等待时间
   * @param exponential 增长指数
   * @return 等待策略
   */
  public static WaitPolicy exponentialWait(Duration initial, Double exponential) {
    return new ExponentialWaitPolicy(initial.toMillis(), exponential);
  }

  /**
   * 固定时长等待策略
   *
   * @param initial 时间间隔
   * @return 等待策略
   */
  public static WaitPolicy fixedWait(Duration initial) {
    return new FixedWaitPolicy(initial.toMillis());
  }

  /**
   * 递增等待时长策略
   *
   * @param initial 初始等待时间
   * @param step    步长
   * @return 等待策略
   */
  public static WaitPolicy incrementWait(Duration initial, Duration step) {
    return new IncrementWaitPolicy(initial.toMillis(), step.toMillis());
  }

  /**
   * 不等待直接重试
   *
   * @return 等待策略
   */
  public static WaitPolicy noWait() {
    return new NoWaitPolicy();
  }

  /**
   * 随机时长等待策略
   *
   * @param min 最小等待间隔
   * @param max 最大等待间隔
   * @return 等待策略
   */
  public static WaitPolicy randomWait(Duration min, Duration max) {
    return new RandomWaitPolicy(min.toMillis(), max.toMillis());
  }


  /**
   * 重试时间间隔（单位：毫秒）
   *
   * @return Long
   */
  public abstract Long delayMillis();

}
