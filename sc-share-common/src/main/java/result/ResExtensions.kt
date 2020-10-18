package result

/**
 * @author 费世程
 * @date 2020/10/13 9:45
 */

/**
 * 将对象[T]包装为[Res],返回[Res.data(T)]
 */
fun <T> T.toDataResult(): Res<T> {
  return Res.data(this)
}
