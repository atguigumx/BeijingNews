package com.example.beijingnews_library.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by shkstart on 2017/6/7.
 */

public class BitmapCacheUtils {
    //网络缓存工具
    private NetCacheUtils netCacheUtils;
    //本地缓存工具类
    private LocalCachUtils localCachUtils;
    public BitmapCacheUtils(Handler handler){
        localCachUtils=new LocalCachUtils();
        netCacheUtils = new NetCacheUtils(handler,localCachUtils);

    }
    public Bitmap getBitmap(String imageUrl, int position) {
        //内存获取
        //从本地获取
        Bitmap bitmap=localCachUtils.getBitmap(imageUrl);
        if(bitmap!=null) {
            return bitmap;
        }
        //网络获取
        netCacheUtils.getBitmapFromNet(imageUrl,position);
        return null;
    }
}
