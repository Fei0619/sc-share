package com.test.share.common.http;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 费世程
 * @date 2021/2/4 10:36
 */
public class WebClients {

  private static final ConcurrentHashMap<String, HttpClient> httpClients = new ConcurrentHashMap<>();

  /**
   * 创建一个【WebClient.Builder】
   *
   * @param connectTimeOut 建立连接的超时时间
   * @param writeTimeOut   写入超时时间
   * @param readTimeOut    读取超时时间
   * @return WebClient.Builder
   */
  public static WebClient.Builder createWebClientBuilder(Integer connectTimeOut, Long writeTimeOut, Long readTimeOut) {
    connectTimeOut = connectTimeOut == null ? 200 : connectTimeOut;
    writeTimeOut = writeTimeOut == null ? 400 : writeTimeOut;
    readTimeOut = readTimeOut == null ? 400 : readTimeOut;
    HttpClient httpClient = getHttpClient(connectTimeOut, writeTimeOut, readTimeOut);
    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
  }

  /**
   * 创建一个【WebClient】
   *
   * @param baseUrl        baseUrl
   * @param connectTimeOut 建立连接的超时时间
   * @param writeTimeOut   写入超时时间
   * @param readTimeOut    读取超时时间
   * @return WebClient
   */
  public static WebClient createWebClient(String baseUrl,
                                          Integer connectTimeOut, Long writeTimeOut, Long readTimeOut) {
    WebClient.Builder builder = createWebClientBuilder(connectTimeOut, writeTimeOut, readTimeOut);
    if (StringUtils.isNotBlank(baseUrl)) {
      builder.baseUrl(baseUrl);
    }
    return builder.build();
  }

  /**
   * 创建一个【WebClient】
   *
   * @param connectTimeOut 建立连接的超时时间
   * @param writeTimeOut   写入超时时间
   * @param readTimeOut    读取超时时间
   * @return WebClient
   */
  public static WebClient createWebClient(Integer connectTimeOut, Long writeTimeOut, Long readTimeOut) {
    return createWebClient("", connectTimeOut, writeTimeOut, readTimeOut);
  }

  private static HttpClient getHttpClient(Integer connectTimeOut, Long writeTimeOut, Long readTimeOut) {
    String key = connectTimeOut + ":" + writeTimeOut + ":" + readTimeOut;
    return httpClients.computeIfAbsent(key, k -> createHttpClient(connectTimeOut, writeTimeOut, readTimeOut));
  }

  private static HttpClient createHttpClient(Integer connectTimeOut, Long writeTimeOut, Long readTimeOut) {
    return HttpClient.create().tcpConfiguration(client ->
        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .doOnConnected(conn -> {
              conn.addHandler(new WriteTimeoutHandler(writeTimeOut, TimeUnit.MILLISECONDS));
              conn.addHandler(new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS));
            }));
  }

}
