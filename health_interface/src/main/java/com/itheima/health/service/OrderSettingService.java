package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/26
 */
public interface OrderSettingService {
    /**
     * 批量导入预约设置
     * @param list
     */
    void add(List<OrderSetting> list) throws MyException;

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    /**
     * 通过日期设置预约数量
     * @param os
     */
    void editNumberByDate(OrderSetting os) throws MyException;
}
