package com.zhengsr.skinlib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.skinlib.callback.IBaseSkinDelegate;
import com.zhengsr.skinlib.utils.LggUtils;

import java.lang.reflect.Field;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 皮肤管理类
 */
public class ZSkin {


    /**
     * 注册类，用来拿到 factory 类
     * @param context
     * @return
     */
    public static LayoutInflater inject(Context context){
        LayoutInflater inflater;

        if (context instanceof Activity){
            inflater = ((Activity) context).getLayoutInflater();
        }else{
            inflater = LayoutInflater.from(context);
        }

        if (inflater == null){
            return null;
        }

        //如果此时 factory2 为null，那么就可以设置自己的 factory2
        if (inflater.getFactory2() == null) {
            SkinFactory factory = setDelegateFactory(context);
            inflater.setFactory2(factory);
        }else if (!(inflater.getFactory2() instanceof SkinFactory)){
            //如果已经设置了  factory2，但是又不是我们自身的 factory,这时就重新设置成我们的 factory2了。
            forceSetFactory2(inflater);
        }

        return inflater;
    }

    /**
     * 把我们的 factory 设置进去
     * @param inflater
     */
    private static void forceSetFactory2(LayoutInflater inflater) {
        try{
            //可能用了 v4 的兼容包
            Field sCheckedField = LayoutInflaterCompat.class.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(inflater,false);

            //重新设置 factory2 和 factory
            Field mFactory = LayoutInflater.class.getDeclaredField("mFactory");
            mFactory.setAccessible(true);
            Field mFactory2 = LayoutInflater.class.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);

            SkinFactory factory = new SkinFactory();

            if (inflater.getFactory2() != null) {
                factory.setInterceptFactory2(inflater.getFactory2());
            }else if (inflater.getFactory() != null){
                factory.setInterceptFactory(inflater.getFactory());
            }

            mFactory.set(inflater,factory);
            mFactory2.set(inflater,factory);


        }catch(Exception e){
            LggUtils.d("ZSkin - forceSetFactory2: "+e);
            e.printStackTrace();
        }
    }


    /**
     * 设置 factory2
     * @param context
     * @return
     */
    private static SkinFactory setDelegateFactory(Context context){
        SkinFactory factory = new SkinFactory();
        //由于 appcompatactivity 已经设置了  factory2，所以，这里我们也要支持系统的 factory2
        if (context instanceof AppCompatActivity){
            final AppCompatDelegate delegate = ((AppCompatActivity) context).getDelegate();
            factory.setInterceptFactory(new LayoutInflater.Factory() {
                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {
                    return delegate.createView(null,name,context,attrs);
                }
            });
        }

        return factory;
    }


    public static void  addDelegate(Class view,IBaseSkinDelegate delegate){
        SkinManager.CusDELEGATEMAP.put(view.getName(),delegate);
    }


    /**
     * 加载插件包路径
     * @param skinPath
     */
    public static void loadSkin(String skinPath){
        SkinManager.get().loadSkin(skinPath);
    }

    /**
     * 应用内换肤，如果不怕体积过大，可以使用该方式
     * 所有的资源必须前缀 prefix 为开头，
     * 比如prefix 是2k_  ，则原始资源为 bottom_pen，换肤资源为2k_bottom_pen
     * @param prefix
     */
    public static void loadSkinByPrefix(String prefix){
        SkinManager.get().loadSkinByPrefix(prefix);
    }


    /**
     * 是否需要换肤，比如recyclerview 中的资源，可以通过此方法，判断是否需要加载皮肤
     * @return
     */
    public static boolean isLoadSkin(){
        return SkinManager.get().isChangeNow();
    }

    /**
     * 通过 id 拿到 皮肤中的 drawable
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId){
        return SkinManager.get().getDrawable(resId);
    }

    public static int getColor(int resId){
        return SkinManager.get().getColor(resId);
    }

    public static Resources getResource(){
        return SkinManager.get().getResource();
    }




}
