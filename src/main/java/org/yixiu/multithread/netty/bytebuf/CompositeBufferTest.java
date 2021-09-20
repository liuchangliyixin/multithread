package org.yixiu.multithread.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class CompositeBufferTest {
    public static void main(String[] args) {
        intCompositeBuffer();
        byteCompositeBuffer();
    }

    private static void byteCompositeBuffer() {
        CompositeByteBuf com = ByteBufAllocator.DEFAULT.compositeBuffer();
        ByteBuf header = Unpooled.copiedBuffer("测试数据头:",Charset.forName("UTF-8"));
        ByteBuf body = Unpooled.copiedBuffer("测试数据体1",Charset.forName("UTF-8"));
        com.addComponents(header,body);
        sendMsg(com);
        header.retain();
        com.release();

        com = ByteBufAllocator.DEFAULT.compositeBuffer();
        ByteBuf body2 = Unpooled.copiedBuffer("测试数据体2",Charset.forName("UTF-8"));
        com.addComponents(header,body2);
        sendMsg(com);
        com.release();
    }

    private static void sendMsg(CompositeByteBuf com) {
        for (ByteBuf byteBuf : com) {
            int length = byteBuf.readableBytes();
            byte[] bytes = new byte[length];
            byteBuf.getBytes(byteBuf.readerIndex(),bytes);
            System.out.println(new String(bytes,Charset.forName("UTF-8")));
        }
        System.out.println("==============");
    }

    private static void intCompositeBuffer() {
        CompositeByteBuf com = Unpooled.compositeBuffer(3);
        com.addComponent(Unpooled.wrappedBuffer(new byte[]{1,2,3}));
        com.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        com.addComponent(Unpooled.wrappedBuffer(new byte[]{5,6}));

        ByteBuffer byteBuffer = com.nioBuffer(0,6);
        byte[] array = byteBuffer.array();
        System.out.print("array = ");
        for (byte b : array) {
            System.out.print(b + " ");
            System.out.println();
        }
        System.out.println("==========");
        com.release();
    }
}
