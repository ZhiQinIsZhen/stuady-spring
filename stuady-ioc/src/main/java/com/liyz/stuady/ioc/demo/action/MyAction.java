package com.liyz.stuady.ioc.demo.action;

import com.liyz.stuady.ioc.demo.service.IModifyService;
import com.liyz.stuady.ioc.demo.service.IQueryService;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzAutowired;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzController;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzRequestMapping;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 20:14
 */
@LyzController
@LyzRequestMapping("/web")
public class MyAction {

    @LyzAutowired
    IQueryService queryService;
    @LyzAutowired
    IModifyService modifyService;

    @LyzRequestMapping("/query.json")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @LyzRequestParam("name") String name){
        String result = queryService.query(name);
        out(response,result);
    }

    @LyzRequestMapping("/add*.json")
    public void add(HttpServletRequest request,HttpServletResponse response,
                    @LyzRequestParam("name") String name,@LyzRequestParam("addr") String addr){
        String result = modifyService.add(name,addr);
        out(response,result);
    }

    @LyzRequestMapping("/remove.json")
    public void remove(HttpServletRequest request, HttpServletResponse response,
                       @LyzRequestParam("id") Integer id){
        String result = modifyService.remove(id);
        out(response,result);
    }

    @LyzRequestMapping("/edit.json")
    public void edit(HttpServletRequest request,HttpServletResponse response,
                     @LyzRequestParam("id") Integer id,
                     @LyzRequestParam("name") String name){
        String result = modifyService.edit(id,name);
        out(response,result);
    }



    private void out(HttpServletResponse resp,String str){
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
