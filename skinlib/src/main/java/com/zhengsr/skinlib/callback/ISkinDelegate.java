package com.zhengsr.skinlib.callback;

import android.content.res.Resources;
import android.view.View;

import com.zhengsr.skinlib.emtity.SkinAttr;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public interface ISkinDelegate<T extends View> {

    void apply(T view, SkinAttr attr, Resources outResource, String pkgName);
}
