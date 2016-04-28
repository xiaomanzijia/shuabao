package com.jhlc.km.sb.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownLoader {

	private LruCache<String, Bitmap> mMemoryCache;

	private CacheFileUtils fileUtils;

	private ExecutorService mImageThreadPool = null;
	
	
	public ImageDownLoader(Context context){

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
			
		};
		
		fileUtils = new CacheFileUtils(context);
	}
	

	public ExecutorService getThreadPool(){
		if(mImageThreadPool == null){
			synchronized(ExecutorService.class){
				if(mImageThreadPool == null){
					mImageThreadPool = Executors.newFixedThreadPool(2);
				}
			}
		}
		
		return mImageThreadPool;
		
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null && bitmap != null) {  
	        mMemoryCache.put(key, bitmap);  
	    }  
	}  

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);  
	} 

	public Bitmap downloadImage(final String url, final onImageLoaderListener listener){
		final String subUrl = url.replaceAll("[^\\w]", "");
		Bitmap bitmap = showCacheBitmap(subUrl);
		if(bitmap != null){
			return bitmap;
		}else{
			
			final Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					listener.onImageLoader((Bitmap)msg.obj, url);
				}
			};
			
			getThreadPool().execute(new Runnable() {
				
				@Override
				public void run() {
					Bitmap bitmap = null;
					try {
						bitmap = getBitmap(url);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					
					try {
						fileUtils.savaBitmap(subUrl, bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}

					addBitmapToMemoryCache(subUrl, bitmap);
				}
			});
		}
		
		return null;
	}

	public Bitmap showCacheBitmap(String url){
		if(getBitmapFromMemCache(url) != null){
			return getBitmapFromMemCache(url);
		}else if(fileUtils.isFileExists(url) && fileUtils.getFileSize(url) != 0){
			Bitmap bitmap = fileUtils.getBitmap(url);
			addBitmapToMemoryCache(url, bitmap);
			return bitmap;
		}
		
		return null;
	}


	public Bitmap returnBitmap(String url) {
		URL fileUrl = null;
		Bitmap bitmap = null;

		try {
			fileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) fileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;

	}


	public Bitmap getBitmap(String path) throws IOException{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		return null;
	}
	

	public Bitmap getBitmapFormUrl(String url) {
		Bitmap bitmap = null;
		HttpURLConnection con = null;
		try {
			URL mImageUrl = new URL(url);
			con = (HttpURLConnection) mImageUrl.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			bitmap = BitmapFactory.decodeStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;
	}

	public synchronized void cancelTask() {
		if(mImageThreadPool != null){
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
		}
	}

	public interface onImageLoaderListener{
		void onImageLoader(Bitmap bitmap, String url);
	}
	
}
