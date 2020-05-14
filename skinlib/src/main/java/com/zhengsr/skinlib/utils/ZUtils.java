package com.zhengsr.skinlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

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
}
