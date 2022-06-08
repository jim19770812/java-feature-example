package com.examples.zookeeper;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZkDemo1 {
    public static class QueueSerializerImpl implements QueueSerializer<String> {
        @Override
        public byte[] serialize(String item) {
            return item.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public String deserialize(byte[] bytes) {
            return new String(bytes);
        }
    }
    @SneakyThrows
    public static CuratorFramework createZk(){
        CuratorFramework result=CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .connectionTimeoutMs(3000)
                .sessionTimeoutMs(240000)
                .waitForShutdownTimeoutMs(240000)
                .namespace("ns_test")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        result.start();
        return result;
    }

//    public static Watcher createPrintWatcher(){
//        return new Watcher() {
//            @Override
//            public void process(WatchedEvent event) {
//                System.out.println(event);
//            }
//        };
//    }

    /**
     * 创建节点
     */
    @SneakyThrows
    public static void testCreate(){
        CuratorFramework curator=createZk();
        try{
            String path="/test/t1";
            Stat stat=curator.checkExists()
                    .forPath(path);
            if (stat!=null){
                curator.delete().forPath(path);
            }
            curator.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, "123".getBytes());
        }finally{
            curator.close();
        }
    }

    /**
     * 创建临时节点
     */
    @SneakyThrows
    public static void testCreateTemp(){
        CuratorFramework curator=createZk();
        try{
            String path="/test/ephemeral";
            Stat stat=curator.checkExists()
                    .forPath(path);
            if (stat!=null){
                curator.delete().forPath(path);
            }
            curator.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, "123".getBytes());
        }finally{
            curator.close();
        }
    }

    /**
     * 创建临时节点
     */
    @SneakyThrows
    public static void testCreateEphemeralNode(){
        CuratorFramework curator=createZk();
        try{
            String path="/test/ephemeral";
            Stat stat=curator.checkExists()
                    .forPath(path);
            if (stat!=null){
                curator.delete().forPath(path);
            }
            curator.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, "123".getBytes());
        }finally{
            curator.close();
        }
    }

    /**
     * 分布式队列
     */
    @SneakyThrows
    public static void testQueue1(){
        CuratorFramework curator=createZk();
        try{
            String rootPath="/test/temp";
            String queuePath=rootPath+"/queue1";
            Stat stat=curator.checkExists().forPath(rootPath);
            if (stat==null){
                curator.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(rootPath);
            }
            QueueConsumer<String> queueConsumer=new QueueConsumer<String>() {
                @Override
                @SneakyThrows
                public void consumeMessage(String message) throws Exception {
                    Thread.sleep(100);
                    System.out.println("consumeMessage : "+message);
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    System.out.println(newState);
                }
            };
            DistributedIdQueue<String> queue=QueueBuilder.builder(curator, queueConsumer, new QueueSerializerImpl(), queuePath)
                    .maxItems(3)
                    .buildIdQueue();
            queue.start();
            for(int i=1; i<=100; i++){
                String data="data_"+i;
                queue.put(data, Integer.toString(i), 2, TimeUnit.SECONDS);
                System.out.println("放入"+ data);
//                Thread.sleep(2);
            }
            Thread.sleep(10000);
            queue.close();
        }finally{
            curator.close();
        }
    }


    /**
     * 查询节点的所有孩子
     */
    @SneakyThrows
    public static void testGetChildren(){
        CuratorFramework curator=createZk();
        try{
            String rootPath="/test/temp/queue1";
            Stat stat=curator.checkExists().forPath(rootPath);
            if (stat==null){
                curator.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(rootPath);
            }
            curator.create().withMode(CreateMode.PERSISTENT).forPath(rootPath+"/node-a", "aaaaa".getBytes(StandardCharsets.UTF_8));
            curator.create().withMode(CreateMode.PERSISTENT).forPath(rootPath+"/node-b", "bbbbb".getBytes(StandardCharsets.UTF_8));
            curator.create().withMode(CreateMode.PERSISTENT).forPath(rootPath+"/node-c", "ccccc".getBytes(StandardCharsets.UTF_8));
            List<String> list=curator.getChildren().forPath(rootPath);
            System.out.println(list);
        }finally{
            curator.close();
        }
    }

    public static void main(String[] args){
//        testCreate();
//        testCreateTemp();
//        testQueue1();
        testGetChildren();
    }
}
