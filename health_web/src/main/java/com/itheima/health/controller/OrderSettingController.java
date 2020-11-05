package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.POIUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/26
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入预约设置
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) throws Exception{
        try {
            // 解析excel
            List<String[]> strings = POIUtils.readExcel(excelFile);
            // 转换javaBean对象
            List<OrderSetting> list = new ArrayList<OrderSetting>();
            OrderSetting os = null;
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] string : strings) {
                // 一行记录
                // string[0] 日期
                // string[1] 数量
                Date orderDate = sdf.parse(string[0]);
                os = new OrderSetting(orderDate,Integer.valueOf(string[1]));
                list.add(os);
            }
            // 调用服务导入
            orderSettingService.add(list);
            // 返回结果给页面
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            log.error("导入预约设置失败",e);
            throw e;
        }
    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        // 调用服务查询
        List<Map<String,Integer>> data = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,data);
    }

    /**
     * 通过日期 设置预约信息
     * @param os
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting os){
        // 调用服务更新
        orderSettingService.editNumberByDate(os);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

}
