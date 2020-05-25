package com.zhengsr.skinlib.callback;

import android.content.res.Resources;
import android.view.View;

import com.zhengsr.skinlib.entity.SkinAttr;

import java.util.Map;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public abstract class ICusSkinDelegate<T extends View> extends IBaseSkinDelegate<T, Map<String,SkinAttr>> {
    @Override
    public void apply(T view, Map<String, SkinAttr> attr, Resources outResource, String pkgName) {
        super.apply(view, attr, outResource, pkgName);
        onApply(view,attr);
    }

    public abstract void onApply(T view,Map<String, SkinAttr> maps);
}
