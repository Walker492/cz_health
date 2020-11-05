package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/25
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传图片
     * name="imgFile"
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //- 获取原文件名才可以获取它后缀名
        String originalFilename = imgFile.getOriginalFilename();
        // 获取文件的扩展名，即后缀名 dlrb.jpg  => .jpg
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //- 产生唯一标识，拼接后缀名，唯一的文件名(七牛的仓库 )
        String uniqueName = UUID.randomUUID().toString() + extension;
        //- 调用7牛utils上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), uniqueName);
            // 上传成功
            /**
             * {
             *     flag
             *     message
             *     data:{
             *     	  domain:  http://qiqhd7v6v.hn-bkt.clouddn.com/
             *     	  imgName: dd2.jpg
             *     }
             * }
             */
            Map<String,String> resultMap = new HashMap<String,String>();
            resultMap.put("domain",QiNiuUtils.DOMAIN);
            resultMap.put("imgName",uniqueName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,resultMap);
        } catch (IOException e) {
            log.error("上传文件失败",e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal 套餐信息
     * @param checkgroupIds 选中的检查组id
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务添加
        Integer setmealId = setmealService.add(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmealId+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        /**
         * {
         *     flag:
         *     message:
         *     data:{
         *          setmeal: setmeal,
         *          domain: QiNiuUtils.DOMAIN
         *     }
         * }
         */
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        resultMap.put("setmeal",setmeal);
        resultMap.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /**
     * 查询属于这个套餐的选中的检查组id
     * @param id
     * @return
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        // 后端list => 前端 [], javaBean 或 map => json{}
        // 查询属于这个套餐的选中的检查组id
        List<Integer> list = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }

    /**
     * 编辑套餐的提交
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务更新
        setmealService.update(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmeal.getId()+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "更新套餐成功");
    }

    /**
     * 删除套餐
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,id+"|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, "删除套餐成功！");
    }

}
