    package com.rose.service.impl;

    import com.rose.dto.GoodsSalesDTO;
    import com.rose.entity.Orders;
    import com.rose.mapper.OrderDetailMapper;
    import com.rose.mapper.OrderMapper;
    import com.rose.mapper.UserMapper;
    import com.rose.service.ReportService;
    import com.rose.service.WorkspaceService;
    import com.rose.vo.*;
    import org.apache.commons.lang.StringUtils;
    import org.apache.poi.xssf.usermodel.XSSFRow;
    import org.apache.poi.xssf.usermodel.XSSFSheet;
    import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import javax.servlet.ServletOutputStream;
    import javax.servlet.http.HttpServletResponse;
    import java.io.*;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    public class ReportServiceImpl implements ReportService {

        @Autowired
        private OrderMapper orderMapper;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        OrderDetailMapper orderDetailMapper;
        @Autowired
        WorkspaceService workspaceService;
        /**
         * 统计指定时间区间内营业额数据
         *
         * @param begin
         * @param end
         * @return
         */
        public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

            //当前集合用于存放从begin到end每天的日期
            List<LocalDate> dateList = new ArrayList<>();

            dateList.add(begin);
            //封装日期横坐标
            while (!begin.equals(end)) {
                 begin = begin.plusDays(1);
                dateList.add(begin);
            }

            //存放每天的营业额
            List<Double> turnoverList = new ArrayList<>();
            //获取每天的营业额
            for (LocalDate localDate : dateList) {
                //查询date日期的营业额,即订单状态为已完成的订单价格总和
                LocalDateTime dayBegin = LocalDateTime.of(localDate, LocalTime.MIN);
                LocalDateTime dayEnd = LocalDateTime.of(localDate, LocalTime.MAX);

                Map<Object, Object> map = new HashMap<>();
                map.put("dayBegin", dayBegin);
                map.put("dayEnd", dayEnd);
                map.put("status", Orders.COMPLETED);
                //select sum(amount) from orders where order_time> ? and order_time<? and status=5  ;
                Double turnover = orderMapper.sumTurnoverByMap(map);
                turnover = turnover == null ? 0.0 : turnover;
                turnoverList.add(turnover);
            }
            return TurnoverReportVO
                    .builder()
                    .dateList(StringUtils.join(dateList, ","))
                    .turnoverList(StringUtils.join(turnoverList, ","))
                    .build();
        }

        /**
         * 用户统计
         * @param begin
         * @param end
         * @return
         */
        public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
            //封装日期横坐标
            List<LocalDate> dateList =new ArrayList<>();
            dateList.add(begin);
            //封装日期横坐标
            while (!begin.equals(end)) {
                begin = begin.plusDays(1);
                dateList.add(begin);
            }
            //存放每天用户量
            List<Integer> userAmountList=new ArrayList<>();
            //存放每天用户增量
            List<Integer> userIncrease=new ArrayList<>();
            for (LocalDate date : dateList) {

                LocalDateTime dayBegin = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

                Map<Object, Object> map=new HashMap<>();
                //获取每天用户量
                map.put("dayEnd",dayEnd);
                Integer userAmount= userMapper.getUserAmountByMap(map);

                //获取每天用户增量
                map.put("dayBegin",dayBegin);
                Integer growth= userMapper.getUserAmountByMap(map);

                userAmountList.add(userAmount);
                userIncrease.add(growth);
            }
            return UserReportVO
                    .builder()
                    .dateList(StringUtils.join(dateList,","))
                    .newUserList(StringUtils.join(userIncrease,","))
                    .totalUserList(StringUtils.join(userAmountList,","))
                    .build();
        }

        /**
         * 订单统计
         * @param begin
         * @param end
         * @return
         */
        public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
            //封装日期横坐标
            List<LocalDate> dateList =new ArrayList<>();
            dateList.add(begin);
            //封装日期横坐标
            while (!begin.equals(end)) {
                begin = begin.plusDays(1);
                dateList.add(begin);
            }

            //存放每天有效订单数
            List<Integer> orderCompletedAmountList=new ArrayList<>();
            //存放每天订单总数
            List<Integer> totalOrderAmountList=new ArrayList<>();
            for (LocalDate date : dateList) {

                LocalDateTime dayBegin = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

                Integer totalOrderAmount = getOrderAmountByDate(dayBegin,dayEnd,null);

                //获取每天有效订单数
                Integer orderCompletedAmount = getOrderAmountByDate(dayBegin,dayEnd,Orders.COMPLETED);

                totalOrderAmountList.add(totalOrderAmount);
                orderCompletedAmountList.add(orderCompletedAmount);
            }
            //求订单总数,已完成订单总数,订单完成率
            Integer allOrdersAmount = totalOrderAmountList.stream().reduce(Integer::sum).get();
            Integer allValidOrdersAmount = orderCompletedAmountList.stream().reduce(Integer::sum).get();
            double orderCompletionRate=0.0;
            if (allOrdersAmount!=0){
                orderCompletionRate= allValidOrdersAmount.doubleValue()/allOrdersAmount;
            }

            return OrderReportVO
                    .builder()
                    .dateList(StringUtils.join(dateList,','))
                    .validOrderCountList(StringUtils.join(orderCompletedAmountList,','))
                    .orderCountList(StringUtils.join(totalOrderAmountList,','))
                    .totalOrderCount(allOrdersAmount)
                    .validOrderCount(allValidOrdersAmount)
                    .orderCompletionRate(orderCompletionRate)
                    .build();
        }


        /**
         * 销量排名
         * @param begin
         * @param end
         * @return
         */
        public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
            //存放菜品名
