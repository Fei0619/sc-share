package json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import date.DatePattern
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * JSON工具类
 * @author 费世程
 * @date 2020/10/13 10:02
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object JsonUtils {

  val javaTimeModule: SimpleModule = JavaTimeModule()
      .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd_HH_mm_ss)))
      .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd_HH_mm_ss)))
      .addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd)))
      .addDeserializer(LocalDate::class.java, LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd)))
      .addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.HH_mm_ss)))
      .addDeserializer(LocalTime::class.java, LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.HH_mm_ss)))

  private val mapper = ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
      .registerModule(javaTimeModule)
      .findAndRegisterModules()

  private val ignoreNullMapper = ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .registerModule(javaTimeModule)
      .findAndRegisterModules()

  /**
   * 将对象转化为json字符串
   */
  @JvmOverloads
  fun <T> toJsonString(t: T, ignoreNull: Boolean = true, pretty: Boolean = true): String {
    val writer = if (ignoreNull) ignoreNullMapper else mapper
    return if (pretty) {
      writer.writerWithDefaultPrettyPrinter().writeValueAsString(t)
    } else {
      writer.writeValueAsString(t)
    }
  }

  /**
   * 将json字符串转化为Object
   */
  fun <T> parseJson(jsonString: String, clazz: Class<T>): T = ignoreNullMapper.readValue(jsonString, clazz)

  fun <T> parseJson(jsonString: String, javaType: JavaType): T = ignoreNullMapper.readValue(jsonString, javaType)

  fun <T> parseJson(jsonString: String, type: TypeReference<T>): T = ignoreNullMapper.readValue(jsonString, type)

  /**
   * val result:Res<User>=JsonUtils.parseJson(jsonString,Result::class.java,User::class.java)
   */
  fun <T> parseJson(jsonString: String, parametrized: Class<out Any>, parameterClass: Class<out Any>): T {
    val javaType = ignoreNullMapper.typeFactory.constructParametricType(parametrized, parameterClass)
    return ignoreNullMapper.readValue(jsonString, javaType)
  }

  /**
   * val result:Result<List<User>>=
   *  JsonUtils.parseJson(jsonString,Result::class.java,List::class.java,User::class.java)
   */
  fun <T> parseJson(jsonString: String,
                    parametrized: Class<out Any>,
                    parameterClass_1: Class<out Any>, parameterClass_2: Class<out Any>): T {
    val typeFactory = ignoreNullMapper.typeFactory
    val javaType = typeFactory.constructParametricType(parameterClass_1, parameterClass_2)
    val type = typeFactory.constructParametricType(parametrized, javaType)
    return ignoreNullMapper.readValue(jsonString, type)
  }

  /**
   * 将json字符串解析为clazz类型的List
   */
  fun <E> parseArray(jsonString: String, clazz: Class<E>): List<E> {
    val javaType = ignoreNullMapper.typeFactory.constructParametricType(List::class.java, clazz)
    return ignoreNullMapper.readValue(jsonString, javaType)
  }

  fun <E> parseArray_2(jsonString: String, clazz: Class<E>): Array<E> {
    val javaType = ignoreNullMapper.typeFactory.constructArrayType(clazz)
    return ignoreNullMapper.readValue(jsonString, javaType)
  }

  fun <E> parseSet(jsonString: String, clazz: Class<E>): Set<E> {
    val javaType = ignoreNullMapper.typeFactory.constructParametricType(Set::class.java, clazz)
    return ignoreNullMapper.readValue(jsonString, javaType)
  }

  @JvmOverloads
  fun <K, V> parseMap(jsonString: String?,
                      keyClass: Class<K>,
                      valueClass: Class<V>,
                      mapClass: Class<out Map<out Any, Any>>? = HashMap::class.java): Map<K, V> {
    return if (jsonString?.isBlank() != false) {
      emptyMap()
    } else {
      val type = ignoreNullMapper.typeFactory.constructMapType(mapClass, keyClass, valueClass)
      ignoreNullMapper.readValue(jsonString, type)
    }
  }

  //TODO marked
  fun getJavaType(type: Type): JavaType {
    return if (type is ParameterizedType) {
      val actualTypeArguments = type.actualTypeArguments
      val rowClass = type.rawType as Class<*>
      val javaTypes = arrayOfNulls<JavaType>(actualTypeArguments.size)
      for (i in actualTypeArguments.indices) {
        javaTypes[i] = getJavaType(actualTypeArguments[i])
      }
      TypeFactory.defaultInstance().constructParametricType(rowClass, *javaTypes)
    } else {
      val cla = type as Class<*>
      TypeFactory.defaultInstance().constructParametricType(cla, *arrayOfNulls<JavaType>(0))
    }
  }

}
