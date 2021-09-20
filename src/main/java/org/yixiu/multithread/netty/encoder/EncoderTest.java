package org.yixiu.multithread.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class EncoderTest {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline().addLast(new IntegerToByteEncoder());
                embeddedChannel.pipeline().addLast(new StringToIntegerEncoder());
            }
        });

        for(int i=0;i<100;i++){
            String str = "Number is " + i;
            channel.write(str);
        }
        channel.flush();

        ByteBuf outbound = channel.readOutbound();
        while(null != outbound){
            System.out.println("outbound = " + outbound.readInt());
            outbound = channel.readOutbound();
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class IntegerToByteEncoder extends MessageToByteEncoder<Integer>{

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf byteBuf) throws Exception {
            byteBuf.writeInt(integer);
            System.out.println("输出数字：" + integer);
        }
    }

    static class StringToIntegerEncoder extends MessageToMessageEncoder<String>{

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
            System.out.println("输入字符串：" + s);
            char[] chars = s.toCharArray();
            for (char c : chars) {
                if(c >= 48 && c <= 57){
                    //48 是0的编码，57 是9 的编码
                    list.add(new Integer(c));
                }
            }
        }
    }
}
