package validator

import javax.validation.Constraint

/**
 * 手机号参数格式验证注解
 * @author 费世程
 * @date 2020/10/13 8:32
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MobileConstraint::class])
annotation class CheckMobile(
    val message: String = "手机号不符合规范！"
)
