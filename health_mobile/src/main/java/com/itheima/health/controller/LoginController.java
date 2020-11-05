package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 快速登陆
     * @param paramMap
     * @return
     */
    @PostMapping("/check")
    public Result login(@RequestBody Map<String,String> paramMap, HttpServletResponse res){
        // 验证码校验
        // 获取手机号码
        String telephone = paramMap.get("telephone");
        // 1. 通过手机号码获取redis中的验证码
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //2. 没有值
        if(StringUtils.isEmpty(codeInRedis)) {
            //   - 返回 重新获取验证码
            return new Result(false, "请重新获取验证码");
        }
        //3. 有值
        //   - 获取前端传过来的验证码
        String validateCode = paramMap.get("validateCode");
        //   - 比较redis中的验证码与前端的验证是否相同
        if(!codeInRedis.equals(validateCode)){
            //   - 不同时，返回验证码不正确
            return new Result(false, "验证码错误");
        }
        //   - 相同，
        //   删除key，
        jedis.del(key); // 防止重复提交
        // 通过手机号码查询用户是否存在
        Member member = memberService.findByTelephone(telephone);
        if(null == member){
            member = new Member();
            member.setRemark("快速登陆");
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }

        // 添加会员的行为跟踪,记录用户的手机号码到cookie
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(30*24*60*60); // 存活 30 天
        cookie.setPath("/");// 访问符合这个表达式的路径时就带上cookie, 根目录
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
