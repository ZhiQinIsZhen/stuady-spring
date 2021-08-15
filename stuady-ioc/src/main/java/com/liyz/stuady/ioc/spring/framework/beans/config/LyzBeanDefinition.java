package com.liyz.stuady.ioc.spring.framework.beans.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 19:59
 */
@Getter
@Setter
public class LyzBeanDefinition {

    private String factoryBeanName;

    private String beanClassName;
}
