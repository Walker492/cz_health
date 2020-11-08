package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/29
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 查询每个月的会员总数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> list = new ArrayList<>(12);
        // 2020-01
        months.forEach(month ->{
            // 查询到每个月最后一天为止的会员总数量
            list.add(memberDao.findMemberCountBeforeDate(month+"-31"));
        });
        return list;
    }

    /**
     * 会员数量gender饼图
     * @return
     */
    @Override
    public List<Map<Integer, String>> getMemberGenderPieReport() {

        return memberDao.getMemberGenderPieReport();
    }

    /**
     * 会员数量年龄段饼图
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getMemberAgePieReport(List<String> agePieces) {


        List<Map<String, Object>> list = new ArrayList<>();

        if (null != agePieces) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            //获取当前时间 只要年份
            Date currentDate = new Date();
            String currentYear = sdf.format(currentDate);
            for (String agePiece : agePieces) {

                String[] split = agePiece.split("-");

                Integer year = Integer.valueOf(currentYear);

                Integer beginAge = Integer.valueOf(split[1]);
                Integer endAge = Integer.valueOf(split[0]);

                int beginBirthDay = year - beginAge;
                int endBirthDay = year - endAge;
                Integer ageTotalByAgePiece = memberDao.getAgeTotalByAgePiece(beginBirthDay, endBirthDay);
                //封装返回数据
                Map<String, Object> map = new HashMap<>();
                map.put("value", ageTotalByAgePiece);
                map.put("name", agePiece);
                list.add(map);
            }
        }
        return list;
    }
}
