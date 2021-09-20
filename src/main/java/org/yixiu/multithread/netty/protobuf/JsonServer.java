package org.yixiu.multithread.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;

public class JsonServer {

    public static Logger logger = Logger.getLogger(JsonServer.class);

    public static void main(String[] args) {
        new JsonServer(8888).runServer();
    }

    private void runServer() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        //设置Reactor反应器线程组
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(boss,worker);
        //设置NIO类型的channel
        bootstrap.channel(NioServerSocketChannel.class);
        //设置监听端口
        bootstrap.localAddress(port);
        //设置通道参数
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);

        //装配子通道流水线
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new JsonMsgDecoder());
            }
        });

        try {
            //绑定server，调用sync同步直到绑定成功
            ChannelFuture channelFuture = bootstrap.bind().sync();
            logger.info("Server start success on port: "+ channelFuture.channel().localAddress());
            //等待通道关闭任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private final int port;

    public JsonServer(int port) {
        this.port = port;
    }

    static class JsonMsgDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String jsonMsg = (String) msg;
            JsonMsg fromJson = JsonMsg.parseFromJson(jsonMsg);
            logger.info("接收JSON数据包: " + fromJson);
        }
    }
}
