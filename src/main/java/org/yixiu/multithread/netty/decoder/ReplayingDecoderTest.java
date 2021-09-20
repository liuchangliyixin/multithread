package org.yixiu.multithread.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ReplayingDecoderTest {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline().addLast(new IntegerAddDecoder());
                embeddedChannel.pipeline().addLast(new IntegerProcessHandler());
            }
        });

        for (int i=0;i<100;i++){
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

    static class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.Status>{

        private int first;
        private int second;

        public IntegerAddDecoder() {
            super(Status.PARSE_1);
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            switch (state()){
                case PARSE_1:
                    first = byteBuf.readInt();
                    //第一步解析完成，进入第二步，并设置读断点指针为当前读取位置
                    checkpoint(Status.PARSE_2);
                    break;
                case PARSE_2:
                    second = byteBuf.readInt();
                    int sum = first+second;
                    list.add(sum);
                    checkpoint(Status.PARSE_1);
                    break;
                default:
                    break;
            }
        }

        enum Status{
            PARSE_1,PARSE_2
        }
    }

    static class IntegerProcessHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Integer num = (Integer) msg;
            if(num % 2 == 0){
                System.out.println(num + " is odd!");
            }else{
                System.out.println(num + " is even!");
            }
        }
    }
}
