package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 新增菜单
     * @param menu
     */
    void add(Menu menu);

    void delete(int id) throws MyException;

    List<Menu> findAll();

    PageResult<Menu> findPage(QueryPageBean queryPageBean);

    Menu findById(int id);

    void update(Menu menu)throws MyException;

    List<Menu> getMenu();
}
