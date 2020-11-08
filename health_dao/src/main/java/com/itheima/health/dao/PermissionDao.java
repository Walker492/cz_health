package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionDao {
    void add(Permission permission);

    Page<Permission> findPage(String queryString);

    void update(Permission permission);

    void deleteById(int id);

    List<Permission> findAll();

    Permission findById(int id);

    int findrolePermissionId(int id);

    void deleterolepermissionId(int id);
}
