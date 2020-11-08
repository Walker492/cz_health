package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

//    /**
//     * 通过角色去查菜单
//     */
//    @PostMapping("/getMenuList")
//    public Result getMenu(@RequestBody Role role){
//
//    }

    @Reference
    private MenuService menuService;

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu){

        menuService.add(menu);

        return new Result(true,"新增菜单成功");

    }

    /**
     * 删除菜单
     * 删除菜单表的同时还要删除菜单的 t_role_menu 表的关联
     */
    @PostMapping("/delete")
    public Result delete(int id){
        menuService.delete(id);

        return new Result(true,"删除菜单表成功");
    }

    /**
     * 这玩意应该是给admin用的
     * 查所有菜单信息，用于展示
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<Menu> list = menuService.findAll();

        return new Result(true,"菜单查询成功", list);
    }

    /**
     * 实现分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Menu> pageResult = menuService.findPage(queryPageBean);

        return new Result(true,"菜单查询成功",pageResult);
    }

    /**
     * 编辑回显
     */
    @GetMapping("/findById")
    public Result findById(int id){

        Menu menu = menuService.findById(id);

        return new Result(true,"回显菜单数据成功",menu);
    }

    /**
     * 编辑菜单提交
     */
    @PostMapping("update")
    public Result update(@RequestBody Menu menu){

        menuService.update(menu);

        return new Result(true,"编辑菜单成功");

    }

    @GetMapping("getMenu")
    public Result getMenu(){
        return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuService.getMenu());
    }
}
