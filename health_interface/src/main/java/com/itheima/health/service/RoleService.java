package com.itheima.health.service;

import com.itheima.health.pojo.Role;

import java.util.List;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;

public interface RoleService {

    List<Role> findAll();
	
	
	//��
	
	 /**
     * ��ҳ������ѯ
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * ����
     * @param role
     * @return
     */
    void Add(Integer[] menuIds,Integer[] permissionIds,Role role);

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

    /**
     * ɾ��
     * @return
     */
    void delete(Integer id) throws Exception;
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
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    void edit( Integer[] menuIds, Integer[] permissionIds,Role role);
}
