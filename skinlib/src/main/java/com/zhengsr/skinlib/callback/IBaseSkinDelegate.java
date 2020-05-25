package com.zhengsr.skinlib.callback;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.zhengsr.skinlib.entity.SkinAttr;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public abstract class IBaseSkinDelegate<T extends View,E> {

    protected Resources resource;
    protected String pkgName;

    public void apply(T view, E attr, Resources resources, String pkgName){
        this.resource = resources;
        this.pkgName = pkgName;
    }


    public int getResId(SkinAttr attr){
        if (resource != null) {
            return resource.getIdentifier(attr.entryName,attr.typeName,pkgName);
        }
        return 0;
    }

    public int getColor(SkinAttr attr){
        int resId =  resource.getIdentifier(attr.entryName,attr.typeName,pkgName);
        if (resId != 0){
            return resource.getColor(resId);
        }
        return 0;
    }

    public Drawable getDrawable(SkinAttr attr){
        int resId =  resource.getIdentifier(attr.entryName,attr.typeName,pkgName);
        if (resId != 0){
            return resource.getDrawable(resId);
        }
        return null;
    }


}
