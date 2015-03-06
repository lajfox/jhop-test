package com.techstar.modules.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * json的mime为：application/json
 * </p>
 * <p>
 * ie6、７、８不支持application/json,一解析认为是文件，提示下载
 * </p>
 * <p>
 * 将mime改为text/plain、text/html、text/json
 * </p>
 * 
 * @author sundoctor
 * 
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IeResponseBody {

}
