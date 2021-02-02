package com.test.share.common.retry.stop;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * @author 费世程
 * @date 2021/1/28 16:11
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class StopAfterAttemptPolicy extends StopPolicy {

  @Nonnull
  private Integer maxAttempt;

  private Integer currentAttemptTime = 0;

  @Override
  public Boolean stopRetry() {
    currentAttemptTime++;
    return currentAttemptTime > maxAttempt;
  }
}
