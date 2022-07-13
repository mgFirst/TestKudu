package com.mg;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.mg.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.client.*;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;


import java.util.Arrays;

@Slf4j
public class KuduSparkTest {

    private static final String KUDU_MASTER = "kudu201:7051,kudu202:7051,kudu203:7051";
    private static String tableName = "person";

    private static String appName="TEST";

    private static JavaSparkContext jsc = null;
    private static SparkSession spark = null;

    @Test
    public void kuduSearchTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();        try {
            KuduTable table = client.openTable(tableName);
            List<String> projectColumns = new ArrayList<>(1);
            projectColumns.add("username");
            KuduScanner scanner = client.newScannerBuilder(table)
                    .setProjectedColumnNames(projectColumns)
                    .build();        while (scanner.hasMoreRows()) {
                RowResultIterator results = scanner.nextRows();            while (results.hasNext()) {
                    RowResult result = results.next();
                    System.out.println(result.getString(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {            try {
            client.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

//    @Test
//    public void searchBysparkSql(){
//        String abstractSql = "select * from ".concat(tableName);
////        SparkSession spark = getSession();
////        spark.sql(abstractSql);
////        Dataset<Row> dataset = spark.sql(abstractSql);
////        List<Row> rowList = dataset.collectAsList();
////        for(Row row : rowList){
////            Console.log(row);
////        }
//        SparkSession sparkSession = getSparkSession();
//        List<StructField> fields = Arrays.asList(
//                DataTypes.createStructField("key", DataTypes.StringType, true),
//                DataTypes.createStructField("value", DataTypes.StringType, true));
//        StructType schema = DataTypes.createStructType(fields);
//        Dataset ds =  sparkSession.read().format("org.apache.kudu.spark.kudu").
//                schema(schema).option("kudu.master",KUDU_MASTER).option("kudu.table",tableName).load();
//        sparkSession.sql(abstractSql).show();
//    }


    @Test
    public void queryBySql(){
        String sql = "select * from ".concat(tableName).concat(" order by id desc");
        SparkConf conf = new SparkConf().setAppName("test")
                .setMaster("spark://kudu201:7077")
//                .set("spark.driver.userClassPathFirst", "true")
//                .set("spark.driver.host", "192.168.200.136") // 指定driver的hosts-name
//                .set("spark.driver.port", "63638") // 指定driver的服务端口
//                .set("spark.cores.max", "6").set("spark.executor.cores", "2")
                .set("spark.sql.parquet.writeLegacyFormat", "true");
        conf.set("spark.sql.crossJoin.enabled", "true");
        SparkContext sparkContext = new SparkContext(conf);
        SparkSession sparkSession = SparkSession.builder().sparkContext(sparkContext).getOrCreate();
        sparkSession.read().format("kudu")
                .option("kudu.master", KUDU_MASTER)
                .option("kudu.table", tableName).load().createOrReplaceTempView(tableName);
        Dataset<Row> namesDF = sparkSession.sql(sql);
//        User.UserBuilder userBuilder =User.builder();
//        List<User> userList = new ArrayList<>();
//        namesDF.show();
        namesDF.foreach(row -> {
            //userList.add(userBuilder.id(row.getInt(0)).username(row.getString(1)).password(row.getString(2)).nickname(row.getString(3)).birthday(row.getDate(4)).build());
            log.info("------ row:{}",row);

        });
//        userList.forEach(user -> {
//            System.out.println(user.getNickname());
//        });
        sparkSession.stop();
        sparkSession.close();
        //System.out.println(123);
    }
    public SparkSession getSparkSession(){
        SparkConf conf = new SparkConf().setAppName("test")
                .setMaster("spark://kudu203:7077")
//                .set("spark.driver.userClassPathFirst", "true")
//                .set("spark.driver.host", "192.168.200.136") // 指定driver的hosts-name
//                .set("spark.driver.port", "63638") // 指定driver的服务端口
//                .set("spark.cores.max", "6").set("spark.executor.cores", "2")
                .set("spark.sql.parquet.writeLegacyFormat", "true");

        conf.set("spark.sql.crossJoin.enabled", "true");
        SparkContext sparkContext = new SparkContext(conf);
        SparkSession sparkSession = SparkSession.builder().sparkContext(sparkContext).getOrCreate();
        return sparkSession;
    }

    private static void initSpark() {
        if (jsc == null || spark == null) {

            SparkConf  sparkConf = new SparkConf();
            sparkConf.set("spark.driver.allowMultipleContexts", "true");
            sparkConf.set("spark.eventLog.enabled", "true");
            sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
            //sparkConf.set("spark.hadoop.validateOutputSpecs", "false");
            sparkConf.set("hive.mapred.supports.subdirectories", "true");
            sparkConf.set("mapreduce.input.fileinputformat.input.dir.recursive", "true");

            spark = SparkSession.builder().appName(appName).master("spark://192.168.200.136:7077").appName("TEST").config(sparkConf).getOrCreate();
            jsc = new JavaSparkContext(spark.sparkContext());
        }

    }

    public static JavaSparkContext getJsc() {
        if (jsc == null) {
            initSpark();
        }
        return jsc;
    }

    public static SparkSession getSession() {
        if (spark == null ) {
            initSpark();
        }
        return spark;

    }
}
