package com.liyz.stuady.ioc.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 18:10
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LyzRequestMapping {
    String value() default "";
}
