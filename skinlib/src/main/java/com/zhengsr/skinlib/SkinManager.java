package com.zhengsr.skinlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.zhengsr.skinlib.callback.IBaseSkinDelegate;
import com.zhengsr.skinlib.entity.SkinAttr;
import com.zhengsr.skinlib.delegate.BackgroundDelegate;
import com.zhengsr.skinlib.delegate.SrcDelegate;
import com.zhengsr.skinlib.delegate.TextColorDelegate;
import com.zhengsr.skinlib.utils.LggUtils;
import com.zhengsr.skinlib.utils.ZUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 皮肤管理类
 */
class SkinManager {

    private final HashMap<View, Map<String, SkinAttr>> VIEWMAP = new LinkedHashMap<>();
    private Context mContext;
    private String mPkgName;
    private String mPluginPath;
    private boolean isChangeNow;
    private Resources mResources;
    private String mPreFix;

    private SkinManager() {
    }

    public static final WeakHashMap<String, IBaseSkinDelegate> DELEGATEMAP = new WeakHashMap<>();
    public static final WeakHashMap<String, IBaseSkinDelegate> CusDELEGATEMAP = new WeakHashMap<>();

    static {
        DELEGATEMAP.put(SkinConstants.TEXTCOLOR, new TextColorDelegate());
        DELEGATEMAP.put(SkinConstants.BACKGROUND, new BackgroundDelegate());
        DELEGATEMAP.put(SkinConstants.SRC, new SrcDelegate());
    }

    private static class Holder {
        static SkinManager HODLER = new SkinManager();
    }


    public static SkinManager get() {
        return Holder.HODLER;
    }

    public SkinManager config(Context context) {
        mContext = context.getApplicationContext();
        return this;
    }

    public void parseViewAttr(@NonNull View view, AttributeSet attrs, String... tag) {
        int count = attrs.getAttributeCount();

        HashMap<String, SkinAttr> viewAttrs = new LinkedHashMap<>();
        //拿到所有属性
        //todo 处理 style 类型，避免theme的数据被覆盖


        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);


            //先判断是否有 tag 标签,如果tag为null，则解析所有标签
            //如果tag有数据，则过滤不是 tag标签里面的数据
            if (!filterTags(attrName, tag)) {
                continue;
            }
            //todo style 不支持，后面可以有没有办法可以解决
            if ("style".equals(attrName)) {

                // continue;
            }

