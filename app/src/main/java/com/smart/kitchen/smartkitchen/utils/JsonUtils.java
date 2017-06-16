package com.smart.kitchen.smartkitchen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.entitys.Messages;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    public static String JsonParse(Messages messages) {
        if (messages.getData() instanceof String) {
            return (String) messages.getData();
        }
        return JSON.toJSONString(messages.getData());
    }

    public static Messages getMessages(String str) {
        return (Messages) JSON.parseObject(str, new TypeReference<Messages>() {
        }, new Feature[0]);
    }
}
