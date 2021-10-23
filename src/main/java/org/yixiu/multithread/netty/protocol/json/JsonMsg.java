package org.yixiu.multithread.netty.protocol.json;

public class JsonMsg {

    private int id;
    private String content;

    public String convertToJson(){
        return JsonUtil.pojo2Json(this);
    }

    public static JsonMsg parseFromJson(String json){
        return JsonUtil.json2Pojo(json,JsonMsg.class);
    }

    @Override
    public String toString() {
        return "JsonMsg{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
