package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入
     * @param list
     * @throws MyException
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> list) throws MyException {
        //- 遍历数据
        for (OrderSetting orderSetting : list) {
            //- 通过日期查询预约设置信息
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if(null != osInDB) {
                //- 存在
                //  - 判断更新后的最大预约数是否大于已预约数
                if(orderSetting.getNumber() < osInDB.getReservations()) {
                    //    - 小于，则要报错，最大可预约数必须大于已预约数
                    throw new MyException("最大预约数不能小于已已预约数");
                }
                //  - 更新最大可预约数
                orderSettingDao.updateNumber(orderSetting);
            }else {
                //- 不存在
                //  - 添加预约设置
                orderSettingDao.add(orderSetting);
            }
        }

    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        month+="%";
        return orderSettingDao.getOrderSettingByMonth(month);
    }

    /**
     * 通过日期设置预约数量
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //- 通过日期查询预约设置信息
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(null != osInDB) {
            //- 存在
            //  - 判断更新后的最大预约数是否大于已预约数
            if(orderSetting.getNumber() < osInDB.getReservations()) {
                //    - 小于，则要报错，最大可预约数必须大于已预约数
                throw new MyException("最大预约数不能小于已已预约数");
            }
            //  - 更新最大可预约数
            orderSettingDao.updateNumber(orderSetting);
        }else {
            //- 不存在
            //  - 添加预约设置
            orderSettingDao.add(orderSetting);
        }
    }
}
