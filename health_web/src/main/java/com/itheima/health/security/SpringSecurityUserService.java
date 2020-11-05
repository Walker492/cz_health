package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/31
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    /**
     * 认证与授权
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过用户名查询数据
        com.itheima.health.pojo.User userInDB = userService.findUserByUsername(username);
        if(null != userInDB) {
            // 授权, userDetail是security需要的
            //String username, 用户名
            //String password, 密码，必须是从数据库查询到的密码
            //Collection<? extends GrantedAuthority> authorities 用户的权限集合
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            // 遍历用户的角色与权限
            // 用户拥有的角色
            Set<Role> roles = userInDB.getRoles();
            if(null != roles){
                GrantedAuthority sga = null;
                for (Role role : roles) {
                    // 角色名, 授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);
                    // 授予权限, 这个角色下所拥有的权限
                    Set<Permission> permissions = role.getPermissions();
                    if(null != permissions){
                        for (Permission permission : permissions) {
                            // 授予权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }

            User securityUser = new User(username, userInDB.getPassword(),authorities);
            return securityUser;
        }
        return null;
    }
}
