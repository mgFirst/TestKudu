package com.mg;

import cn.hutool.core.lang.Console;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.apache.kudu.util.DateUtil;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class KuduTest {

    private KuduClient kuduClient;

    private String kuduMaster;

    private String tableName;

    @Before
    public void init(){
        tableName = "users";
        //创建kudu连接
        kuduMaster = "kuduserver:7051";
        KuduClient.KuduClientBuilder kuduClientBuilder = new KuduClient.KuduClientBuilder(kuduMaster);
        kuduClientBuilder.defaultAdminOperationTimeoutMs(10000);
        kuduClient = kuduClientBuilder.build();

    }

    /**
     * 创建数据库
     * @throws KuduException
     */
    @Test
    public void createTable() throws KuduException {

        if(kuduClient.tableExists(tableName)){
            Console.log("数据库表已存在!");
        }else{
            // 列信息
            ArrayList<ColumnSchema> columnSchemas = new ArrayList<>();
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.INT32).key(true).nullable(false).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("username", Type.STRING).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("password", Type.STRING).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("nickname", Type.STRING).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("birthday", Type.DATE).build());
            Schema schema = new Schema(columnSchemas);

            //option
            CreateTableOptions createTableOptions = new CreateTableOptions();

            ArrayList<String> columnsList = new ArrayList<String>();
            columnsList.add("id");
            createTableOptions.addHashPartitions(columnsList,2);//hash类型  2个分区

            createTableOptions.setNumReplicas(1);//1个副本

            kuduClient.createTable(tableName,schema,createTableOptions);
        }
    }

    /**
     * 增
     * @throws KuduException
     */
    @Test
    public void insert() throws KuduException {
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        KuduTable kuduTable = kuduClient.openTable(tableName);

        //插入10条
        for(int i=1;i<=10;i++){
            Insert insert = kuduTable.newInsert();
            PartialRow row = insert.getRow();
            row.addInt("id",i);
            row.addString("username","username"+i);
            row.addString("password","password"+i);
            row.addString("nickname","nickname"+i);
            Date date = new Date(System.currentTimeMillis());
            row.addDate("birthday", date);
            kuduSession.apply(insert);
        }

        //关闭
        kuduSession.close();
        kuduClient.close();

    }

    /**
     * 删
     * @throws KuduException
     */
    @Test
    public void delete() throws KuduException {
        //删除表的数据需要一个 kuduSession 对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        //需要使用 kuduTable 来构建 Operation 的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(tableName);
        Delete delete = kuduTable.newDelete();
        PartialRow row = delete.getRow();
        row.addInt("id",1);

        kuduSession.apply(delete);//最后实现执行数据的删除操作

        //关闭
        kuduSession.flush();
        kuduSession.close();
    }


    /**
     * 删表
     * @throws KuduException
     */
    @Test
    public void dropTable() throws KuduException {
        if(kuduClient.tableExists(tableName)){
            kuduClient.deleteTable(tableName);
        }
    }

    /**
     * 改
     * @throws KuduException
     */
    @Test
    public void update() throws KuduException {
        //修改表的数据需要一个 kuduSession 对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        //需要使用 kuduTable 来构建 Operation 的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(tableName);
        //Update update = kuduTable.newUpdate();
        Upsert upsert = kuduTable.newUpsert(); //如果 id 存在就表示修改，不存在就新增
        PartialRow row = upsert.getRow();
        row.addInt("id",1);
        row.addString("username","username"+System.currentTimeMillis());
        row.addString("password","password"+System.currentTimeMillis());
        row.addString("nickname","nickname"+System.currentTimeMillis());
        Date date = new Date(System.currentTimeMillis());
        row.addDate("birthday", date);
        kuduSession.apply(upsert);//最后实现执行数据的修改操作

        //关闭
        kuduSession.flush();
        kuduSession.close();

        kuduClient.close();
    }

    /**
     * 查
     * @throws KuduException
     */
    @Test
    public void query() throws KuduException {
        //构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder = kuduClient.newScannerBuilder(kuduClient.openTable(tableName));
        ArrayList<String> columnsList = new ArrayList<String>();
        columnsList.add("id");
        columnsList.add("username");
        columnsList.add("password");
        columnsList.add("nickname");
        columnsList.add("birthday");
        kuduScannerBuilder.setProjectedColumnNames(columnsList);
        //返回结果集
        KuduScanner kuduScanner = kuduScannerBuilder.build();
        //遍历
        while (kuduScanner.hasMoreRows()){
            RowResultIterator rowResults = kuduScanner.nextRows();
            while (rowResults.hasNext()){
                RowResult row = rowResults.next();
                Console.log(row.getInt("id"));
                Console.log(row.getString("username"));
                Console.log(row.getString("password"));
                Console.log(row.getString("nickname"));
                Console.log(row.getDate("birthday"));
            }
        }
        //关闭
        kuduClient.close();
    }



}
