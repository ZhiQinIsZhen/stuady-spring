package com.liyz.stuady.ioc.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 注释:自动注入
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 18:05
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LyzAutowired {
    String value() default "";
}
