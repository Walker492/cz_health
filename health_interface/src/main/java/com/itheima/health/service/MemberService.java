package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

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

    //会员数量gender饼图
    List<Map<Integer, String>> getMemberGenderPieReport();


    //会员数量年龄段饼图
    List<Map<String, Object>> getMemberAgePieReport(List<String> agePieces);
}
