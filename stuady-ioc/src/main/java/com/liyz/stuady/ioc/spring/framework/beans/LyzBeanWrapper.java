package com.liyz.stuady.ioc.spring.framework.beans;

import lombok.Getter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 19:58
 */
public class LyzBeanWrapper {

    @Getter
    private Object wrapperInstance;

    @Getter
    private Class<?> wrappedClass;

    public LyzBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrappedClass = instance.getClass();
    }
}
