package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/31
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 通过用户名查询用户信息，包含角色及权限信息
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<User> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+ queryPageBean.getQueryString()+"%");
        }
        Page<User> page = userDao.findByCondition(queryPageBean.getQueryString());

        PageResult<User> pageResult = new PageResult<>(page.getTotal(),page.getResult());

        return pageResult;
    }

//    public static void main(String[] args) throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date now = new Date();
//        System.out.println(now);
//        String format = simpleDateFormat.format(now);
//        System.out.printf(format);
//        System.out.println(simpleDateFormat.parse(format));
//        User user = new User();
//        user.setBirthday(now);
//        System.out.println(user.getBirthday());
//
//    }
    /**
     * 新增用户
     * @param user
     * @param userIds
     */
    @Override
    @Transactional
    public void add(User user, Integer[] userIds) {
        //对用户的密码进行编码
        String password = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String passHash = encoder.encode(password);
        user.setPassword(passHash);
        //添加用户
        userDao.add(user);
        //获取用户id
        Integer userId = user.getId();
        // 遍历选中的角色ids
        if(null != userIds) {
            // 添加用户与角色的关系
            for (Integer roleId : userIds) {
                userDao.addUserRole(userId,roleId);
            }
        }
    }

    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    /**
     * 通过用户id查询角色id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdsByUserId(int id) {
        List<Integer> roleIds = userDao.findRoleIdsByUserId(id);
        return roleIds;
    }

    /**
     * 更新用户信息
     * @param user
     * @param roleIds
     * @return
     */
    @Transactional
    @Override
    public void update(User user, Integer[] roleIds) {
        // 更新检查组
        userDao.update(user);
        // 删除旧关系
        userDao.deleteUserRole(user.getId());
        // 添加新关系
        if(null != roleIds){
            for (Integer roleId : roleIds) {
                userDao.addUserRole(user.getId(), roleId);
            }
        }
    }

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    @Override
    public void deleteById(int id) {
        userDao.deleteById(id);
        // 判断是否被角色使用了
        int count = userDao.findCountByUserId(id);
        // 使用了
        if(count > 0){
            throw new MyException("该用户已经有角色了，不能删除");
        }
        // 没使用
        // 先删除用户与角色的关系
        userDao.deleteUserRole(id);
        // 删除用户
        userDao.deleteById(id);
    }


}
