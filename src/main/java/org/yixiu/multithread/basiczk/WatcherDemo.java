package org.yixiu.multithread.basiczk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;

public class WatcherDemo {
    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConstant.CONNECT_STR).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
        /**
         * PathChildCache  --针对于子节点的创建、删除和更新 触发事件
         * NodeCache  针对当前节点的变化触发事件
         * TreeCache  综合事件
         */
        addListenerWithNode(curatorFramework);
        //阻塞当前线程，使其保持执行状态
        System.in.read();
    }

    private static void addListenerWithNode(CuratorFramework curatorFramework) throws Exception{
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener nodeCacheListener = () -> {
            System.out.println("Receive Node Change:---->");
            System.out.println("Client:"+nodeCache.getClient());
            System.out.println("DataPath:"+nodeCache.getCurrentData().getPath());
            System.out.println("DataValue:"+nodeCache.getCurrentData().getData());

        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

    //实现服务注册中心的时候，可以针对服务做动态感知
    private static void addListenerWithChild(CuratorFramework curatorFramework) throws Exception {
        //这里如果设置false下面的输出DataValue会取不到值
        PathChildrenCache nodeCache=new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener nodeCacheListener= (curatorFramework1, pathChildrenCacheEvent) -> {
            System.out.println("Receive Child Node Change:---->");
            System.out.println("Type:"+pathChildrenCacheEvent.getType());
            System.out.println("DataPath:"+pathChildrenCacheEvent.getData().getPath());
            System.out.println("DataValue:"+pathChildrenCacheEvent.getData().getData());
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
    }

}
