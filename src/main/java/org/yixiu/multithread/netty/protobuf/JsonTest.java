package org.yixiu.multithread.netty.protobuf;

public class JsonTest {
    public static void main(String[] args) {
        JsonMsg msg = new JsonMsg();
        msg.setId(1);
        msg.setContent("何须浅碧深红色，自是花中第一流。");
        String json = msg.convertToJson();
        System.out.println("Obj To Json: "+json);
        JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
        System.out.println("Json To Obj: "+jsonMsg);
    }
}
