package com.jhlc.km.sb.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;

import com.jhlc.km.sb.constants.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

/**
 * Created by licheng on 17/3/16.
 */
public class StoragePicure {


    /**
     * 将从Message中获取的，表示图片的字符串解析为Bitmap对象
     *
     * @param imgByte
     * @return
     */
    public static Bitmap decodeImg(byte[] imgByte) {
        Bitmap bitmap = null;

        InputStream input = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //该值可以设置图片显示尺寸
            options.inSampleSize = 3;
            input = new ByteArrayInputStream(imgByte);
            SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
            bitmap = (Bitmap) softRef.get();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imgByte != null) {
                imgByte = null;
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    public static void savePicture(byte[] data, Camera.Size size, File file){

        /**
         * 存储方法一:不能解决图片旋转
         */

//        try {
//            Calendar c = Calendar.getInstance();
//            String time = formatTimer(c.get(Calendar.YEAR)) + "-"
//                    + formatTimer(c.get(Calendar.MONTH)) + "-"
//                    + formatTimer(c.get(Calendar.DAY_OF_MONTH)) + " "
//                    + formatTimer(c.get(Calendar.HOUR_OF_DAY)) + "."
//                    + formatTimer(c.get(Calendar.MINUTE)) + "."
//                    + formatTimer(c.get(Calendar.SECOND));
//            System.out.println("现在时间：" + time + "  将此时间当作图片名存储");
//
//            String path = file + "/"+time + ".jpg";
//            File file1 = new File(path);
//            if(!file1.exists()){
//                file1.createNewFile();
//            }
//            FileOutputStream filecon = new FileOutputStream(path);
//
//
//            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width,size.height, null);
//
//            if(yuvImage != null){
//                yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, filecon);
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /**
         *存储方法二
         */

        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width,size.height, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 50, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap image = decodeImg(imageBytes);
//        //图片旋转方向
        Bitmap bMapRotate;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(90);
        bMapRotate = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
                image.getHeight(), matrix, true);
        image = bMapRotate;
        File second = new File(file,""+System.currentTimeMillis()+".jpg");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(second));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            /* 采用压缩转档方法 */
        image.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        image.recycle();
        System.out.println("this is pass");
        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String formatTimer(int d) {
        return d >= 10 ? "" + d : "0" + d;
    }

}
