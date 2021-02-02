package com.test.share.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.*;

/**
 * @author 费世程
 * @date 2021/1/15 17:11
 */
@SuppressWarnings({"unused"})
public class ThreadPools {

  /**
   * 服务器核心数
   */
  private static final Integer availableProcessorsCount = Runtime.getRuntime().availableProcessors();
  /**
   * 核心线程数
   */
  private static Integer commonPoolCoreSize = 0;
  /**
   * 最大线程数=服务器核心数*16
   */
  private static Integer commonPoolMaxSize = availableProcessorsCount << 4;
  /**
   * 线程名前缀
   */
  private static final String commonPoolNamePrefix = "common-pool-";
  /**
   * 通用线程池
   * 默认核心线程数：0
   * 默认最大线程数：cpu数量*16
   * 如果线程池满了，则任务将在调用者线程中执行
   */
  public static final ThreadPoolExecutor commonPool;
  /**
   * 以线程方式执行任务
   */
  public static final Scheduler commonSchedule;

  private static final Logger log = LoggerFactory.getLogger(ThreadPools.class);


  static {
    String commonPoolCoreSizeStr = System.getProperty("commonPoolCoreSize");
    String commonPoolMaxSizeStr = System.getProperty("commonPoolMaxSize");
    Integer commonPoolCoreSizeInt;
    Integer commonPoolMaxSizeInt;
    try {
      commonPoolCoreSizeInt = Integer.parseInt(commonPoolCoreSizeStr);
    } catch (Exception e) {
      commonPoolCoreSizeInt = commonPoolCoreSize;
      log.debug("自定义commonPoolCoreSize格式不合法 -> {}", commonPoolCoreSizeStr);
    }
    try {
      commonPoolMaxSizeInt = Integer.parseInt(commonPoolMaxSizeStr);
    } catch (Exception e) {
      commonPoolMaxSizeInt = commonPoolMaxSize;
      log.debug("自定义commonPoolCoreSize格式不合法 -> {}", commonPoolMaxSizeStr);
    }
    commonPoolCoreSize = commonPoolCoreSizeInt;
    commonPoolMaxSize = commonPoolMaxSizeInt;

    commonPool = new ThreadPoolExecutor(
        commonPoolCoreSize,
        commonPoolMaxSize,
        60L,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        new ThreadFactoryBuilder().setNameFormat(commonPoolNamePrefix + "%d").build(),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );
    commonSchedule = Schedulers.fromExecutor(commonPool);

    Runtime.getRuntime().addShutdownHook(new Thread(commonPool::shutdown));
  }

}
