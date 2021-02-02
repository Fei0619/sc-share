package com.test.share.common.retry.stop;

/**
 * 不停止重试
 *
 * @author 费世程
 * @date 2021/1/28 16:09
 */
public class NeverStopPolicy extends StopPolicy {

  @Override
  public Boolean stopRetry() {
    return false;
  }

}
