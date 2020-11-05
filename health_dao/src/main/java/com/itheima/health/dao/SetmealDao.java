package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/25
 */
public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查组的关系
     * @param checkgroupId
     * @param setmealId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 查询属于这个套餐的选中的检查组id
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 更新套餐
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐与检查组的关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 通过套餐id统计订单个数
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 通过id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 获取套餐的所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 套餐列表
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);

    Setmeal findDetailById2(int id);

    List<CheckGroup> findCheckGroupListBySetmealId(int id);

    List<CheckItem> findCheckItemByCheckGroupId(Integer id);

    /**
     * 套餐预约占比
     * @return
     */
    List<Map<String, Object>> getSetmealReport();
}
