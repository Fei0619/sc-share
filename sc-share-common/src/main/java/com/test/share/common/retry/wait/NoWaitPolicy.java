package com.test.share.common.retry.wait;

/**
 * @author 费世程
 * @date 2021/1/28 10:37
 */
public class NoWaitPolicy extends WaitPolicy {
  @Override
  public Long delayMillis() {
    return 0L;
  }
}
