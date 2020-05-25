package com.zhengsr.skinlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.Map;
import java.util.Set;

/**
 * Created by zhengshaorui on 2017/2/24.
 */

public class ZSprefUtils {
    private static final String FILE_NAME = "skinlib";
    private static Context sContext ;


    public static void init(Context context){
        sContext =context;
    }

    /**
     * 保存数据
     * @param key
     * @param value
     */
    public static  void put(String key,Object value){
        put(FILE_NAME,key,value);
    }


    public static void put(String fileName,String key,Object value){
        SharedPreferences.Editor editor =
                sContext.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        if (value instanceof String
                ||value instanceof Integer
                ||value instanceof Boolean
                || value instanceof Float
                || value instanceof Long
                ||value instanceof Double) {
            //转换成 string，这样封边封装
            editor.putString(key, String.valueOf(value));
        }else{
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.commit();
    }

    /**
     * 拿到数值
     * @param <T>
     * @return
     */
    public static <T> T get(String fileName,String key,T defaultValue){
        SharedPreferences sp = sContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        String value = sp.getString(key, String.valueOf(defaultValue));
        if (defaultValue instanceof String){
            return (T) value;
        }if (defaultValue instanceof Integer){
            return (T) Integer.valueOf(value);
        }if (defaultValue instanceof Boolean){
            return (T) Boolean.valueOf(value);
        }if (defaultValue instanceof Float){
            return (T) Float.valueOf(value);
        }if (defaultValue instanceof Long){
            return (T) Long.valueOf(value);
        }if (defaultValue instanceof Double){
            return (T) Double.valueOf(value);
        }else{
            return (T) sp.getStringSet(key, (Set<String>) defaultValue);
        }
    }

    public static <T> T get(String key,T defaultValue){
        return get(FILE_NAME,key,defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public static void remove(String fileName,String key) {
        SharedPreferences.Editor editor =
                sContext.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void remove(String key) {
        remove(FILE_NAME,key);
    }

    /**
     * 清除所有数据
     */
    public static void clear(String fileName) {
        SharedPreferences.Editor editor =
                sContext.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    public static void clear(Context context) {
        clear(FILE_NAME);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static <T> Map<String, T> getAll(String fileName) {
        SharedPreferences sp = sContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return (Map<String, T>) sp.getAll();
    }

    public static <T> Map<String, T> getAll() {

        return getAll(FILE_NAME);
    }
    /**
     * key 是否存在
     * @param fileName
     * @param key
     * @return
     */
    public static boolean isExist(String fileName,String key){
        SharedPreferences sp = sContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return !"nullValue".equals(sp.getString(key,"nullValue"));
    }

    public static boolean isExist(String key){
        return isExist(FILE_NAME,key);
    }

}