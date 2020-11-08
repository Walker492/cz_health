package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.List;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

public interface RoleDao {
    List<Role> findAll();
	
	
	//改
	
	 /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Role> findPage(String queryString);


    /**
     * 增加
     * @param role
     * @return
     */
    int Add(Role role);



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






    //删除
   void delete(Integer id);
    Integer findCountRoleMenu();

    Integer findCountRoleUser();

    Integer findCountRolePermission();

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
     */
    void edit(Role role);

    void roleDeleteMenu(Integer id);

    void roleDeletePermission(Integer id);

    void rolePermission(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

    void roleMenu(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId);
}
