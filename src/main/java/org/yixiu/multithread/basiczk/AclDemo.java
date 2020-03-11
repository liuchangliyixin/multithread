package org.yixiu.multithread.basiczk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

public class AclDemo {
    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConstant.CONNECT_STR)
                .sessionTimeoutMs(5000)
                /**
                 *授权 相当于命令addauth
                 */
                //.authorization("digest","admin:admin".getBytes()).
                /**
                 * 四种重试策略
                 *ExponentialBackoffRetry:重试指定的次数, 且每一次重试之间停顿的时间逐渐增加.
                 *RetryNTimes:指定最大重试次数的重试策略
                 *RetryOneTime:仅重试一次
                 *RetryUntilElapsed:一直重试直到达到规定的时间
                 */
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();

        deleteNode(curatorFramework);

    }

    public static void createNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath("/data/program","test".getBytes());
    }

    public static void createNodeWithAcl(CuratorFramework curatorFramework) throws Exception {List<ACL> list = new ArrayList<ACL>();
        ACL acl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);
        curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
               // .withACL(ZooDefs.Ids.CREATOR_ALL_ACL)
                .withACL(list).forPath("/data/program","test".getBytes());
    }

    public static void deleteNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/data");
    }

    public static void deleteNodeWithVersion(CuratorFramework curatorFramework) throws Exception{
        Stat stat = new Stat();
        String value = new String(curatorFramework.getData().storingStatIn(stat).forPath("/test"));
        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/test");
    }

    public static void queryNode(CuratorFramework curatorFramework) throws Exception {
        List<String> s= curatorFramework.getChildren().forPath("/zookeeper");
        System.out.println(s);
    }

    public static void updateNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/test","test".getBytes());
    }
}
