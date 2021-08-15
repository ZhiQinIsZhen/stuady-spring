package com.liyz.stuady.ioc.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 18:11
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LyzRequestParam {
    String value() default "";
}
