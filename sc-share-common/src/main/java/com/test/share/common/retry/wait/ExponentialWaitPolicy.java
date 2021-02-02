package com.test.share.common.retry.wait;

import com.test.share.common.util.CommonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;


/**
 * 指数等待时长策略
 *
 * @author 费世程
 * @date 2021/1/28 9:38
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@SuppressWarnings("unused")
public class ExponentialWaitPolicy extends WaitPolicy {

  @Nonnull
  private Long initial;
  @Nonnull
  private Double exponential;

  private Long currentDelay = -1L;

  @Override
  public Long delayMillis() {
    CommonUtils.require(initial >= 0, "初始等待时间间隔不能为空!");
    CommonUtils.require(exponential > 0, "递增指数要大于0！");
    if (currentDelay == -1L) {
      currentDelay = initial;
    } else {
      currentDelay = new Double(currentDelay * exponential).longValue();
    }
    return currentDelay;
  }

}
