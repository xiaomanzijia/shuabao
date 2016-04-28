package com.jhlc.km.sb.oss;

import android.util.Base64;
import android.util.Log;

/**
 * Created by OSS on 2015/12/11 0011.
 * 使用图片服务处理图片
 */
public class ImageService {
    //字体，默认文泉驿正黑，可以根据文档自行更改
    private static final String font = "d3F5LXplbmhlaQ==";

    //给图片打上文字水印，除了大小字体之外其他都是默认值，有需要更改的可以参考图片服务文档自行调整
    public String textWatermark(String object, String text, int size) {
        String base64Text = Base64.encodeToString(text.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);

        String queryString = "@watermark=2&type=" + font + "&text=" + base64Text + "&size=" + String.valueOf(size);
        Log.d("TextWatermark", object);
        Log.d("Text", text);
        Log.d("QuerySyring", queryString);

        return (object + queryString);
    }

    //强制缩放，其他缩放方式可以参考图片服务文档
    public String resize(String object, int width, int height) {
        String queryString = "@" + String.valueOf(width) + "w_" + String.valueOf(height) + "h_2e";

        Log.d("ResizeImage", object);
        Log.d("Width", String.valueOf(width));
        Log.d("Height", String.valueOf(height));
        Log.d("QueryString", queryString);

        return object + queryString;
    }

    public String crop(String object, int width, int height, int pos) {
        String queryString = "@" + String.valueOf(width) + "x" + String.valueOf(height) + "-" + String.valueOf(pos) + "rc";

        Log.d("CropImage", object);
        Log.d("Width", String.valueOf(width));
        Log.d("Height", String.valueOf(height));
        Log.d("QueryString", queryString);

        return (object + queryString);
    }

    public String rotate(String object, int deg) {
        String queryString = "@" + String.valueOf(deg) + "r";

        Log.d("CropImage", object);
        Log.d("Degree", String.valueOf(deg));
        Log.d("QueryString", queryString);

        return (object + queryString);
    }



}
