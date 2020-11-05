package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.Utils.DateUtils;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/1
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 获取运营数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        //reportDate
        String reportDate = today;
        // 本周星期一
        String monday = sdf.format(DateUtils.getThisWeekMonday());
        // 本周星期天
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());


        // ================  会员数量统计 ===================
        //todayNewMember 本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //totalMember 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        // =================== 预约到诊数量 =====================
        //todayOrderNumber 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //todayVisitsNumber 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //thisWeekOrderNumber 本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //thisWeekVisitsNumber 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        // 热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        Map<String, Object> reportData = new HashMap<String, Object>(11);
        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }
}
