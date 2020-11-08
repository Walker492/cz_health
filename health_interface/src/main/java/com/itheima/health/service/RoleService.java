package com.itheima.health.service;

import com.itheima.health.pojo.Role;

import java.util.List;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;

public interface RoleService {

    List<Role> findAll();
	
	
	//改
	
	 /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 增加
     * @param role
     * @return
     */
    void Add(Integer[] menuIds,Integer[] permissionIds,Role role);

    /**
     * 新增里面的权限查询
     * @return
     */
    List<Permission> findAllPermission();
    /**
     * 新增里面的菜单查询
     * @return
     */
    List<Menu> findAllMenu();

    /**
     * 删除
     * @return
     */
    void delete(Integer id) throws Exception;
    /**
     * 编辑数据回显1Role
     */
    Role updateOne(Integer id);
    /**
     * 编辑数据回显2Menu
     */
    List<Integer> updateTwo(Integer id);
    /**
     * 编辑数据回显3Permission
     */
    List<Integer> updateThree(Integer id);

    /**
     * 编辑传入数据
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    void edit( Integer[] menuIds, Integer[] permissionIds,Role role);
}
