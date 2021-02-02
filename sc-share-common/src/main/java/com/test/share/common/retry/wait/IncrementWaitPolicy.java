package com.test.share.common.retry.wait;

import com.test.share.common.util.CommonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * 递增等待时长策略
 *
 * @author 费世程
 * @date 2021/1/28 10:33
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class IncrementWaitPolicy extends WaitPolicy {

  @Nonnull
  private Long initial;
  @Nonnull
  private Long step;

  private Long currentDelay = -1L;

  @Override
  public Long delayMillis() {
    CommonUtils.require(initial >= 0, "初始等待时长不能小于0ms!");
    CommonUtils.require(step >= 0, "等待步长不能小于0");
    if (currentDelay == -1L) {
      currentDelay = initial;
    }
    currentDelay += step;
    return currentDelay;
  }
}
