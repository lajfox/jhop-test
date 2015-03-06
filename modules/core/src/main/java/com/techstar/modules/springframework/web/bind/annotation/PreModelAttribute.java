package com.techstar.modules.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreModelAttribute {

	String value() default "id";// 实体主键属性名称

	boolean preparable() default true;//预加载实体（二次绑定）主要用于不隐藏属性的加载

	String[] disallowedFields() default {};//忽略处理的属性
}
