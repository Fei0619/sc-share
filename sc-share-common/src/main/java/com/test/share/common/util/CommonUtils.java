package com.test.share.common.util;

/**
 * @author 费世程
 * @date 2021/1/28 9:54
 */
public class CommonUtils {

  public static void require(Boolean value, String message) {
    if (!value) {
      throw new IllegalArgumentException(message);
    }
  }

}
