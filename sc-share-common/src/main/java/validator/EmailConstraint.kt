package validator

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author 费世程
 * @date 2020/10/13 0:34
 */
class EmailConstraint : ConstraintValidator<CheckEmail, String> {

  private val regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$"
  private val pattern = Pattern.compile(regex)

  override fun isValid(email: String, context: ConstraintValidatorContext?): Boolean {
    return pattern.matcher(email).matches()
  }
}
