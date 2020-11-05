package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 提交 预约
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        // 检验验证码
        // 获取手机号码
        String telephone = orderInfo.get("telephone");
        // 1. 通过手机号码获取redis中的验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //2. 没有值
        if(StringUtils.isEmpty(codeInRedis)) {
            //   - 返回 重新获取验证码
            return new Result(false, "请重新获取验证码");
        }
        //3. 有值
        //   - 获取前端传过来的验证码
        String validateCode = orderInfo.get("validateCode");
        //   - 比较redis中的验证码与前端的验证是否相同
        if(!codeInRedis.equals(validateCode)){
            //   - 不同时，返回验证码不正确
            return new Result(false, "验证码错误");
        }
        //   - 相同，
        //   删除key，
        jedis.del(key); // 防止重复提交
        //   设置预约类型（health_mobile, 写死为微信预约）
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        //   调用服务，返回结果给页面,要返回order对象是因为页面中需要用到它的id
        Order order = orderService.submit(orderInfo);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询订单信息
        Map<String,Object> orderInfo = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
