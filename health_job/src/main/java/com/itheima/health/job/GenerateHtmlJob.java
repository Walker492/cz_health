package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/28
 */
@Component
public class GenerateHtmlJob {

    @Autowired
    private Configuration configuration;

    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @Value("${out_put_path}")
    private String out_put_path;// 生成后保存的目录

    /**
     * freemarker使用的配置
     */
    @PostConstruct // xml init-method
    public void init(){
        // 设置模板路径
        configuration.setClassForTemplateLoading(this.getClass(),"/ftl");
        // 设置默认编码
        configuration.setDefaultEncoding("utf-8");
    }

    @Scheduled(initialDelay = 3000,fixedDelay = 30000)
    public void generateHtml(){
        // 获取要生成的套餐的id
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";// 企业中习惯用法
        // 需要处理的套餐id集合
        Set<String> ids = jedis.smembers(key);
        // id|操作符|时间戳
        log.debug("ids: {}", ids==null?0:ids.size());
        if(null != ids && ids.size() > 0){
            for (String id : ids) {
                // id|操作符|时间戳
                log.debug("id: {}", id);
                String[] split = id.split("\\|");
                String setmealId = split[0];// 套餐id
                String operator = split[1];
                if("1".equals(operator)){
                    // 生成套餐详情静态页面
                    generateSetmealDetail(setmealId);
                    log.info("生成套餐详情静态页面");
                }else if("0".equals(operator)){
                    removeFile(setmealId);
                    log.info("删除静态页面");
                }
                // 删除setmeal对应的id的任务
                jedis.srem(key, id);
            }
            //生成静态的套餐列表页面
            try {
                generateSetmealList();
                log.info("生成静态的套餐列表页面 成功!");
            } catch (Exception e) {
                log.error("生成静态的套餐列表页面 失败!",e);
            }
        }
        jedis.close();
    }

    /**
     * 删除套餐详情文件
     * @param setmealId
     */
    private void removeFile(String setmealId){
        String filename = out_put_path + "/setmeal_" + setmealId+".html";
        File file = new File(filename);
        if(file.exists()){
            file.delete();
        }
    }

    private void generateSetmealDetail(String setmealId){
        // 构建模板填充的数据
        Map<String,Object> dataMap = new HashMap<String,Object>();
        //  查询套餐详情
        Setmeal setmeal = setmealService.findDetailById(Integer.valueOf(setmealId));
        //     设置图片完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        // key为模板中的变量名
        dataMap.put("setmeal",setmeal);
        String filename = out_put_path + "/setmeal_" + setmealId+".html";
        try {
            generate("mobile_setmeal_detail.ftl",dataMap,filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成静态列表页面
     */
    private void generateSetmealList() throws Exception{
        // 构建模板填充的数据
        Map<String,Object> dataMap = new HashMap<String,Object>();
        //  查询所有的套餐信息
        List<Setmeal> setmealList = setmealService.findAll();
        //     设置图片完整路径
        setmealList.forEach(s->s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        // key为模板中的变量名
        dataMap.put("setmealList",setmealList);
        String filename = out_put_path + "/mobile_setmeal.html";
        generate("mobile_setmeal.ftl",dataMap,filename);
    }

    private void generate(String templateName,Map<String,Object> dataMap,String filename) throws Exception{
        // 获取模板
        Template template = configuration.getTemplate(templateName);
        // 定义Writer 保存到某个目录下, 这里的utf-8编码不能少
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
        // 使用模板的process方法
        template.process(dataMap,writer);
        // 关闭流
        writer.flush();
        writer.close();
    }
}
