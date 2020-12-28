package com.test.share.common.validator

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author 费世程
 * @date 2020/10/13 8:34
 */
class MobileConstraint : ConstraintValidator<CheckMobile, String> {

  companion object {
    private const val regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0-8])|(18[0-9])|166|198|199|147)\\d{8}\$"
    private val pattern = Pattern.compile(regex)
  }

  override fun isValid(mobile: String, p1: ConstraintValidatorContext?): Boolean {
    return pattern.matcher(mobile).matches()
  }

}
