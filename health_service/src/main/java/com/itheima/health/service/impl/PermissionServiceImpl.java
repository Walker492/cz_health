package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;

import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.List;
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 添加权限
     */
    @Transactional
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean) {
        // 页码，与大小 pageHelper复杂的查询语句，使用手工分页方式，影响性能
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 判断是否有查询条件 如果有要实现模糊查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //【注意】 非空要加!
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，查询语句会被分页
        Page<Permission> page = permissionDao.findPage(queryPageBean.getQueryString());
        // 为什么返回pageResult而不返回page对象
        // 1. total是基础数据类型，rpc后，数据会丢失
        // 2. 代码耦合了
        PageResult<Permission> pageResult = new PageResult<Permission>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 更新
     */
    @Transactional
    @Override
    public void update(Permission permission) {
        permissionDao.update(permission);
    }

    /**
     * 通过id删除
     */
    @Override
    @Transactional
    public void deleteById(int id) throws MyException{
        int cont = permissionDao.findrolePermissionId(id);
        if (cont > 0) {
            throw new MyException("该权限已经被角色使用了，不能删除");
        } else {
            permissionDao.deleterolepermissionId(id);
            permissionDao.deleteById(id);
        }
    }
    /**
     * 查询所有
     */
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    /**
     * 通过id查询权限
     */
    @Override
    public Permission findById(int id) {
        return permissionDao.findById(id);
    }
}
