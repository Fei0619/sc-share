package com.test.share.common.retry.wait;

import com.test.share.common.util.CommonUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机等待时长策略
 *
 * @author 费世程
 * @date 2021/1/28 10:37
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class RandomWaitPolicy extends WaitPolicy {

  /**
   * 最小时间间隔
   */
  @Nonnull
  private Long min;
  /**
   * 最大时间间隔，[min,max)
   */
  @Nonnull
  private Long max;


  @Override
  public Long delayMillis() {
    CommonUtils.require(min >= 0, "随机等待时长最小时间间隔不合法！");
    CommonUtils.require(max >= min, "随机等待时长最大时间间隔不合法！");
    return ThreadLocalRandom.current().nextLong(min, max);
  }
}
