package com.mg.util;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * InfluxDB查询语句工具类
 *
 * @author WANGHT ON 2021年8月13日 17:29:51
 */
@Slf4j
public class QueryUtils {

    // 查询关键字(SELECT)
    public static final String SELECT = "select ";

    // 查询关键字(DELETE)
    public static final String DELETE = "delete ";

    // 查询关键字(DROP)
    public static final String DROP = "drop ";

    // 查询关键字(MEASUREMENT)
    public static final String MEASUREMENT = " measurement ";

    // 查询关键字(COUNT)
    public static final String COUNT = "count";

    // 查询关键字(SUM)
    public static final String SUM = "SUM";

    // 查询关键字(MEAN)
    public static final String MEAN = "MEAN";

    // 查询关键字(FROM)
    public static final String FROM = " from ";

    // 查询关键字(WHERE)
    public static final String WHERE = " where ";

    // 查询关键字(LIMIT)
    public static final String LIMIT = " limit ";

    // 查询关键字(OFFSET)
    public static final String OFFSET = " offset ";

    // 查询关键字(GROUP_BY)
    public static final String GROUP_BY = " group by ";

    // 查询关键字(ORDER_BY)
    public static final String ORDER_BY = " order by ";

    // 查询关键字(DESC)
    public static final String DESC = " DESC ";

    // 查询关键字(OR)
    public static final String OR = " or ";

    // 查询关键字(AND)
    public static final String AND = " and ";

    // 查询关键字(LIKE)
    public static final String LIKE = " =~ ";

    // 上海时区
    public static final String TIME_ZONE_SHANGHAI = " TZ('Asia/Shanghai')";

    // 查询表Breaker
    public static final String MEASUREMENT_BREAKER = "Breaker";

    // 查询表Disconnector
    public static final String MEASUREMENT_DISCONNECTOR = "Disconnector";

    // 查询表GroundDisconnector
    public static final String MEASUREMENT_GROUND_DISCONNECTOR = "GroundDisconnector";

    /** 默认DEFAULT_MEASUREMENT */
    public static final String DEFAULT_MEASUREMENT = MEASUREMENT_BREAKER.concat(Constants.COMMA_VALUE)
                    .concat(MEASUREMENT_DISCONNECTOR)
                    .concat(Constants.COMMA_VALUE)
                    .concat(MEASUREMENT_GROUND_DISCONNECTOR);

    public static String queryRectifierInverterPdc(String dataTime){
        StringBuilder builder = new StringBuilder(SELECT);
        // columns
        queryColumn(builder, "stationId,sum".split(","));
        // from
        builder.append(FROM);
        // measurement
        queryMeasurement(builder, "rectifierInverter_pdc");
        // where
        builder.append(WHERE);
        // dataTime
        queryColumn(builder, "dataTime");
        builder.append(Constants.SIGN_VALUE).append(Constants.SPACE_VALUE);
        queryValue(builder, dataTime);
        // 返回
        return builder.toString();
    }

    private static void queryValue(StringBuilder builder, Object value){
        builder.append(Constants.SINGLE_QUOTATION_MARKS)
                .append(value)
                .append(Constants.SINGLE_QUOTATION_MARKS)
                .append(Constants.SPACE_VALUE);
    }

    private static String getConnectValue(String mark, String split, String[] array, String time){
        StringBuilder builder = new StringBuilder();
        if(Objects.nonNull(array)){
            int index = 0;
            for(String value : array){
                // 拼接数据
                builder.append(mark).append(value);
                // 时间戳
//                if(StringUtils.isNotBlank(time)){
//                    // 每2日的数据存入一张表-只使用单号日期作为表后缀
//                    builder.append(getTimeSuffix(time));
//                }
                // 拼接标记
                builder.append(mark);
                // 拼接","
                if(++index < array.length){
                    builder.append(split);
                }
                // 拼接" "
                builder.append(Constants.SPACE_VALUE);
            }
        }
        return builder.toString();
    }

    private static void queryMeasurement(StringBuilder builder, String date, String[] columns){
        builder.append(getConnectValue(
                Constants.DOUBLE_QUOTATION_MARKS, Constants.COMMA_VALUE, columns, date));
    }
    private static void queryMeasurement(StringBuilder builder, String...columns){
        queryMeasurement(builder, DateUtils.today(Constants.DATE_FORMAT_VALUE), columns);
    }

    private static void queryColumn(StringBuilder builder, String...columns){
        builder.append(getConnectValue(Constants.DOUBLE_QUOTATION_MARKS, Constants.COMMA_VALUE, columns, null));
    }

    /**
     * 构造日期
     *
     * @param time
     * @return
     */
    private static String getTimeSuffix(String time){
        int timeValue = Integer.parseInt(time);
        if(timeValue % 2 == 0){
            return String.valueOf(timeValue - 1);
        }
        return time;
    }

    /**
     * 转换为influxDB时间戳
     *
     * @param date          日期
     * @param timestamp     时间戳
     * @return
     */
    private static String convertInfluxTime(String date, String timestamp){
        return date.concat(timestamp);
    }
}
