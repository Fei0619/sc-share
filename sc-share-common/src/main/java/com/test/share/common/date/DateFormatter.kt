package com.test.share.common.date

import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 费世程
 * @date 2020/12/22 15:01
 */
object DateFormatter {

  private val map = ConcurrentHashMap<String, DateTimeFormatter>()

  /**
   * 通过[pattern]和[locale]获取[DateTimeFormatter]
   * @return DateTimeFormatter
   * @author 费世程
   * @date 2021/1/15 17:06
   */
  fun of(pattern: String = DatePattern.yyyy_MM_dd_HH_mm_ss,
         locale: Locale = Locale.SIMPLIFIED_CHINESE): DateTimeFormatter {
    val key = "$pattern:${locale.language}:${locale.country}"
    return map.computeIfAbsent(key) { DateTimeFormatter.ofPattern(pattern, locale) }
  }

}
