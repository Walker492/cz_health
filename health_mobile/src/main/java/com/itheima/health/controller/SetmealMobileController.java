package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/28
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    /**
     * 套餐列表
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        String key = "findAll";
//        先判断redis中有没有数据
        Jedis jedis = jedisPool.getResource();
        String setmeal = jedis.get(key);
        if (setmeal != null) {
            List list = JSON.parseObject(setmeal, List.class);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        }else {
//            不存在话 调用dao层，存到redis，再返回

            List<Setmeal> setmealList = setmealService.findAll();
            setmealList.forEach(s -> s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
//            存入到redis
            jedis.set(key, JSON.toJSONString(setmealList));
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
        }
    }

    /**
     * 套餐详情 
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        //判断redis中有没有数据
        String key = "findDetail"+id;
        Jedis jedis = jedisPool.getResource();
//        获取redis中的数据
        String detail = jedis.get(key);
        if (detail != null) {
//            如果存在的话则直接返回
            Setmeal setmeal = JSON.parseObject(detail, Setmeal.class);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }else{
//            不存在的话则调用业务层查询后储存到redis中再返回
            // 调用服务查询
            Setmeal s = setmealService.findDetailById(id);
            // 图片的完整路径
            s.setImg(QiNiuUtils.DOMAIN+s.getImg());
            jedis.set(key,JSON.toJSONString(s));
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,s);
        }
    }

    /**
     * 套餐详情
     */
    @GetMapping("/findDetailById2")
    public Result findDetailById2(int id){
        // 调用服务查询
        Setmeal s = setmealService.findDetailById2(id);
        // 图片的完整路径
        s.setImg(QiNiuUtils.DOMAIN+s.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,s);
    }

    /**
     * 套餐详情
     */
    @GetMapping("/findDetailById3")
    public Result findDetailById3(int id){
        // 调用服务查询
        Setmeal s = setmealService.findDetailById3(id);
        // 图片的完整路径
        s.setImg(QiNiuUtils.DOMAIN+s.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,s);
    }

    /**
     * 套餐基本信息
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Setmeal s = setmealService.findById(id);
        // 图片的完整路径
        s.setImg(QiNiuUtils.DOMAIN+s.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,s);
    }
}
