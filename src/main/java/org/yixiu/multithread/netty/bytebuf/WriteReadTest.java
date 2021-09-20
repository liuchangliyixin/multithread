package org.yixiu.multithread.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.log4j.Logger;

public class WriteReadTest {

    private static Logger logger = Logger.getLogger(WriteReadTest.class);

    @org.junit.Test
    public void testWriteRead() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println("动作：分配 ByteBuf(9, 100):"+buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("动作：写入4个字节 (1,2,3,4):"+buffer);
        logger.info("start==========:get==========");
        getByteBuf(buffer);
        System.out.println("动作：取数据 ByteBuf:"+buffer);
        logger.info("start==========:read==========");
        readByteBuf(buffer);
        System.out.println("动作：读完 ByteBuf:"+buffer);
    }

    //读取一个字节
    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            logger.info("读取一个字节:" + buffer.readByte());
        }
    }


    //读取一个字节，不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.readableBytes(); i++) {
            logger.info("读取一个字节:" + buffer.getByte(i));
        }
    }

}
