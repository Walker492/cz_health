package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/31
 */
public interface UserDao {
    /**
     * 通过用户名查询用户信息，包含角色及权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<User> findByCondition(String queryString);

    /**
     * 添加用户
     * @param user
     */
    void add(User user);

    /**
     * 添加用户所对应的角色
     * @param userId
     * @param roleId
     */
    void addUserRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

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
     * @return
     */
    void update(User user);
    /**
     * 删除用户关联的角色
     * @param id
     * @return
     */
    void deleteUserRole(Integer id);
    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    void deleteById(int id);
    int findCountByUserId(int id);
}
