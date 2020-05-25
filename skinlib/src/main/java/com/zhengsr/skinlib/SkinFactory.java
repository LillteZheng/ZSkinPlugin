package com.zhengsr.skinlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.skinlib.utils.LggUtils;

import java.lang.reflect.Constructor;
import java.util.Map;


/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 皮肤管理类
 */
class SkinFactory implements LayoutInflater.Factory2 {

    private LayoutInflater.Factory mViewCreateFactory;
    private LayoutInflater.Factory2 mViewCreateFactory2;

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




    public void setInterceptFactory(LayoutInflater.Factory factory) {
        mViewCreateFactory = factory;
    }

    public void setInterceptFactory2(LayoutInflater.Factory2 factory2) {
        mViewCreateFactory2 = factory2;
    }





    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        //找到需要换肤的类
        if (isSkinEnable(attrs)) {
            //首先先拿到 view

            /**
             * 防止与其他库冲突，比如替换字体等
             */
            if (mViewCreateFactory2 != null){
                view = mViewCreateFactory2.onCreateView(name,context,attrs);
                if (view == null) {
                    view = mViewCreateFactory2.onCreateView(null,name,context,attrs);
                }
            }

            if (mViewCreateFactory != null){
                view = mViewCreateFactory.onCreateView(name,context,attrs);
            }

            if (view == null) {
                view = createViewFromTag(context,name,attrs);
            }

            if (view != null) {
                //开始解析view
                parseSkinAttr(view,attrs);
            }
        }
        return view;
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

      //  HashMap<String, SkinAttr> attrHashMap = ParseConsumer.parseSkinAttr(view, attrs, tagNames);


        //拿到属性之后，保存一个view 对应的 属性值

        SkinManager.get().parseViewAttr(view,attrs,tagNames);


    }


    /**
     * 需要在 xml 中，先设置     xmlns:skin="http://schemas.android.com/android/skin"
     * 然后在需要的空间中，设置 skin:enable = "true";
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



    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
       return onCreateView(name,context,attrs);
    }
}
