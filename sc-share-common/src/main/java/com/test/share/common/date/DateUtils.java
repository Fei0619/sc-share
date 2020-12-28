package com.test.share.common.date;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 费世程
 * @date 2020/12/22 14:31
 */
public class DateUtils {

  //Java7中的SimpleDateFormat不是线程安全的，可以用ThreadLocal来解决这个问题
  private static final Map<String, ThreadLocal<SimpleDateFormat>> SDF_MAP = new HashMap<>();

  /**
   * 根据模式串获取一个SimpleDateFormat实例
   *
   * @param pattern 模式串
   * @return SimpleDateFormat实例
   */
  public static SimpleDateFormat getSimpleDateFormat(String pattern) {
    ThreadLocal<SimpleDateFormat> threadLocal = SDF_MAP.get(pattern);
    if (threadLocal == null) {
      synchronized (pattern) {
        threadLocal = SDF_MAP.get(pattern);
        if (threadLocal == null) {
          threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
          SDF_MAP.put(pattern, threadLocal);
        }
      }
    }
    return threadLocal.get();
  }


}
