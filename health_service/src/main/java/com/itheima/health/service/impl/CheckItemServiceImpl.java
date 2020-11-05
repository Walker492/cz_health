package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *
 * </p>
 * interfaceClass 发布服务时使用的接口
 * @author: Eric
 * @since: 2020/10/22
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有的检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 页码，与大小 pageHelper复杂的查询语句，使用手工分页方式，影响性能
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 判断是否有查询条件 如果有要实现模糊查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            //【注意】 非空要加!
            queryPageBean.setQueryString("%"+ queryPageBean.getQueryString()+"%");
        }
        // 条件查询，查询语句会被分页
        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());
        // 为什么返回pageResult而不返回page对象
        // 1. total是基础数据类型，rpc后，数据会丢失
        // 2. 代码耦合了
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(int id) {
        // 判断是否被检查组使用了
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            // 被使用了，则要报错
            throw new MyException("该检查项已经被使用了，不能删除!");
        }
        // 没使用，就可以删除
        checkItemDao.deleteById(id);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
