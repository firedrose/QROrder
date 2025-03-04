package com.rose.controller.admin;

import com.rose.result.Result;
import com.rose.service.ReportService;
import com.rose.vo.OrderReportVO;
import com.rose.vo.SalesTop10ReportVO;
import com.rose.vo.TurnoverReportVO;
import com.rose.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Api(tags="数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计:{},{}",begin,end);
        return Result.success(reportService.getTurnoverStatistics(begin,end));
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @RequestMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计:{},{}",begin,end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @RequestMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计:{},{}",begin,end);
        return Result.success(reportService.getOrderStatistics(begin,end));
    }

    /**
     *导出运营数据Excel报表
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据Excel报表")
    public void export(HttpServletResponse response){
        log.info("导出运营数据");
        reportService.exportBussinessData(response);
    }

    /**
     * 销量排名
     * @param begin
     * @param end
     * @return
     */
    @RequestMapping("/top10")
    @ApiOperation("销量排名")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("销量排名:{},{}",begin,end);
        return Result.success(reportService.getSalesTop10(begin,end));

    }
}
