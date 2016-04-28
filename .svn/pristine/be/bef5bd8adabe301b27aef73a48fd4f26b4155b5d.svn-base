package com.jhlc.km.sb.utils;

import com.google.gson.Gson;

/**
 * json管理
 * 
 * @author Administrator
 * 
 */
public class JsonUtils {
	private static Gson gson = new Gson();

	public static <T> T object(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}
	public static <T> String toJson(Class<T> param) {
		return gson.toJson(param);
	}
}
