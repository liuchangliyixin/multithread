package org.yixiu.multithread.netty.protocol.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.apache.log4j.Logger;
import org.yixiu.multithread.netty.protocol.MsgProtos;

public class ProtobufClient {

    public static Logger logger = Logger.getLogger(ProtobufClient.class);

    private final String ip;
    private final int port;

    public ProtobufClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        new ProtobufClient("127.0.0.1",8888).runClient();
    }

    private void runClient() {
        Bootstrap bootstrap = new Bootstrap();

        EventLoopGroup group = new NioEventLoopGroup();

        try{
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(ip,port);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    socketChannel.pipeline().addLast(new ProtobufEncoder());
                }
            });

            ChannelFuture connect = bootstrap.connect();
            connect.addListener(future -> {
               if(future.isSuccess()){
                   logger.info("客户端连接成功！");
               }else{
                   logger.info("客户端连接失败！");
               }
            });
            connect.sync();
            Channel channel = connect.channel();
            for(int i=0;i<100;i++){
                MsgProtos.Msg msg = build(i, "何须浅碧深红色，自是花中第一流。");
                channel.writeAndFlush(msg);
                logger.info("发送报文--->" + msg);
            }
            channel.flush();

            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }

    }

    private MsgProtos.Msg build(int id,String content){
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(id);
        builder.setContent(content);
        return builder.build();
    }
}
