package com.liyz.stuady.ioc.spring.framework.webmvc.servlet;

import com.liyz.stuady.ioc.spring.framework.annotation.LyzController;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzRequestMapping;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzRequestParam;
import com.liyz.stuady.ioc.spring.framework.context.LyzApplicationContext;
import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PipedReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 18:24
 */
public class LyzDispatchServlet extends HttpServlet {

    private LyzApplicationContext applicationContext;

    //IoC容器，key默认是类名首字母小写，value就是对应的实例对象
//    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<String, Method>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,Detail : " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+","/");

        if(!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 Not Found!!!");
            return;
        }

        Map<String,String[]> params = req.getParameterMap();

        Method method = this.handlerMapping.get(url);

        //获取形参列表
        Class<?> [] parameterTypes = method.getParameterTypes();
        Object [] paramValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class paramterType = parameterTypes[i];
            if(paramterType == HttpServletRequest.class){
                paramValues[i] = req;
            }else if(paramterType == HttpServletResponse.class){
                paramValues[i] = resp;
            }else if(paramterType == String.class){
                //通过运行时的状态去拿到你
                Annotation[] [] pa = method.getParameterAnnotations();
                for (int j = 0; j < pa.length ; j ++) {
                    for(Annotation a : pa[i]){
                        if(a instanceof LyzRequestParam){
                            String paramName = ((LyzRequestParam) a).value();
                            if(!"".equals(paramName.trim())){
                                String value = Arrays.toString(params.get(paramName))
                                        .replaceAll("\\[|\\]","")
                                        .replaceAll("\\s+",",");
                                paramValues[i] = value;
                            }
                        }
                    }
                }

            }
        }
        //暂时硬编码
        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        //赋值实参列表
//        method.invoke(ioc.get(beanName),paramValues);
        method.invoke(applicationContext.getBean(beanName),paramValues);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化Spring核心IoC容器
        applicationContext = new LyzApplicationContext(config.getInitParameter("contextConfigLocation"));

        //==============MVC部分==============
        //5、初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("GP Spring framework is init.");
    }

    private void doInitHandlerMapping() {
//        if(ioc.isEmpty()){ return;}
        if (this.applicationContext.getBeanDefitionCount() == 0) {
            return;
        }
//        for (Map.Entry<String,Object> entry : ioc.entrySet()) {
        for (String beanName : this.applicationContext.getBeanDefitionNames()) {
//            Class<?> clazz = entry.getValue().getClass();
            Class<?> clazz = applicationContext.getBean(beanName).getClass();
            if(!clazz.isAnnotationPresent(LyzController.class)){ continue; }


            //相当于提取 class上配置的url
            String baseUrl = "";
            if(clazz.isAnnotationPresent(LyzRequestMapping.class)){
                LyzRequestMapping requestMapping = clazz.getAnnotation(LyzRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            //只获取public的方法
            for (Method method : clazz.getMethods()) {
                if(!method.isAnnotationPresent(LyzRequestMapping.class)){continue;}
                //提取每个方法上面配置的url
                LyzRequestMapping requestMapping = method.getAnnotation(LyzRequestMapping.class);

                // //demo//query
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+","/");
                handlerMapping.put(url,method);
                System.out.println("Mapped : " + url + "," + method);
            }

        }
    }

    //自己写，自己用
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
//        if(chars[0] > )
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
