package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/31
 */
public interface UserService {

    /**
     * 通过用户名查询用户信息，包含角色及权限信息
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<User> findPage(QueryPageBean queryPageBean);

    /**
     * 添加用户
     * @param user
     * @param userIds
     */
    void add(User user, Integer[] userIds);

    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 通过用户id查询角色id
     * @param id
     * @return
     */
    List<Integer> findRoleIdsByUserId(int id);

    /**
     * 更新用户信息
     * @param user
     * @param roleIds
     * @return
     */
    void update(User user, Integer[] roleIds);

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    void deleteById(int id);
}
