package com.example.beijingnews_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by shkstart on 2017/6/7.
 */
public class LocalCachUtils {
    /**
     * 保存图片
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap2Local(String imageUrl, Bitmap bitmap) {
        try {

            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            //文件名称
            String fileName = MD5Encoder.encode(imageUrl);

            File file = new File(dir, fileName);
            //得到 //sdcard/beijingnews/
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                //创建多级目录
                parentFile.mkdirs();
            }
            //创建文件
            if (!file.exists()) {
                file.createNewFile();
            }

            //保存图片
            FileOutputStream fos = new FileOutputStream(file);
            //写入数据
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public Bitmap getBitmap(String imageUrl) {

        try {
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            String fileName = MD5Encoder.encode(imageUrl);

            File file = new File(dir, fileName);
            if (file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

                return bitmap;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