            //比如 @color/white , 拿到的数据时 @xxx 一串数值
            if (attrValue.startsWith("@")) {

                SkinAttr attr = new SkinAttr();
                attr.attrValueId = Integer.valueOf(attrValue.substring(1));
                if (attr.attrValueId != 0) {
                    attr.entryName = view.getContext().getResources().getResourceEntryName(attr.attrValueId);
                    //判断是否是应用内换肤
                    if (isLoadSkinBySelf()) {
                        attr.entryName = mPreFix+attr.entryName;
                    }
                    attr.typeName = view.getContext().getResources().getResourceTypeName(attr.attrValueId);
                    attr.attrName = attrName;
                    viewAttrs.put(attrName, attr);
                }

            }
        }

        saveViewAttr(view, viewAttrs);
    }


    public void saveViewAttr(View view, HashMap<String, SkinAttr> map) {
        if (view == null || map == null || map.isEmpty()) {
            return;
        }

        if (isChangeNow) {
            doAttrsToView(view, map);
        }
        //保存下次调用
        Map<String, SkinAttr> attrMap = VIEWMAP.get(view);
        if (attrMap == null) {
            VIEWMAP.put(view, map);
        }


    }


    public void changeSkin() {
        mResources = null;
        notifyChangeSkin();
    }

    /**
     * 插件换肤
     *
     * @param skinPath
     */
    public void loadSkin(String skinPath) {

        mResources = null;

        File file = new File(skinPath);
        if (!file.exists()) {
            //todo 添加一个监听
            LggUtils.d("SkinManager - loadSkin fail: " + "skinPath don't exist: " + skinPath);
            return;
        }

        //todo 通知所有的view
        this.isChangeNow = true;

        mPkgName = ZUtils.getPkgName(mContext, skinPath);
        mPluginPath = skinPath;
        mResources = getResource();
        notifyChangeSkin();
    }


    /**
     * 应用内换肤，所有的资源必须前缀 prefix 为开头，
     * 比如prefix 是2k_  ，则原始资源为 bottom_pen，换肤资源为2k_bottom_pen
     *
     * @param prefix
     */
    public void loadSkinByPrefix(String prefix) {
        mPreFix = prefix;
        mPluginPath = null;
        this.isChangeNow = true;
        mPkgName = mContext.getPackageName();
    }

    /**
     * 是否是应用内换肤
     *
     * @return
     */
    public boolean isLoadSkinBySelf() {
        return mPreFix != null && mPluginPath == null;
    }

    public boolean isChangeNow() {
        return isChangeNow;
    }

    private void notifyChangeSkin() {
        if (VIEWMAP.isEmpty()) {
            return;
        }

        Set<Map.Entry<View, Map<String, SkinAttr>>> entries =
                VIEWMAP.entrySet();
        for (Map.Entry<View, Map<String, SkinAttr>> mapEntry : entries) {
            View view = mapEntry.getKey();
            Map<String, SkinAttr> value = mapEntry.getValue();

            //使用map+策略模式
            doAttrsToView(view, value);
        }
    }


    /**
     * 把属性填充到view中
     *
     * @param view
     * @param value
     */
    private void doAttrsToView(View view, Map<String, SkinAttr> value) {
        if (mPkgName != null) {
            /**
             * 自定义的
             */
            IBaseSkinDelegate skinDelegate = CusDELEGATEMAP.get(view.getClass().getName());
            if (skinDelegate != null) {
                skinDelegate.apply(view, value, getResource(), mPkgName);
            } else {

                /**
                 * 自带的属性
                 */
                Set<Map.Entry<String, SkinAttr>> entries = value.entrySet();
                for (Map.Entry<String, SkinAttr> attrEntry : entries) {
                    String key = attrEntry.getKey();
                    SkinAttr attr = attrEntry.getValue();

                    IBaseSkinDelegate delegate = DELEGATEMAP.get(key);
                    if (delegate != null) {
                        delegate.apply(view, attr, getResource(), mPkgName);
                    }
                }
            }
        }

    }

    private SkinAttr parseSkinAttr(String attrName, int resId) {
        if (mContext == null) {
            return null;
        }
        SkinAttr skinAttr = null;
        try {
            String attrValueName = mContext.getResources().getResourceEntryName(resId);
            String attrValueType = mContext.getResources().getResourceTypeName(resId);
            skinAttr = new SkinAttr(attrName, resId, attrValueName, attrValueType);
            // LggUtils.d("SkinManager - parseSkinAttr: "+attrName+" "+attrValueName+" "+attrValueType);
        } catch (Exception ex) {

        }
        return skinAttr;
    }

    /**
     * 获取 drawable
     *
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId) {
        SkinAttr attr = parseSkinAttr(null, resId);
        if (attr != null && mResources != null) {

            int trueResId = mResources.getIdentifier(attr.entryName, attr.typeName, mPkgName);
            if (trueResId != 0) {
                try {
                    Drawable drawable = mResources.getDrawable(trueResId);

                    return drawable;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public int getColor(int resId) {
        SkinAttr attr = parseSkinAttr(null, resId);
        if (attr != null && mResources != null) {

            int trueResId = mResources.getIdentifier(attr.entryName, attr.typeName, mPkgName);
            if (trueResId != 0) {
                try {
                    return mResources.getColor(trueResId);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return 0;
    }


    public Resources getResource() {
        if (mResources == null) {

            if (mPluginPath != null) {
                mResources = getPluginResource(mPluginPath);
            } else {
                mResources = mContext.getResources();
            }
        }
        return mResources;
    }

    private Resources getPluginResource(String skinPath) {
        try {
            //拿到资源加载器

            AssetManager assetManager = AssetManager.class.newInstance();

            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, skinPath);
            Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics()
                    , mContext.getResources().getConfiguration());

            return resources;


        } catch (Exception e) {
            LggUtils.e("SkinManager - loadSkinPath error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否需要过滤tag标签
     *
     * @param name
     * @param tag
     * @return
     */
    private static boolean filterTags(String name, String... tag) {
        //如果tag为null，也认为它是不需要过滤的
        if (tag == null || tag.length == 0) {
            return true;
        }
        for (String s : tag) {
            if (s.trim().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
