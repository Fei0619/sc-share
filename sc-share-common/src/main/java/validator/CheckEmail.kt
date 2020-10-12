package validator

import javax.validation.Constraint

/**
 * 邮箱参数格式验证注解
 * @author 费世程
 * @date 2020/10/13 0:28
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailConstraint::class])
annotation class CheckEmail(
    val message: String = "邮箱不合法！"
)
