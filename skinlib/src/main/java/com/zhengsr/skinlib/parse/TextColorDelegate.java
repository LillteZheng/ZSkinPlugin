package com.zhengsr.skinlib.parse;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.emtity.SkinAttr;

import java.util.HashMap;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class TextColorDelegate implements ISkinDelegate<TextView> {


    @Override
    public void apply(TextView view, SkinAttr attr, Resources resources, String pkgName) {
        int res = resources.getIdentifier(attr.entryName,attr.typeName,pkgName);
        if (res!=0) {
            int color = resources.getColor(res);
            view.setTextColor(color);
        }
    }
}
