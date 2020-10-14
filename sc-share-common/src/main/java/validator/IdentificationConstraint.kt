package validator

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author 费世程
 * @date 2020/10/13 0:45
 */
class IdentificationConstraint : ConstraintValidator<CheckIdentification, String> {

  companion object {
    private const val regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)" + "|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"
    private val pattern = Pattern.compile(regex)
  }

  override fun isValid(identification: String, p1: ConstraintValidatorContext?): Boolean {
    return pattern.matcher(identification).matches()
  }

}
