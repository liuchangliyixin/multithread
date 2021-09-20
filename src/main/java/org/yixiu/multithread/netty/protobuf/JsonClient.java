package org.yixiu.multithread.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

public class JsonClient {

    public static Logger logger = Logger.getLogger(JsonClient.class);

    private final String ip;
    private final int port;

    public JsonClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        new JsonClient("127.0.0.1",8888).startClient();
    }

    private void startClient() {
        Bootstrap bootstrap = new Bootstrap();

        EventLoopGroup boss = new NioEventLoopGroup();

        try {
            bootstrap.group(boss);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(ip,port);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                }
            });

            ChannelFuture connect = bootstrap.connect();
            connect.addListener((ChannelFutureListener) channelFuture -> {
                if(channelFuture.isSuccess()){
                    logger.info("Client connect success!");
                }else{
                    logger.info("Client connect failure!");
                }
            });
            //阻塞直到连接完成
            connect.sync();
            Channel channel = connect.channel();
            //发送json字符串
            for(int i=0;i<100;i++){
                JsonMsg jsonMsg = buildMsg(i, "发送报文"+ i +"--何须浅碧深红色，自是花中第一流。");
                channel.writeAndFlush(jsonMsg.convertToJson());
                logger.info(jsonMsg.convertToJson());
            }
            channel.flush();
            //等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            boss.shutdownGracefully();
        }

    }

    private JsonMsg buildMsg(int id,String content){
        JsonMsg msg = new JsonMsg();
        msg.setId(id);
        msg.setContent(content);
        return msg;
    }
}
