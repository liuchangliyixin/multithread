package org.yixiu.multithread.netty.decoder;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

public class StringReplayingDecoderTest {

    public static final String TEST_STR = "两只黄鹂鸣翠柳，一行白鹭上青天。";

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                //embeddedChannel.pipeline().addLast(new StringReplayingDecoder());
                embeddedChannel.pipeline().addLast(new StringByteToMessageDecoder());
                embeddedChannel.pipeline().addLast(new StringProcessorHandler());
            }
        });

        Random random = new Random();
        byte[] bytes = TEST_STR.getBytes(Charset.forName("UTF-8"));
        for(int i=1;i<=100;i++){
            int nextInt = random.nextInt(3);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(bytes.length*nextInt);
            for(int j=0;j<nextInt;j++){
                buffer.writeBytes(bytes);
            }
            channel.writeInbound(buffer);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class StringReplayingDecoder extends ReplayingDecoder<StringReplayingDecoder.Status>{

        private int length;
        private byte[] bytes;

        public StringReplayingDecoder() {
            super(Status.PARSE_1);
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            switch (state()){
                case PARSE_1:
                    length = byteBuf.readInt();
                    bytes = new byte[length];
                    checkpoint(Status.PARSE_2);
                    break;
                case PARSE_2:
                    byteBuf.readBytes(bytes,0,length);
                    String msg = new String(bytes,"UTF-8");
                    list.add(length+"_"+msg);
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

    static class StringProcessorHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String msgInfo = (String) msg;
            //int length = Integer.parseInt(msgInfo.substring(0,((String) msg).indexOf("_")));
            String info = msgInfo.substring(msgInfo.indexOf("_")+1,msgInfo.length());
            //System.out.println("Info length:"+length);
            System.out.println("Info data:"+info);
        }
    }

    static class StringByteToMessageDecoder extends ByteToMessageDecoder{

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            if(byteBuf.readableBytes() < 4){
                return;
            }

            byteBuf.markReaderIndex();
            int length = byteBuf.readInt();
            if(byteBuf.readableBytes() < length){
                byteBuf.resetReaderIndex();
                return;
            }

            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes,0,length);
            list.add(new String(bytes,"UTF-8"));
        }
    }
}
