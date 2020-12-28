package com.test.share.common.validator

import javax.validation.Constraint

/**
 * 身份证号参数格式验证注解
 * @author 费世程
 * @date 2020/10/13 0:43
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IdentificationConstraint::class])
annotation class CheckIdentification(
    val message: String = "身份证号格式不合法！"
)
