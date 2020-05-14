package com.zhengsr.skinlib;

import android.app.Activity;
import android.text.TextUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 皮肤管理类
 */
public class ZSkin {
    private static  String sSkinPath = null;
    public static void loadSkinNow(String skinPath){
        sSkinPath = skinPath;
    }

    public static SkinFactory setFactory(Activity activity){
      /*  if (TextUtils.isEmpty(sSkinPath)){
        }else{
            getFactory().factory(activity,sSkinPath);
        }*/
          return   getFactory().factory(activity);

    }

    public static void changeSkin(){
       getFactory().changeSkin();
    }


    public static void changeSkin(String skinPath){
        SkinFactory.loadSkin(skinPath);
    }


    private static SkinFactory getFactory(){
        return SkinFactory.get();
    }


}
