package org.yixiu.multithread.netty.protocol.json;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    static GsonBuilder builder = new GsonBuilder();

    static {
        //不需要 html escape
        builder.disableHtmlEscaping();
    }

    public static String pojo2Json(Object obj){
        return builder.create().toJson(obj);
    }

    public static <T> T json2Pojo(String json,Class<T> tClass){
        return JSONObject.parseObject(json,tClass);
    }
}
