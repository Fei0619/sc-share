package extension

import java.lang.IndexOutOfBoundsException

/**
 * @author 费世程
 * @date 2020/11/30 13:46
 */

/**
 * 根据下标获取集合中的某个元素
 * @param index 下标
 * @return T
 * @author 费世程
 * @date 2020/11/30 13:56
 */
fun <T> Collection<T>.get(index: Int): T {
  if (index + 1 > this.size) {
    throw IndexOutOfBoundsException()
  }
  val iterator = this.iterator()
  var i = 0
  var res: T? = null
  while (i <= index && iterator.hasNext()) {
    val next = iterator.next()
    if (i == index) {
      res = next
    }
    i++
  }
  return res!!
}
