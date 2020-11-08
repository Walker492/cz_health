package com.itheima.health.dao;

import com.itheima.health.pojo.Role;

import java.util.List;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

public interface RoleDao {
    List<Role> findAll();
	
	
	//��
	
	 /**
     * ��ҳ��ѯ
     * @param queryString
     * @return
     */
    Page<Role> findPage(String queryString);


    /**
     * ����
     * @param role
     * @return
     */
    int Add(Role role);



 /**
     * ���������Ȩ�޲�ѯ
     * @return
     */
    List<Permission> findAllPermission();



    /**
     * ��������Ĳ˵���ѯ
     * @return
     */
    List<Menu> findAllMenu();






    //ɾ��
   void delete(Integer id);
    Integer findCountRoleMenu();

    Integer findCountRoleUser();

    Integer findCountRolePermission();

    /**
     * �༭���ݻ���1Role
     */
    Role updateOne(Integer id);



    /**
     * �༭���ݻ���2Menu
     */
    List<Integer> updateTwo(Integer id);



    /**
     * �༭���ݻ���3Permission
     */
    List<Integer> updateThree(Integer id);



    /**
     * �༭��������
     */
    void edit(Role role);

    void roleDeleteMenu(Integer id);

    void roleDeletePermission(Integer id);

    void rolePermission(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

    void roleMenu(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId);
}
