package com.zhengsr.skinlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class ZUtils {


    /**
     * 检查某个属性是否为空
     */
    public static <T> T checkNull(T arg,String msg){
        if (arg == null) {
            throw new  NullPointerException(msg);
        }
        return arg;
    }
    /**
     * 检查某个属性是否为空
     */
    public static <T> boolean checkNull(T arg){
        if (arg == null) {
            return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }


    /**
     * 通过路径拿到包名
     * @param context
     * @param skinPath
     * @return
     */
    public static String getPkgName(Context context,String skinPath){
        PackageInfo info = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES |
                PackageManager.GET_SERVICES |
                PackageManager.GET_META_DATA);
        if (info != null) {
            return info.packageName;
        }
        return null;
    }


    /**
     * 关闭可关闭的流
     * @param closeables
     */
    public static void close(Closeable... closeables){
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void  copyAssetFileToStorage(Context context, String assetName, String path,String name){
        InputStream open = null;
        BufferedOutputStream bos = null;
        try {
            open = context.getAssets().open(assetName);

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path,name);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();


            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bytes = new byte[1024];

            int len;
            while ( (len = open.read(bytes)) != -1 ){
                bos.write(bytes,0,len);
            }

            bos.flush();

        } catch (IOException e) {
            LggUtils.d("ZUtils - copyAssetFileToStorage: "+e.getMessage());
            e.printStackTrace();
        }finally {
            ZUtils.close(open,bos);
        }
    }
}
