package com.test.share.common.retry.stop;

import java.time.Duration;

/**
 * 停止策略
 *
 * @author 费世程
 * @date 2021/1/28 9:32
 */
@SuppressWarnings("unused")
public abstract class StopPolicy {

  /**
   * 永远不停止
   */
  public static StopPolicy neverStop() {
    return new NeverStopPolicy();
  }

  /**
   * 达到最大重试次数后停止重试
   */
  public static StopPolicy stopAfterAttempt(Integer maxAttempt) {
    return new StopAfterAttemptPolicy(maxAttempt);
  }

  /**
   * 达到最大可执行时间后停止重试
   */
  public static StopPolicy stopAfterDelay(Duration maxAttempt) {
    return new StopAfterDelayPolicy(maxAttempt);
  }

  /**
   * 是否停止重试
   *
   * @return Boolean
   */
  public abstract Boolean stopRetry();

}
