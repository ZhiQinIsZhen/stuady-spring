package com.liyz.stuady.ioc.demo.service.impl;

import com.liyz.stuady.ioc.demo.service.IModifyService;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzService;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 20:17
 */
@LyzService
public class ModifyServiceImpl implements IModifyService {

    /**
     * 增加
     */
    public String add(String name,String addr) {
        return "modifyService add,name=" + name + ",addr=" + addr;
    }

    /**
     * 修改
     */
    public String edit(Integer id,String name) {
        return "modifyService edit,id=" + id + ",name=" + name;
    }

    /**
     * 删除
     */
    public String remove(Integer id) {
        return "modifyService id=" + id;
    }
}
