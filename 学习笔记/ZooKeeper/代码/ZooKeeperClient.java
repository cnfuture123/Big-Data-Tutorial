package com.cn;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZooKeeperClient {

    // define configuration parameters
    private String connectionString = "192.168.66.132:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;

    // 1.initialize client
    // @Test
    public void initZK() throws Exception {
        zkClient = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "--" + event.getPath());
//                try{
//
//                } catch (){
//
//                }
            }
        });
    }

    // 2.create node
    // @Test
    public void createNode() throws Exception {
        // 参数1：要创建的节点的路径； 参数2：节点数据 ； 参数3：节点权限 ；参数4：节点的类型
        String createdNode = zkClient.create("/cn", "create node cn".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // 3.get child nodes
    // @Test
    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", false);
        for (String child : children){
            System.out.println(child);
        }
    }

    // 4.determine node is existed or not
    // @Test
    public void isNodeExisted() throws  Exception {
        Stat status = zkClient.exists("/cn", false);
        System.out.println(status == null ? "not exist" : "exist");
    }



}