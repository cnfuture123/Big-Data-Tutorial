package cn.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

public class TestDDLAPI {

    public static Configuration conf;
    static{
        //使用 HBaseConfiguration 的单例方法实例化
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.66.132");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "192.168.66.132:16010");
    }

    // 1.determine whether table exists
    public static boolean isTableExist(String tableName) throws Exception {
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        return admin.tableExists(tableName);
    }

    // 2.create table
    public static void createTable(String tableName, String... columnFamily) throws Exception {
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        //创建表属性对象,表名需要转字节
        HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(tableName));
        for (String cf : columnFamily){
            descriptor.addFamily(new HColumnDescriptor(cf));
        }

        admin.createTable(descriptor);
        System.out.println("表" + tableName + "创建成功！");
    }

    // 3.delete table
    public static void deteleTable(String tableName) throws Exception {
        //在 HBase 中管理、访问表需要先创建 HBaseAdmin 对象
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
        System.out.println("表" + tableName + "删除成功！");
    }

    public static void main(String[] args) throws Exception{
        // System.out.println(isTableExist("abc"));
        // createTable("stu1", "info1");
        deteleTable("stu1");
    }

}
