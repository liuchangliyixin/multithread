package org.yixiu.multithread.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

public class ByteToMessageDecoderTest {

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline().addLast(new ByteToIntegerDecoder());
                embeddedChannel.pipeline().addLast(new IntegerProcessHandler());
            }
        });

        for(int i=0; i<100; i++){
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            channel.writeInbound(buffer);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class ByteToIntegerDecoder extends ByteToMessageDecoder{

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            while(byteBuf.readableBytes() >= 4){
                int num = byteBuf.readInt();
                System.out.println("解码后得到数值：" + num);
                list.add(num);
            }
        }
    }

    static class IntegerProcessHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Integer integer = (Integer) msg;
            if(integer % 2 == 0){
                System.out.println(integer + " is odd.");
            }else{
                System.out.println(integer + " is even.");
            }
        }
    }
}
