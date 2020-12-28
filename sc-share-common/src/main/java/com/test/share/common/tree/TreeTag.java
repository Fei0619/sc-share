package com.test.share.common.tree;

import kotlin.annotation.MustBeDocumented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 费世程
 * @date 2020/11/30 10:24
 */
@MustBeDocumented
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TreeTag {

  String nodeParam();

  String parentNodeParam();

  String childListParam();

}
