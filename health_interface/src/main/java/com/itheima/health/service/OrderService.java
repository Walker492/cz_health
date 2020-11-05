package com.itheima.health.service;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
public interface OrderService {
    /**
     * 提交预约
     * @param orderInfo
     * @return
     */
    Order submit(Map<String, String> orderInfo) throws MyException;

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    Map<String, Object> findById(int id);

}
