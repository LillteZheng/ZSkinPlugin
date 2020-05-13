package com.zhengsr.skinlib;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.skinlib.utils.LggUtils;

public class SkinFactory implements LayoutInflater.Factory {


    public static void setFactory(final Activity activity){
        SkinFactory factory = new SkinFactory();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (activity instanceof AppCompatActivity){
            /**
             * 由于 appcompatactivity 本身就有实现 factory 的功能，所以除了要实现
             */
            AppCompatDelegate delegate = ((AppCompatActivity) activity).getDelegate();

        }
        /**
         * 这里设置了 Factory，里面也是设置成 Factory2，不用担心，这里只是为了少了一个接口回调
         */
        layoutInflater.setFactory(factory);
    }



    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        //找到需要换肤的类
        if (isSkinEnable(attrs)){
          //首先先拿到 view


        }
        return null;
    }




    /**
     * 需要在 xml 中，先设置     xmlns:skin="http://schemas.android.com/android/skin"
     * 然后在需要的空间中，设置 skin:skin_enable = "true";
     * @param attrs
     * @return
     */
    private boolean isSkinEnable(AttributeSet attrs){
        return attrs.getAttributeBooleanValue(SkinConstants.SKIN_XML_NAMESPACE,SkinConstants.SKIN_ATTRS,false);
    }
}
