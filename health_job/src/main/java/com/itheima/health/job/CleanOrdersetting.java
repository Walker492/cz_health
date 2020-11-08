package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.CleanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CleanOrdersetting {
    @Reference
    private CleanService cleanService;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //每月最后一天凌晨2点执行一次
    @Scheduled(cron = "0 00 02 L * ?")
    //测试用的
//    @Scheduled(cron = "0/2 * * * * ?")
    public void clean() {
        //清理当天之前的旧数据
        String format = sdf.format(new Date());
        //用来测试
//        String format = "2020-03-07";
        System.out.println("任务开始执行");
        cleanService.clean(format);
    }

}
