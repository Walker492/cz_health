package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authority")
public class PermissionController {
    @Reference
    PermissionService permissionService;
    /**
     * 添加权限
     */
    @PostMapping("/add")
    public Result add(@RequestBody Permission permission){
        // 调用服务添加
        permissionService.add(permission);
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 分页条件查询权限
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<Permission> pageResult = permissionService.findPage(queryPageBean);
        return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,pageResult);
    }

    /**
     * 通过id查询权限
     */
    @GetMapping("/findById")
    public Result findById(int id){
      Permission permission = permissionService.findById(id);
        return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,permission);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public Result update(@RequestBody Permission permission){
        // 调用服务更新
        permissionService.update(permission);
        return new Result(true, MessageConstant.UPDATE_PERMISSION_SUCCESS);
    }

    /**
     * 通过id删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        permissionService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    /**
     * 查询所有
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAll();
        return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,list);
    }
}
