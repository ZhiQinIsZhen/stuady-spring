package com.liyz.stuady.ioc.spring.framework.context;

import com.liyz.stuady.ioc.spring.framework.annotation.LyzAutowired;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzController;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzService;
import com.liyz.stuady.ioc.spring.framework.beans.LyzBeanWrapper;
import com.liyz.stuady.ioc.spring.framework.beans.config.LyzBeanDefinition;
import com.liyz.stuady.ioc.spring.framework.beans.support.LyzBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 19:56
 */
public class LyzApplicationContext {

    private LyzBeanDefinitionReader reader;

    private Map<String,LyzBeanDefinition> beanDefinitionMap = new HashMap<String, LyzBeanDefinition>();

    private Map<String,LyzBeanWrapper> factoryBeanInstanceCache = new HashMap<String, LyzBeanWrapper>();
    private Map<String,Object> factoryBeanObjectCache = new HashMap<String, Object>();

    public LyzApplicationContext(String... configLocations) {

        //1、加载配置文件
        reader = new LyzBeanDefinitionReader(configLocations);

        try {
            //2、解析配置文件，封装成BeanDefinition
            List<LyzBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            //3、把BeanDefintion缓存起来
            doRegistBeanDefinition(beanDefinitions);

            doAutowrited();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doAutowrited() {
        //调用getBean()
        //这一步，所有的Bean并没有真正的实例化，还只是配置阶段
        for (Map.Entry<String,LyzBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            getBean(beanName);
        }
    }

    private void doRegistBeanDefinition(List<LyzBeanDefinition> beanDefinitions) throws Exception {
        for (LyzBeanDefinition beanDefinition : beanDefinitions) {
            if(this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + "is exists");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
        }
    }

    //Bean的实例化，DI是从而这个方法开始的
    public Object getBean(String beanName){
        //1、先拿到BeanDefinition配置信息
        LyzBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        //2、反射实例化newInstance();
        Object instance = instantiateBean(beanName,beanDefinition);
        //3、封装成一个叫做BeanWrapper
        LyzBeanWrapper beanWrapper = new LyzBeanWrapper(instance);
        //4、保存到IoC容器
        factoryBeanInstanceCache.put(beanName,beanWrapper);
        //5、执行依赖注入
        populateBean(beanName,beanDefinition,beanWrapper);

        return beanWrapper.getWrapperInstance();
    }

    private void populateBean(String beanName, LyzBeanDefinition beanDefinition, LyzBeanWrapper beanWrapper) {
        //可能涉及到循环依赖？
        //A{ B b}
        //B{ A b}
        //用两个缓存，循环两次
        //1、把第一次读取结果为空的BeanDefinition存到第一个缓存
        //2、等第一次循环之后，第二次循环再检查第一次的缓存，再进行赋值

        Object instance = beanWrapper.getWrapperInstance();

        Class<?> clazz = beanWrapper.getWrappedClass();

        //在Spring中@Component
        if(!(clazz.isAnnotationPresent(LyzController.class) || clazz.isAnnotationPresent(LyzService.class))){
            return;
        }

        //把所有的包括private/protected/default/public 修饰字段都取出来
        for (Field field : clazz.getDeclaredFields()) {
            if(!field.isAnnotationPresent(LyzAutowired.class)){ continue; }

            LyzAutowired autowired = field.getAnnotation(LyzAutowired.class);

            //如果用户没有自定义的beanName，就默认根据类型注入
            String autowiredBeanName = autowired.value().trim();
            if("".equals(autowiredBeanName)){
                //field.getType().getName() 获取字段的类型
                autowiredBeanName = field.getType().getName();
            }

            //暴力访问
            field.setAccessible(true);

            try {
                if(this.factoryBeanInstanceCache.get(autowiredBeanName) == null){
                    continue;
                }
                //ioc.get(beanName) 相当于通过接口的全名拿到接口的实现的实例
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }

    }


    //创建真正的实例对象
    private Object instantiateBean(String beanName, LyzBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            if (this.factoryBeanInstanceCache.containsKey(beanName)) {
                instance = this.factoryBeanInstanceCache.get(beanName);
            } else {
                Class<?> clazz = Class.forName(className);
                //2、默认的类名首字母小写
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanName, instance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    public Object getBean(Class beanClass){
        return getBean(beanClass.getName());
    }

    public int getBeanDefitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }
}
