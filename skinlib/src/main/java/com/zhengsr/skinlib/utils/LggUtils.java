package com.zhengsr.skinlib.utils;

import android.util.Log;

public class LggUtils {
    private static final String TAG = "LggUtils";
    private static final boolean ENABLE = true;

    public static void d(String msg){
        Log.d(TAG, "zsr -> "+msg);
    }
    public static void e(String msg){
        Log.e(TAG, "zsr -> "+msg);
    }
}
