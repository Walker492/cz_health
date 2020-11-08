package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    /**
     * 获取登陆用户名
     *
     * @return
     */
    @GetMapping("/getUsername")
    public Result getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("登陆的用户名:" + user.getUsername());
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
    }

    /**
     * 用户数据分页查询
     *
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用服务分页查询
        PageResult<com.itheima.health.pojo.User> pageResult = userService.findPage(queryPageBean);
        return new Result(true, "查询用户数据失败",pageResult);
    }

    /**
     * 新增用户
     * @param user
     * @param userIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer[] userIds) {
        // 调用服务添加
        userService.add(user, userIds);
        return new Result(true, "新增用户成功");
    }


    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        com.itheima.health.pojo.User user = userService.findById(id);
        return new Result(true, "查询用户成功",user);
    }

    /**
     * 通过用户id查询角色id
     * @param id
     * @return
     */
    @GetMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(int id) {
        List<Integer> roleIds = userService.findRoleIdsByUserId(id);
        return new Result(true,"查询角色id成功",roleIds);
    }

    /**
     * 更新用户信息
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds) {
        System.out.println(Arrays.toString(roleIds));
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        userService.update(user,roleIds);
        return new Result(true,"更新用户成功");
    }

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id) {
        userService.deleteById(id);
        return new Result(true,"删除用户成功");
    }







}
