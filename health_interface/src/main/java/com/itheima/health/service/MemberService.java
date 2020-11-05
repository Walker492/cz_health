package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
public interface MemberService {
    /**
     * 通过手机号码查询用户
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加新会员
     * @param member
     */
    void add(Member member);

    /**
     * 查询每个月的会员总数量
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
