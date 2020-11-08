package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    @GetMapping("/findAll")
    public Result findAll(){
        // 调用服务查询
        List<Role> list = roleService.findAll();
        // 封装到Result再返回
        return new Result(true, "查询角色成功",list);
    }
/**
     * 分页条件查询
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = roleService.findPage(queryPageBean);
        return new Result(true, "查询分页成功", pageResult);
    }



    /**
     * 增加
     *
     * @param role
     * @return
     */
    @RequestMapping("add")
    public Result Add(  Integer[] menuIds,Integer[] permissionIds,@RequestBody Role role) {
        try {
       roleService.Add(menuIds,permissionIds,role);
        return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加角色失败");
        }
    }

    /**
     * 新增里面的权限查询
     *
     * @param
     * @return
     */
    @GetMapping("findAllPermission")
    public Result findAllPermission() {
        try {
        List<Permission> list = roleService.findAllPermission();
        return new Result(true, "成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询所有权限失败");
        }
    }

    /**
     * 新增里面的菜单查询
     *
     * @param
     * @return
     */
    @GetMapping("findAllMenu")
    public Result findAllMenu() {
        try {
        List<Menu> list = roleService.findAllMenu();
        return new Result(true, "成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询所有菜单失败");
        }
    }

    /**
     * 删除
     * @return
     */
    @GetMapping("delete")
    public Result delete(Integer id) {
        try {
            roleService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }
    /**
     * 编辑数据回显1Role
     */
    @GetMapping("updateOne")
    public Result updateOne(Integer id) {
        try {
            Role result = roleService.updateOne(id);
            return new Result(true, "编辑成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "回显formData失败");
        }
    }
    /**
     * 编辑数据回显2Menu
     */
    @GetMapping("updateTwo")
    public Result updateTwo(Integer id){
        try {
            List<Integer> two = roleService.updateTwo(id);
            return new Result(true, "编辑成功", two);
        }catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "回显MenuIds失败");
            }
    }
    /**
     * 编辑数据回显3Permission
     */
    @GetMapping("updateThree")
    public Result updateThree(Integer id){
        try {
        List<Integer> three= roleService.updateThree(id);
        return new Result(true,"编辑成功",three);
    }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "回显PermissionIds失败");
        }
    }
    /**
     * 编辑传入数据
     */
    @PostMapping("edit")
    public Result edit(Integer[] menuIds, Integer[] permissionIds,@RequestBody  Role role){

        try {
            roleService.edit(menuIds,permissionIds,role);
            return new Result(true, "更新角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新角色失败");
        }
    }
}
