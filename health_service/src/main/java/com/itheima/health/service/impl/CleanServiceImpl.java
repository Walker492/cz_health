package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.CleanDao;
import com.itheima.health.service.CleanService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = CleanService.class)
public class CleanServiceImpl implements CleanService {
    @Autowired
    private CleanDao cleanDao;

    @Override
    public void clean(String format) {
        cleanDao.clean(format);
    }
}
