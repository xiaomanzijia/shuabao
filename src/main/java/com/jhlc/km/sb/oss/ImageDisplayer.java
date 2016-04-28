package com.jhlc.km.sb.oss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by OSS on 2015/12/7 0007.
 * 完成显示图片操作
 */
public class ImageDisplayer {

    private ImageView imageView;
    private int height;
    private int width;

    public ImageDisplayer(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageDisplayer(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public static byte[] getBytesFromStream(InputStream stream) throws IOException {
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = stream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            return outStream.toByteArray();
        }
    }

    //计算图片缩放比例
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //根据ImageView的大小自动缩放图片
    public Bitmap autoResizeFromLocalFile(String picturePath) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        // Calculate inSampleSize
        int h = height;
        int w = width;
        if (imageView != null) {
            h = imageView.getHeight();
            w = imageView.getWidth();
        }
        options.inSampleSize = calculateInSampleSize(options, w, h);
        Log.d("ImageHeight", String.valueOf(options.outHeight));
        Log.d("ImageWidth", String.valueOf(options.outWidth));
        Log.d("Height", String.valueOf(h));
        Log.d("Width",String.valueOf(w));
        //options.inSampleSize = 10;

        Log.d("SampleSize", String.valueOf(options.inSampleSize));
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);

    }


    public Bitmap autoResizeFromBytes(byte[] data) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        int h = height;
        int w = width;
        if (imageView != null) {
            h = imageView.getHeight();
            w = imageView.getWidth();
        }
        options.inSampleSize = calculateInSampleSize(options, w, h);

        Log.d("ImageHeight", String.valueOf(options.outHeight));
        Log.d("ImageWidth", String.valueOf(options.outWidth));
        Log.d("Height", String.valueOf(h));
        Log.d("Width",String.valueOf(w));
        //options.inSampleSize = 10;

        Log.d("SampleSize", String.valueOf(options.inSampleSize));
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    //根据ImageView大小自动缩放图片
    public Bitmap autoResizeFromStream(InputStream stream) throws IOException {
        byte[] data = getBytesFromStream(stream);
        return autoResizeFromBytes(data);
    }

    public Bitmap autoResizeFromBitmap(Bitmap bm) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = bm.getHeight();
        options.outWidth = bm.getWidth();

        int h = height;
        int w = width;
        if (imageView != null) {
            h = imageView.getHeight();
            w = imageView.getWidth();
        }
        int inSampleSize = calculateInSampleSize(options, w, h);
        Log.d("ImageHeight", String.valueOf(options.outHeight));
        Log.d("ImageWidth", String.valueOf(options.outWidth));
        Log.d("Height", String.valueOf(h));
        Log.d("Width",String.valueOf(w));
        if (inSampleSize == 1) {
            return bm;
        }
        else {
            return Bitmap.createScaledBitmap(bm, bm.getWidth() / inSampleSize, bm.getHeight() / inSampleSize, true);
        }
    }
}
