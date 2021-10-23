package org.yixiu.multithread.netty.protocol.protobuf;

import org.apache.log4j.Logger;
import org.yixiu.multithread.netty.protocol.MsgProtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtobufTest {

    public static Logger logger = Logger.getLogger(ProtobufTest.class);

    public static void main(String[] args) throws IOException {
        testSerAndDesr1();
        testSerAndDesr2();
        testSerAndDesr3();
    }

    private static void testSerAndDesr1() throws IOException {
        MsgProtos.Msg message = buildMsg();
        //将Protobuf对象序列化成二进制数组
        byte[] bytes = message.toByteArray();
        //可以用于网络传输,保存到内存或外存
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);

        byte[] data = outputStream.toByteArray();
        //二进制字节数组,反序列化成Protobuf 对象
        MsgProtos.Msg msg = MsgProtos.Msg.parseFrom(data);
        logger.info("第一次反序列化得到：id-->" + msg.getId() + "&content-->" + msg.getContent());
    }

    private static void testSerAndDesr2() throws IOException {
        MsgProtos.Msg message = buildMsg();
        //将Protobuf对象序列化成二进制数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //从二进制流 反序列化成Protobuf 对象
        MsgProtos.Msg msg = MsgProtos.Msg.parseFrom(inputStream);
        logger.info("第二次反序列化得到：id-->" + msg.getId() + "&content-->" + msg.getContent());
    }

    private static void testSerAndDesr3() throws IOException {
        MsgProtos.Msg message = buildMsg();
        //将Protobuf对象序列化成二进制数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeDelimitedTo(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //二进制字节数组,反序列化成Protobuf 对象
        MsgProtos.Msg msg = MsgProtos.Msg.parseDelimitedFrom(inputStream);
        logger.info("第三次反序列化得到：id-->" + msg.getId() + "&content-->" + msg.getContent());
    }

    private static MsgProtos.Msg buildMsg() {
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(1);
        builder.setContent("何须浅碧深红，自是花中第一流");
        MsgProtos.Msg msg = builder.build();
        return msg;
    }
}
