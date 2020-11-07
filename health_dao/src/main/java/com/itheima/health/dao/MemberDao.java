package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

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
public interface MemberDao {
    /**
     * 查询所有
     * @return
     */
    List<Member> findAll();
    Page<Member> selectByCondition(String queryString);
    void add(Member member);
    void deleteById(Integer id);
    Member findById(Integer id);

    /**
     * 通过手机号码查询
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);
    void edit(Member member);

    Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountByDate(String date);
    Integer findMemberCountAfterDate(String date);
    Integer findMemberTotalCount();
    //会员数量gender饼图
    List<Map<Integer, String>> getMemberGenderPieReport();

    //会员数量年龄段饼图
    Integer getAgeTotalByAgePiece(@Param("beginBirthDay") Integer beginBirthDay, @Param("endBirthDay") Integer endBirthDay);

}
