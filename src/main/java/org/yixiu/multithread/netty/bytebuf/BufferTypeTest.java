package org.yixiu.multithread.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

public class BufferTypeTest {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static void main(String[] args) {
        System.out.println("=======Heap ByteBuf===========");
        testHeapBuffer();
        System.out.println("=======Direct ByteBuf===========");
        testDirectBuffer();
    }

    private static void testHeapBuffer() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        buf.writeBytes("测试堆缓冲区...".getBytes(UTF_8));
        if(buf.hasArray()){
            byte[] array = buf.array();
            int offset = buf.arrayOffset() + buf.readerIndex();
            int length = buf.readableBytes();
            String msg = new String(array,offset,length);
            System.out.println("Heap Data:" + msg);
        }
        buf.release();
    }

    private static void testDirectBuffer() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        buf.writeBytes("测试直接缓冲区...".getBytes(UTF_8));
        if(!buf.hasArray()){
            int length = buf.readableBytes();
            byte[] array = new byte[length];
            buf.getBytes(buf.readerIndex(),array);
            String msg = new String(array,UTF_8);
            System.out.println("Direct Data:" + msg);
        }
        buf.release();
    }
}
