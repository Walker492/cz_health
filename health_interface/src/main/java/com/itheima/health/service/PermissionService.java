package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 添加权限
     */
    void add(Permission permission);

    /**
     * 分页条件查询权限
     */
    PageResult<Permission> findPage(QueryPageBean queryPageBean);

    /**
     * 更新
     */
    void update(Permission Permission);

    /**
     * 通过id删除
     */
    void deleteById(int id) throws MyException;

    /**
     * 查询所有
     */
    List<Permission> findAll();

    /**
     * 通过id查询权限
     */
    Permission findById(int id);
}