//            List<String> nameList=new ArrayList<>();
            //存放销量
//            List<Integer> salesList=new ArrayList<>();

            LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
            //获取销量前10的菜品名

            Map<Object, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            List<GoodsSalesDTO> top10=  orderDetailMapper.getSaLesTop10(map);
//            for (GoodsSalesDTO goodsSalesDTO : top10) {
//                nameList.add(goodsSalesDTO.getName());
//                salesList.add(goodsSalesDTO.getNumber());
//            }

            if (top10 == null || top10.isEmpty()) {
                System.out.println("No sales data found for the given date range.");
                return SalesTop10ReportVO.builder().nameList("").numberList("").build();
            }else {
                List<String> nameList = top10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
                List<Integer> salesList = top10.stream().map(GoodsSalesDTO::getNum).collect(Collectors.toList());
                return SalesTop10ReportVO
                        .builder()
                        .nameList(StringUtils.join(nameList,","))
                        .numberList(StringUtils.join(salesList,","))
                        .build();
            }
            }

        /**
         *导出运营数据Excel报表
         * @param response
         */
        public void  exportBussinessData(HttpServletResponse response) {
            //1.查询数据库,获取运营数据(最近30天)
            LocalDate dateBegin = LocalDate.now().minusDays(30);
            LocalDate dateEnd = LocalDate.now().minusDays(1);
            LocalDateTime begin = LocalDateTime.of(dateBegin, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(dateEnd, LocalTime.MAX);
            BusinessDataVO businessData = workspaceService.getBusinessData(begin, end);
            //2.利用POI将数据写入Excel文件
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
            try {
                //基于模板文件创建新的Excel文件
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                //填充时间
                XSSFSheet sheet = workbook.getSheet("sheet1");
                sheet.getRow(1).getCell(1).setCellValue(begin+"~"+end);

                //
                XSSFRow row3 = sheet.getRow(3);
                row3.getCell(2).setCellValue(businessData.getTurnover());
                row3.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row3.getCell(6).setCellValue(businessData.getNewUsers());

                //
                XSSFRow row4 = sheet.getRow(4);
                row4.getCell(2).setCellValue(businessData.getValidOrderCount());
                row4.getCell(4).setCellValue(businessData.getUnitPrice());

                //
                for (int i = 0; i < 30; i++) {
                    LocalDate date = dateBegin.plusDays(i);
                    BusinessDataVO dataOfDay = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                    XSSFRow row = sheet.getRow(7 + i);
                    row.getCell(1).setCellValue(date.toString());

                    row.getCell(2).setCellValue(dataOfDay.getTurnover());
                    row.getCell(3).setCellValue(dataOfDay.getValidOrderCount());
                    row.getCell(4).setCellValue(dataOfDay.getOrderCompletionRate());
                    row.getCell(5).setCellValue(dataOfDay.getUnitPrice());
                    row.getCell(6).setCellValue(dataOfDay.getNewUsers());
                }
                //3.通过输出流将Excel文件下载到客户端
                ServletOutputStream outputStream = response.getOutputStream();
                workbook.write(outputStream);

                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        //
        private Integer getOrderAmountByDate(LocalDateTime dayBegin, LocalDateTime dayEnd ,Integer orderStatus) {
            //获取每天订单总数
            Map<Object, Object> map=new HashMap<>();
            map.put("dayEnd", dayEnd);
            map.put("dayBegin", dayBegin);
            map.put("orderStatus",orderStatus);
            return orderMapper.getOrderAmountByMap(map);
        }
    }
