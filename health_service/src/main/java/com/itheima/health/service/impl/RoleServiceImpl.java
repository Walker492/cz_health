package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
	
	//��
	 /**
     * ��ҳ��ѯ
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //ҳ�����С
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //�ж��Ƿ��в�ѯ���� �����Ҫʵ��ģ����ѯ
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        //������ѯ,��ѯ���ᱻ��ҳ
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        PageResult<Role> pageResult = new PageResult<Role>(page.getTotal(),page.getResult());
        return pageResult;
    }


    /**
     * ����
     * @param role
     * @return
     */
    @Override
    public void Add(Integer[] menuIds,Integer[] permissionIds,Role role) {
        roleDao.Add(role);
        Integer roleId = role.getId();
        for (Integer menuId : menuIds) {
            roleDao.roleMenu(roleId, menuId);
        }
        for (Integer permissionId : permissionIds) {
            roleDao.rolePermission(roleId, permissionId);
        }
    }



    /**
     * ���������Ȩ�޲�ѯ
     * @return
     */
    @Override
    public List<Permission> findAllPermission() {
       return roleDao.findAllPermission();
    }



    /**
     * ��������Ĳ˵���ѯ
     * @return
     */
    @Override
    public List<Menu> findAllMenu() {
        return roleDao.findAllMenu();
    }



    /**
     * ɾ��
     * @param id
     */
    @Override
    public void delete(Integer id) throws Exception{
        Integer RoleMenuID = roleDao.findCountRoleMenu();
        Integer RoleUserID = roleDao.findCountRoleUser();
        Integer RolePermissionID = roleDao.findCountRolePermission();
        if (!(RoleMenuID ==0&&RoleUserID==0&&RolePermissionID==0)){
            throw new Exception("ʹ���а�,û��ɾ��");
        }
        roleDao.delete(id);
    }


    /**
     * �༭���ݻ���1Role
     */
    @Override
    public Role updateOne(Integer id) {
        return  roleDao.updateOne(id);

    }


    /**
     * �༭���ݻ���2Menu
     */
    @Override
    public List<Integer> updateTwo(Integer id) {
        return  roleDao.updateTwo(id);
    }


    /**
     * �༭���ݻ���3Permission
     */
    @Override
    public List<Integer> updateThree(Integer id) {
        return  roleDao.updateThree(id);
    }



    /**
     * �༭��������
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @Override
    public void edit(Integer[] menuIds, Integer[] permissionIds,Role role) {
        roleDao.edit(role);
        //Ҫ��ɾ���ټ���
        roleDao.roleDeleteMenu(role.getId());
        roleDao.roleDeletePermission(role.getId());



        for (Integer menuId : menuIds){
            roleDao.roleMenu(role.getId(),menuId);
        }
        for (Integer permissionId : permissionIds){
            roleDao.rolePermission(role.getId(),permissionId);
        }

    }
}
