package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    /**
     * 新增菜单
     * @param menu
     */
    @Override
    public void add(Menu menu) {
        //直接新增菜单项,不需要管其他的关联表

        if (menu.getParentMenuId() == null){
            menuDao.add(menu);
        }else {
            menuDao.addLevel(menu);
        }
    }

    /**
     * 删除菜单表，删除t_role_menu 表
     *
     * @param id
     */
    @Override
    public void delete(int id) {
        // 判断菜单表是否被角色使用了,查询 t_role_menu 如果返回值大于0就是有被使用则不能删除
        int count = menuDao.findCountByMenu(id);

        if (count > 0) {
            throw new MyException("该菜单已经被角色使用了，不能被删除");
        }
        // 删TM的
        menuDao.delete(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }

    /**
     * 分页查询菜单
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Menu> findPage(QueryPageBean queryPageBean) {
        // 通过page helper来查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //判断是否有查询语句
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+ queryPageBean.getQueryString()+"%");
        }
        Page<Menu> page = menuDao.findPage(queryPageBean.getQueryString());

        PageResult<Menu> pageResult = new PageResult<Menu>(page.getTotal(),page.getResult());

        return pageResult;

    }

    /**
     * 根据id查菜单数据
     * @param id
     * @return
     */
    @Override
    public Menu findById(int id) {
        return menuDao.findById(id);

    }

    /**
     * 更新菜单数据
     * @param menu
     */
    @Override
    public void update(Menu menu) {

            if (menu.getParentMenuId() == null){
                menuDao.update(menu);
            }else {
                menuDao.updateLevel(menu);
            }
    }
//    获取菜单数据
    @Override
    public List<Menu> getMenu() {
        List<Menu> resultList = new ArrayList<>();
        List<Menu> allMenuList = menuDao.findAll();
        assemble(allMenuList, resultList);
        return resultList;
    }

//    装配页面菜单对象
    private void assemble(List<Menu> sourceList, List<Menu> targetList) {
        sourceList.stream().filter((ele) -> ele.getParentMenuId() == null).forEach((ele) -> {
            ele.setChildren(new ArrayList<>());
            targetList.add(ele);
        });
        sourceList.stream().filter((ele) -> ele.getParentMenuId() != null).forEach((ele) -> {
            for (Menu menu : targetList) {
                if (menu.getId() == ele.getParentMenuId()) {
                    menu.getChildren().add(ele);
                }
            }
        });
    }
}
