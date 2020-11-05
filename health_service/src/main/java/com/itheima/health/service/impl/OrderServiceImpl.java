package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单服务
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 体检预约
     * @param orderInfo
     * @return
     */
    @Override
    @Transactional
    public Order submit(Map<String, String> orderInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 前端传过来的预约日期，字符串
        String orderDateStr = orderInfo.get("orderDate");
        Date orderDate = null;
        try {
            // 转成日期类型
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
            throw new MyException("预约日期格式不正确");
        }
        //1. 通过预约日期查询预约设置信息
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(null == orderSetting) {
            //   - 不存在，报错，所选日期不能预约，请选择其它日期
            throw new MyException("所选日期不能预约，请选择其它日期");
        }
        if(orderSetting.getReservations() >= orderSetting.getNumber()) {
            //   - 存在 判断是否约满 reservations>=number
            //     - 约满，报错：所选日期预约已满，请选择其它日期
            throw new MyException("所选日期，预约已满，请选择其它日期");
        }
        String telephone = orderInfo.get("telephone");
        //2. 通过手机号码查询会员是否存在?
        Member member = memberDao.findByTelephone(telephone);
        // 构建查询条件
        Order order = new Order();
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        order.setOrderDate(orderDate);
        if(null != member) {
            //   - 存在才有可能重复预约
            //   	    判断是否重复预约 通过member_id, orderDate, setmeal_id 查询订单表
            order.setMemberId(member.getId());
            List<Order> list = orderDao.findByCondition(order);
            //      	 有数据就是 重复预约，
            if(null != list && list.size() > 0){
                //则报错，已经预约过了，不能重复预约
                throw new MyException("所选日期已经预约过了，不能重复预约");
            }
        }else {
            //   - 不存在是不可能重复预约
            member = new Member();
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            String idCard = orderInfo.get("idCard");
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setPassword(idCard.substring(idCard.length()-6));
            member.setRemark("微信公众号注册");
            //     添加新会员
            memberDao.add(member);
            order.setMemberId(member.getId());
        }
        //3. 添加订单
        order.setOrderType(orderInfo.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO); // 未到诊
        orderDao.add(order);
        //4. 通过日期更新已预约人数
        int affectedRowCount = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if(affectedRowCount == 0){
            throw new MyException("所选日期，预约已满，请选择其它日期");
        }
        return order;
    }

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById4Detail(id);
    }

}
