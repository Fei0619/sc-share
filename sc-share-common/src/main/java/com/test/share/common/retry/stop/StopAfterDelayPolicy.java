package com.test.share.common.retry.stop;

import com.test.share.common.util.CommonUtils;

import java.time.Duration;
import java.util.Optional;

/**
 * 最长允许执行时间
 *
 * @author 费世程
 * @date 2021/1/28 16:14
 */
@SuppressWarnings("unused")
public class StopAfterDelayPolicy extends StopPolicy {

  private Long stopTime;

  public StopAfterDelayPolicy(Duration maxDelay) {
    CommonUtils.require(maxDelay != null, "最长允许执行时间不能为空！");
    assert maxDelay != null;
    long maxDelayMillis = maxDelay.toMillis();
    CommonUtils.require(maxDelayMillis > 0, "最长允许执行时间至少为1ms");
    stopTime = System.currentTimeMillis() + maxDelay.toMillis();
  }

  @Override
  public Boolean stopRetry() {
    return System.currentTimeMillis() >= stopTime;
  }
}
