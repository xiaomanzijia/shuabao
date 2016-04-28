package com.jhlc.km.sb.volley;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * 小袁
 * Created by Administrator on 2015/3/11.
 */
public class IRequest {
	/**
	 * 返回String get
	 * 
	 * @param context
	 * @param url
	 * @param l
	 */
	public static void get(Context context, String url, RequestListener l) {
		RequestManager.get(url, context, l);
	}

	/**
	 * 返回String 带进度条 get
	 * 
	 * @param context
	 * @param url
	 * @param progressTitle
	 * @param l
	 */
	public static void get(Context context, String url, String progressTitle,
						   RequestListener l) {
		RequestManager.get(url, context, progressTitle, l);
	}

	/**
	 * 返回对象 get
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param l
	 * @param <T>
	 */
	public static <T> void get(Context context, String url, Class<T> classOfT,
							   RequestJsonListener<T> l) {
		RequestManager.get(url, context, classOfT, null, false, l);
	}

	/**
	 * 返回对象 带进度条 get
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param progressTitle
	 * @param <T>
	 */
	public static <T> void get(Context context, String url, Class<T> classOfT,
							   String progressTitle, RequestJsonListener<T> l) {
		RequestManager.get(url, context, classOfT, progressTitle, true, l);

	}

	/**
	 * 返回对象 带进度条 get 可选择显示进度 适合带分页
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param progressTitle
	 * @param LoadingShow
	 *            true (显示进度) false (不显示进度)
	 * @param <T>
	 */
	public static <T> void get(Context context, String url, Class<T> classOfT,
							   String progressTitle, boolean LoadingShow, RequestJsonListener<T> l) {
		RequestManager.get(url, context, classOfT, progressTitle, LoadingShow,
				l);

	}

	/**
	 * 返回String post
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param l
	 */
	public static void post(Context context, String url, RequestParams params,
							RequestListener l) {
		RequestManager.post(url, context, params, l);
	}

	/**
	 * 返回对象 post
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param params
	 * @param l
	 */
	public static <T> void post(Context context, String url, Class<T> classOfT,
								RequestParams params, RequestJsonListener<T> l) {
		RequestManager.post(url, context, classOfT, params, null, false, l);
	}

	/**
	 * 返回String 带进度条 post
	 * @param context
	 * @param url
	 * @param params
	 * @param progressTitle
	 * @param l
	 */
	public static void post(Context context, String url, RequestParams params,
							String progressTitle, RequestListener l) {
		RequestManager.post(url, context, params, progressTitle, l);
	}

	/**
	 * 返回对象 带进度条 post
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param params
	 * @param l
	 */
	public static <T> void post(Context context, String url, Class<T> classOfT,
								RequestParams params, String progressTitle, RequestJsonListener<T> l) {
		RequestManager.post(url, context, classOfT, params, progressTitle,
				true, l);

	}

	/**
	 * 返回对象 带进度条 post 可选择显示进度 适合带分页
	 * 
	 * @param context
	 * @param url
	 * @param classOfT
	 * @param params
	 * @param progressTitle
	 * @param LoadingShow
	 *            true (显示进度) false (不显示进度)
	 * @param l
	 */
	public static <T> void post(Context context, String url, Class<T> classOfT,
								RequestParams params, String progressTitle, boolean LoadingShow,
								RequestJsonListener<T> l) {
		RequestManager.post(url, context, classOfT, params, progressTitle,
				true, l);
	}

	public static void getImage(Context context,String url){

	}
}
