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

import java.io.IOException;
import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;

public class AclTest {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                //.authorization("digest","admin:admin".getBytes())
                .connectString(ZkConstant.CONNECT_STR).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();

        //createNode(curatorFramework);
        deleteNode(curatorFramework);
        //setNode(curatorFramework);
        //queryNode(curatorFramework);
    }

    private static void setNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/test/pack","new".getBytes());
    }

    private static void queryNode(CuratorFramework curatorFramework) throws Exception {
        String value = new String(curatorFramework.getData().forPath("/test/pack"));
        System.out.println(value);
    }

    private static void deleteNode(CuratorFramework curatorFramework) throws Exception {
        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat);
        curatorFramework.delete().deletingChildrenIfNeeded()/*.withVersion(stat.getVersion())*/.forPath("/test/pack");
    }

    private static void createNode(CuratorFramework curatorFramework) throws Exception {
        List<ACL> list = new ArrayList<>();
        ACL acl = new ACL(ZooDefs.Perms.READ,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);
        curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(list)
                .forPath("/test/pack","test".getBytes());
    }
}
