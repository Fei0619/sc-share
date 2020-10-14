package result;

/**
 * @author 费世程
 * @date 2020/10/13 8:39
 */
public interface ResMsg {

  /**
   * 响应码
   */
  int code();

  /**
   * 响应描述
   */
  String message();

}
