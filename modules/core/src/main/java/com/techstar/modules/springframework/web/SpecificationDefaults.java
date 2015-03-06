package com.techstar.modules.springframework.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SpecificationDefaults {

	String name();// 实体属性查询字符串，按spring data jpa 规范编写

	String[] values() default {};// 查询参数，如{"admin","2012-4-12","100.56"}
}
