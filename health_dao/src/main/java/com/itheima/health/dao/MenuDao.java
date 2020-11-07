package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuDao {
    /**
     * 新增菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     * 查询菜单与角色关联
     * @param id
     * @return
     */
    int findCountByMenu(int id);

    /**
     * 删除菜单
     * @param id
     */
    void delete(int id);

    List<Menu> findAll();

    Page<Menu> findPage(String queryString);

    Menu findById(int id);

    void update(Menu menu);

    void addLevel(Menu menu);

    void updateLevel(Menu menu);



}
