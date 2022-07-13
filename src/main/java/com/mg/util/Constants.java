package com.mg.util;

import java.io.Serializable;

/**
 * 常量类
 *
 */
public class Constants implements Serializable {

    /** SM4私钥(SM2加密后) */
    public static final String SM4_K_BY_SM2 = "04f049ba0ba7506e3ae4d2cc581054088c544872cc95aff82dad1f57c22b5f0964e8b057af104c7be1205e75f777866d52c273a725ca38cb4eab5aaa943a12ee61c158c625f657a6f30383f4518dc7c4a3c4fdcf62be85601e626624f66c45e2e6e533be22f49e49c97ce39f0b23bc2147";

    /** 时序数据库密码密钥 */
    public static final String INFLUX_DB_KEY = "75F2C63C5EA6C20FD3FA4180E3803A34";

    /** 星号(*) */
    public static final String ASTERISK_VALUE = "*";

    /** 逗号(,) */
    public static final String COMMA_VALUE = ",";

    /** 下划线(_) */
    public static final String UNDER_LINE = "_";

    /** 单引号(') */
    public static final String SINGLE_QUOTATION_MARKS = "'";

    /** 单引号(') */
    public static final String DOUBLE_QUOTATION_MARKS = "\"";

    /** 空格 */
    public static final String SPACE_VALUE = " ";

    /** 点(.) */
    public static final String SPOT_VALUE = ".";

    /** 等号(=) */
    public static final String SIGN_VALUE = "=";

    /** 不等号(!=) */
    public static final String NOT_SIGN_VALUE = "!=";

    /** 大于号(>) */
    public static final String GT_VALUE = ">";

    /** 小于号(<) */
    public static final String LT_VALUE = "<";

    /** 小括号左(\() */
    public static final String L_PARENTHESES = "(";

    /** 小括号右(\)) */
    public static final String R_PARENTHESES = ")";

    /** 问号(?) */
    public static final String  QUESTION_MARK_VALUE = "?";

    /** 斜线(/) */
    public static final String SLASH_VALUE = "/";

    /** 空字符串 */
    public static final String EMPTY_VALUE = "";

    /** 储值卡头编码 */
    public static final String CARD_TITLE = "YNR";

    /** 字符串0 */
    public static final String ZERO_VALUE = "0";

    /** 字符串00 */
    public static final String DOUBLE_ZERO_VALUE = "00";

    /** 数字9 */
    public static final int NUMBER_NINE = 9;

    /** 数字99 */
    public static final int NUMBER_NINETY_NINE = 99;

    /** 时序数据库时间补位 */
    public static final String INFLUX_DB_TIME_COMPLEMENT = "T00:00:00Z";

    /** 时序数据库时间戳(起) */
    public static final String INFLUX_DB_START_TIMESTAMP = " 00:00:00";

    /** 时序数据库时间戳(止) */
    public static final String INFLUX_DB_END_TIMESTAMP = " 23:59:59";

    /** 同步分页数据量 */
    public static final int SYNC_PAGE_SIZE = 1000;

    /** 日期格式-时间 */
    public static final String DATE_FORMAT_TEMP = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式-时间 */
    public static final String DATE_FORMAT_TEMP_SSS = "yyyy-MM-DDHH:mm:ss.SSS";

    /** 日期格式-时间(分钟) */
    public static final String DATE_FORMAT_TEMP_MIN = "yyyy-MM-dd HH:mm";

    /** 日期格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /** 日期格式值 */
    public static final String DATE_FORMAT_VALUE = "yyyyMMdd";

    /** 时序 */
    public static final String INFLUX_DATE_FORMAT_VALUE = "yyyy-MM-ddTHH:mm:ssZ";

    /** 日期格式值 */
    public static final String TIME_FORMAT_VALUE = "HHmmss";

    /** redis实现类BEAN名称 */
    public static final String REDISTEMPLATE_BEAN_NAME = "redisTemplate";

    /** redis缓存Key */
    public static final String INFLUX_DATA_REDIS_KEY = "influxDB_redis_key";

    /** redis缓存Key(上次) */
    public static final String LAST_INFLUX_DATA_REDIS_KEY = "last_influxDB_redis_key";

    /** redis缓存时间(30分钟)-暂时设置为24小时 方便调用 */
    public static final int INFLUX_DATA_REDIS_TIME = 60 * 64 * 24;

    /** 文件大小单位(M) */
    public static final long FILE_SIZE_UNIT_M = 1024 * 1024;

    /** 告警数量key */
    public static final String ALARM_COUNT_KEY = "alarm_count_value";
    /** 告警数量key（new） */
    public static final String ALARM_EVENT_COUNT_KEY = "alarm_event_count_value";

    /** 阀值缓存key */
    public static final String THRESHOLD_SETTING_KEY = "threshold_setting_list";

    /** 监控关键字key */
    public static final String EVENT_MONITORING_WORD_KEY = "event_monitoring_word_list";

    /** 同步文件列表key */
    public static final String SYNC_DATA_FILE_KEY = "sync_data_file_list";

    /** 对端站点列表key */
    public static final String OPPOSITE_END_STATION_KEY = "opposite_end_station_list";

    public static final String SECRET_SALT = "ewms";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    public static final String UPLOAD_PATH = "/ewms/file/upload/";
}
