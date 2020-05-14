package com.zhengsr.skinlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.emtity.ConfigBean;
import com.zhengsr.skinlib.emtity.SkinAttr;
import com.zhengsr.skinlib.parse.BackgroundDelegate;
import com.zhengsr.skinlib.parse.SrcDelegate;
import com.zhengsr.skinlib.parse.TextColorDelegate;
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

    private final HashMap<View,Map<String,SkinAttr>> VIEWMAP = new LinkedHashMap<>();
    private Context mContext;
    private Resources mResources;
    private String mPkgName;
    private SkinManager() {
    }

    public static final WeakHashMap<String, ISkinDelegate> DELEGATEMAP = new WeakHashMap<>();
    static {
        DELEGATEMAP.put(SkinConstants.TEXTCOLOR,new TextColorDelegate());
        DELEGATEMAP.put(SkinConstants.BACKGROUND,new BackgroundDelegate());
        DELEGATEMAP.put(SkinConstants.SRC,new SrcDelegate());
    }

    private static class Holder {
        static SkinManager HODLER = new SkinManager();
    }


    public static SkinManager get() {
        return Holder.HODLER;
    }

    public SkinManager config(Context context){
        mContext = context.getApplicationContext();
        return this;
    }


    public void saveViewAttr(View view, HashMap<String, SkinAttr> map, ConfigBean bean) {
        if (view == null || map == null || map.isEmpty()) {
            return;
        }

        if (bean.isLoadNow) {
            if (mResources == null) {
                mResources = loadSkinPath(bean.skinPath);
                mPkgName = ZUtils.getPkgName(view.getContext(),bean.skinPath);
            }

            doAttrsToView(view,map);
        }
        //保存下次调用
        Map<String, SkinAttr> attrMap = VIEWMAP.get(view);
        if (attrMap == null) {
            VIEWMAP.put(view,map);
        }

    }

    public void loadSkin(String skinPath){
        File file = new File(skinPath);
        if (!file.exists()) {
            //todo 添加一个监听
            Toast.makeText(mContext, "skinPath don't exist: "+skinPath, Toast.LENGTH_SHORT).show();
            return;
        }

        mResources = loadSkinPath(skinPath);

        //todo 通知所有的view
        mPkgName = ZUtils.getPkgName(mContext, skinPath);

        notifyChangeSkin();

    }


    private void notifyChangeSkin(){
        Set<Map.Entry<View, Map<String, SkinAttr>>> entries =
                VIEWMAP.entrySet();
        for (Map.Entry<View, Map<String, SkinAttr>> mapEntry : entries) {
            View view = mapEntry.getKey();
            Map<String, SkinAttr> value = mapEntry.getValue();

            //使用map+策略模式
            doAttrsToView(view,value);
        }
    }

    /**
     * 把属性填充到view中
     * @param view
     * @param value
     */
    private void doAttrsToView(View view, Map<String, SkinAttr> value) {
        if (mPkgName != null) {
            Set<Map.Entry<String, SkinAttr>> entries = value.entrySet();
            for (Map.Entry<String, SkinAttr> attrEntry : entries) {
                String key = attrEntry.getKey();
                SkinAttr attr = attrEntry.getValue();
                ISkinDelegate delegate = DELEGATEMAP.get(key);
                if (delegate != null) {
                    delegate.apply(view,attr,getOutResource(),mPkgName);
                }

            }
        }

    }


    private Resources getOutResource(){

        return mResources;
    }

    private Resources loadSkinPath(String skinPath){
        try {
            //拿到资源加载器

            AssetManager assetManager = AssetManager.class.newInstance();

            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager,skinPath);



           return mResources =  new Resources(assetManager,mContext.getResources().getDisplayMetrics()
                    ,mContext.getResources().getConfiguration());



        } catch (Exception e) {
            LggUtils.e("SkinManager - loadSkinPath error: "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
