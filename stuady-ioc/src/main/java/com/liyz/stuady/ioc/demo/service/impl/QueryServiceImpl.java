package com.liyz.stuady.ioc.demo.service.impl;

import com.liyz.stuady.ioc.demo.service.IQueryService;
import com.liyz.stuady.ioc.spring.framework.annotation.LyzService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/15 20:17
 */
@Slf4j
@LyzService
public class QueryServiceImpl implements IQueryService {

    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        log.info("这是在业务方法中打印的：" + json);
        return json;
    }
}
