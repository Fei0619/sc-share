package tree

import extension.get
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author 费世程
 * @date 2020/11/30 11:07
 */
object TreeUtils {

  private val getChildMethodMap = ConcurrentHashMap<Class<*>, Method>()
  private val getParentMethodMap = ConcurrentHashMap<Class<*>, Method>()
  private val getChildListMethodMap = ConcurrentHashMap<Class<*>, Method>()
  private val setChildListMethodMap = ConcurrentHashMap<Class<*>, Method>()

  fun <T> toTreeArray(sourceList: Collection<T>): List<T> {
    if (sourceList.size < 2) {
      return sourceList.toList()
    }
    val clazz = (sourceList.get(0) as Any)::class.java
    if (getChildMethodMap[clazz] == null) {
      synchronized(clazz) {
        if (getChildMethodMap[clazz] == null) {
          init(clazz)
        }
      }
    }
    val getChildMethod = getChildMethodMap[clazz]
    val getParentMethod = getParentMethodMap[clazz]
    val getChildListMethod = getChildListMethodMap[clazz]
    val setChildListMethod = setChildListMethodMap[clazz]

    val res = ArrayList<T>()
    val sourceMap = HashMap<Any, T>()
    sourceList.forEach { item ->
      //节点的childList若为空，初始化
      val childList = getChildListMethod!!.invoke(item)
      if (childList == null) {
        setChildListMethod!!.invoke(item, ArrayList<T>())
      }
      sourceMap[getChildMethod!!.invoke(item)] = item
    }
    for (item in sourceList) {
      val parentNodeId = getParentMethod!!.invoke(item)
      val parentNode = sourceMap[parentNodeId]
      if (parentNode == null) {
        res.add(item)
      } else {
        val childList = getChildListMethod!!.invoke(parentNode)!!
        @Suppress("unchecked_cast")
        val list = childList as MutableList<T>
        list.add(item)
      }
    }
    return res
  }

  //--------------------------------------------- 私有方法 ---------------------------------------------//

  private fun init(clazz: Class<*>) {
    val annotation = clazz.getAnnotation(TreeTag::class.java)
    getChildMethodMap[clazz] = clazz.getMethod("get${getHumpName(annotation.nodeParam)}")
    getParentMethodMap[clazz] = clazz.getMethod("get${getHumpName(annotation.parentNodeParam)}")
    getChildListMethodMap[clazz] = clazz.getMethod("get${getHumpName(annotation.childListParam)}")
    setChildListMethodMap[clazz] = clazz.getMethod("set${getHumpName(annotation.childListParam)}", List::class.java)
  }

  private fun getHumpName(source: String): String {
    return source[0].toUpperCase() + (if (source.length < 2) "" else source.substring(1))
  }

}

fun <T> Collection<T>.toTreeArray() = TreeUtils.toTreeArray(this)
