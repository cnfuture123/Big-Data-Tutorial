package cn.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class TestDMLAPI {

    public static Configuration conf;
    static{
        //使用 HBaseConfiguration 的单例方法实例化
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.66.132");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "192.168.66.132:16010");
    }

    // 1.insert data into table
    public static void insertDataIntoTable(String tableName, String rowKey, String columnFamily,
                                           String column, String value) throws Exception {
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        // 创建Put对象
        Put put = new Put(Bytes.toBytes(rowKey));
        //向 Put 对象中组装数据
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));

        table.put(put);
        table.close();
        System.out.println("插入数据成功");
    }

    // 2.get data
    public static void getData(String tableName, String rowKey, String columnFamily,
                               String column) throws Exception{
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        // 创建Get对象
        Get get = new Get(Bytes.toBytes(rowKey));

        Result result = table.get(get);
        for(Cell cell : result.rawCells()){
            System.out.println(" 行 键 :" + result.getRow());
            System.out.println(" 列 族 " + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println(" 列 :" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(" 值 :" + Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println("时间戳:" + cell.getTimestamp());
        }
    }

    // 3.delete data
    public static void deleteData(String tableName, String rowKey, String columnFamily,
                                  String column) throws Exception {
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        // 创建Delete对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));

        table.delete(delete);
        table.close();
    }

    public static void main(String[] args) throws Exception{
        // insertDataIntoTable("student", "3", "info", "age", "30");
        getData("student", "1", "", "");
    }

}
