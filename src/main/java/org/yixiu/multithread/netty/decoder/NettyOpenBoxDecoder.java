package org.yixiu.multithread.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.UnsupportedEncodingException;

public class NettyOpenBoxDecoder {

    public static final int VER = 2;
    public static final int MAGIC_CODE = 9999;
    public static final String TEST_STR = "两只黄鹂鸣翠柳，一行白鹭上青天。";

    public static void main(String[] args) {
        testLineBasedFrameDecoder();
    }

    static void testLineBasedFrameDecoder(){
        try {
            EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {

                @Override
                protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                    embeddedChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,2,4,4,10));
                    embeddedChannel.pipeline().addLast(new StringDecoder());
                    embeddedChannel.pipeline().addLast(new StringProcessHandler());
                }
            });

            for(int i=0;i<100;i++){
                ByteBuf buffer = Unpooled.buffer();
                String info = i + "次发送:" + TEST_STR;
                byte[] bytes = info.getBytes("UTF-8");
                buffer.writeShort(VER);
                buffer.writeInt(bytes.length);
                buffer.writeInt(MAGIC_CODE);
                buffer.writeBytes(bytes);
                channel.writeInbound(buffer);
            }


            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static class StringProcessHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String info = (String) msg;
            System.out.println("Receive Msg : " + info);
        }
    }
}
