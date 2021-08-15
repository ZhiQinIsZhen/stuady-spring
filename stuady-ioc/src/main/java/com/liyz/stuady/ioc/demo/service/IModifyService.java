package com.liyz.stuady.ioc.demo.service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 20:16
 */
public interface IModifyService {

    /**
     * 增加
     */
    public String add(String name, String addr);

    /**
     * 修改
     */
    public String edit(Integer id, String name);

    /**
     * 删除
     */
    public String remove(Integer id);
}
