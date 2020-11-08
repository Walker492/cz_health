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
	
	//改
	 /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //页码与大小
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //判断是否有查询条件 如果有要实现模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        //条件查询,查询语句会被分页
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        PageResult<Role> pageResult = new PageResult<Role>(page.getTotal(),page.getResult());
        return pageResult;
    }


    /**
     * 增加
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
     * 新增里面的权限查询
     * @return
     */
    @Override
    public List<Permission> findAllPermission() {
       return roleDao.findAllPermission();
    }



    /**
     * 新增里面的菜单查询
     * @return
     */
    @Override
    public List<Menu> findAllMenu() {
        return roleDao.findAllMenu();
    }



    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id) throws Exception{
        Integer RoleMenuID = roleDao.findCountRoleMenu();
        Integer RoleUserID = roleDao.findCountRoleUser();
        Integer RolePermissionID = roleDao.findCountRolePermission();
        if (!(RoleMenuID ==0&&RoleUserID==0&&RolePermissionID==0)){
            throw new Exception("使用中啊,没法删除");
        }
        roleDao.delete(id);
    }


    /**
     * 编辑数据回显1Role
     */
    @Override
    public Role updateOne(Integer id) {
        return  roleDao.updateOne(id);

    }


    /**
     * 编辑数据回显2Menu
     */
    @Override
    public List<Integer> updateTwo(Integer id) {
        return  roleDao.updateTwo(id);
    }


    /**
     * 编辑数据回显3Permission
     */
    @Override
    public List<Integer> updateThree(Integer id) {
        return  roleDao.updateThree(id);
    }



    /**
     * 编辑传入数据
     * @param role
     * @param menuIds
     * @param permissionIds
     */
    @Override
    public void edit(Integer[] menuIds, Integer[] permissionIds,Role role) {
        roleDao.edit(role);
        //要先删除再加入
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
