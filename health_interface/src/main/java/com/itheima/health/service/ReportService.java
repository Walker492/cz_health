package com.itheima.health.service;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/1
 */
public interface ReportService {

    /**
     * 获取运营数据统计
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
