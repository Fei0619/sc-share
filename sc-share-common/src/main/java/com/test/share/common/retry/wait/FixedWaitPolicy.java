package com.test.share.common.retry.wait;

import com.test.share.common.util.CommonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * 固定时长等待策略
 *
 * @author 费世程
 * @date 2021/1/28 10:29
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class FixedWaitPolicy extends WaitPolicy {

  @Nonnull
  private Long interval;

  @Override
  public Long delayMillis() {
    CommonUtils.require(interval >= 0L, "重试等待时长不能小于0ms");
    return interval;
  }
}
