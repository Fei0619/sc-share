package com.test.share.common.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType

/**
 * @author 费世程
 * @date 2020/10/14 8:45
 */
@JvmOverloads
fun <T> T.toJsonString(ignoreNull: Boolean = true, pretty: Boolean = true): String =
    JsonUtils.toJsonString(this, ignoreNull, pretty)

fun <T> String.parseJson(clazz: Class<T>): T = JsonUtils.parseJson(this, clazz)

fun <T> String.parseJson(javaType: JavaType): T = JsonUtils.parseJson(this, javaType)

fun <T> String.parseJson(type: TypeReference<T>): T = JsonUtils.parseJson(this, type)

fun <T> String.parseJson(parametrized: Class<out Any>, parameterClass: Class<out Any>): T {
  return JsonUtils.parseJson(this, parametrized, parameterClass)
}

fun <T> String.parseJson(parametrized: Class<out Any>,
                         parameterClass_1: Class<out Any>, parameterClass_2: Class<out Any>): T {
  return JsonUtils.parseJson(this, parametrized, parameterClass_1, parameterClass_2)
}

fun <E> String.parseJsonToList(clazz: Class<E>): List<E> {
  return JsonUtils.parseArray(this, clazz)
}

fun <E> String.parseJsonToSet(clazz: Class<E>): Set<E> {
  return JsonUtils.parseSet(this, clazz)
}

fun <K, V> String.parseJsonMap(keyClass: Class<K>,
                               valueClass: Class<V>,
                               mapClass: Class<out Map<out Any, Any>>? = HashMap::class.java): Map<K, V> {
  return JsonUtils.parseMap(this, keyClass, valueClass, mapClass)
}
