package com.zhengsr.skinlib;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.skinlib.emtity.ConfigBean;
import com.zhengsr.skinlib.emtity.SkinAttr;
import com.zhengsr.skinlib.parse.ParseConsumer;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 皮肤管理类
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private Activity mActivity;
    private ConfigBean mBean;
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();

    private final Object[] mConstructorArgs = new Object[2];
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};


    private static final SkinFactory INSTANCE = new SkinFactory();

    public static SkinFactory get(){
        return INSTANCE;
    }


    public SkinFactory factory(Activity activity){
        mActivity = activity;
        mBean = new ConfigBean();
        return this;
    }



    public SkinFactory loadSkinNow(String skinPath){
        mBean.skinPath = skinPath;
        return this;
    }

    public void load(){
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        layoutInflater.setFactory2(this);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //找到需要换肤的类
        if (isSkinEnable(attrs)) {
            //首先先拿到 view
            View view = null;
            /**
             * 由于 appcompatactivity 本身就有实现 factory 的功能,且里面的view，有appcompat的一些属性，这里保留着
             */
            if (mActivity instanceof AppCompatActivity) {
                AppCompatDelegate delegate = ((AppCompatActivity) mActivity).getDelegate();
                view = delegate.createView(parent, name, context, attrs);
            }

            if (view == null) {
                view = createViewFromTag(context,name,attrs);
            }

            if (view != null) {
                //开始解析view
                parseSkinAttr(view,attrs);
            }
            return view;

        }
        return null;
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return null;
    }


    /**
     * 如果设置了 tags 的变量，则只替换tags 里面的数据
     * 如果没有，则替换所有的属性
     * @param view
     * @param attrs
     */
    private void parseSkinAttr(@NonNull View view, AttributeSet attrs){
        //是否有定义 tag
        String tags = attrs.getAttributeValue(SkinConstants.SKIN_XML_NAMESPACE, SkinConstants.SKIN_TAG);
        String[] tagNames = null;
        if (!TextUtils.isEmpty(tags)){
            tagNames = tags.trim().split("\\|");
        }

        HashMap<String, SkinAttr> attrHashMap = ParseConsumer.parseSkinAttr(view, attrs, tagNames);


        //拿到属性之后，保存一个view 对应的 属性值

        SkinManager.get().config(mActivity).saveViewAttr(view,attrHashMap,mBean);


    }


    /**
     * 需要在 xml 中，先设置     xmlns:skin="http://schemas.android.com/android/skin"
     * 然后在需要的空间中，设置 skin:skin_enable = "true";
     *
     * @param attrs
     * @return
     */
    private boolean isSkinEnable(AttributeSet attrs) {
        return attrs.getAttributeBooleanValue(SkinConstants.SKIN_XML_NAMESPACE, SkinConstants.SKIN_ATTRS, false);
    }

    /**
     * copy android.support.v7.app.AppcompatViewInflater #createViewFromTag()
     * @param context
     * @param name
     * @param attrs
     * @return
     */
    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createView(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    /**
     * 创建view
     * @param context
     * @param name
     * @param prefix
     * @return
     * @throws ClassNotFoundException
     * @throws InflateException
     */
    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    public static void loadSkin(String skinPath){
        SkinManager.get().loadSkin(skinPath);
    }

    public void changeSkin() {
        if (mBean != null) {
           // SkinManager.get().loadSkin();
        }
    }
}
