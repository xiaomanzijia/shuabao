package com.jhlc.km.sb.common;

import java.util.UUID;

/**
 * Created by licheng on 9/3/16.
 */
public class UUIDGenerator {

    public UUIDGenerator() {
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String uStr = str.replace("-","");
        return uStr;
    }
}
