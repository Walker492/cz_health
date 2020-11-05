package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/1
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员数量拆线图
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        // 产生12个月的数据, 2020-01
        List<String> months = new ArrayList<String>();
        // 使用日历
        Calendar car = Calendar.getInstance();
        // 过去一年, 年-1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        car.add(Calendar.YEAR, -1);
        // 遍历12次，依次加1个月
        for (int i = 0; i < 12; i++) {
            // +1个月
            car.add(Calendar.MONTH,1);
            months.add(sdf.format(car.getTime()));
        }

        // 调用服务去查询12个月的数据
        List<Integer> memberCount = memberService.getMemberReport(months);
        // 构建返回的数据
        /**
         * {
         *     flag
         *     message:
         *     data:{
         *         months:
         *         memberCount:
         *     }
         * }
         */
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    /**
     * 套餐预约占比
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // 调用服务查询套餐预约占比, map {value, name} name=套餐名称
        List<Map<String,Object>> list = setmealService.getSetmealReport();
        List<String> setmealNames = list.stream().map(m -> (String)m.get("name")).collect(Collectors.toList());
        //List<String> setmealNames = new ArrayList<String>();
        //for (Map<String, Object> map : list) {
        //    // {value, name: 套餐名称}
        //    String setmealName = (String) map.get("name");
        //    setmealNames.add(setmealName);
        //}
        /**
         * {
         *     flag
         *     message
         *     data:{
         *         setmealNames: ['名称'...],
         *         setmealCount: [{value,name}] list
         *     }
         * }
         */
        // 构建前端需要的数据格式
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        resultMap.put("setmealNames", setmealNames);
        resultMap.put("setmealCount", list);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    /**
     * 获取运营数据统计
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        // 调用服务查询
        Map<String,Object> reportData = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportData);
    }

    /**
     * 导出excel
     */
    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        //- 获取模板所在
        String templatePath = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        //- 获取报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //- 创建Workbook传模板所在路径
        try(Workbook wk = new XSSFWorkbook(templatePath)) {
            //- 获取工作表
            Sheet sht = wk.getSheetAt(0);
            //- 获取行，单元格，设置相应的数据
            sht.getRow(2).getCell(5).setCellValue((String) reportData.get("reportDate"));
            //  ================= 会员数量 ================
            sht.getRow(4).getCell(5).setCellValue((Integer) reportData.get("todayNewMember"));
            sht.getRow(4).getCell(7).setCellValue((Integer) reportData.get("totalMember"));
            sht.getRow(5).getCell(5).setCellValue((Integer) reportData.get("thisWeekNewMember"));
            sht.getRow(5).getCell(7).setCellValue((Integer) reportData.get("thisMonthNewMember"));

            // =================== 预约到诊数量 =====================
            sht.getRow(7).getCell(5).setCellValue((Integer) reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer) reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer) reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer) reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer) reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer) reportData.get("thisMonthVisitsNumber"));

            // ================== 热门套餐，遍历输出填值 ================
            List<Map<String,Object>> hotSetmealList = (List<Map<String,Object>>)reportData.get("hotSetmeal");
            int rowIndex = 12;
            for (Map<String, Object> setmeal : hotSetmealList) {
                sht.getRow(rowIndex).getCell(4).setCellValue(((String) setmeal.get("name")));
                //  - 数量的类型为Long
                sht.getRow(rowIndex).getCell(5).setCellValue((Long)setmeal.get("setmeal_count"));
                //  - 占比的值的类型为bigdecimal，转成dubbo
                BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                sht.getRow(rowIndex).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(rowIndex).getCell(7).setCellValue((String)setmeal.get("remark"));
                rowIndex++;
            }
            //
            //- 设置响应体内容的格式application/vnd.ms-excel
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营数据统计.xlsx";
            byte[] bytes = filename.getBytes();
            filename = new String(bytes, "ISO-8859-1");
            //- 设置响应头信息，告诉浏览器下载的文件名叫什么 Content-Disposition, attachment;filename=文件名
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            //- Workbook.write响应输出流
            wk.write(res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
