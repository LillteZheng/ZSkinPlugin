package com.zhengsr.skinlib.parse;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.emtity.SkinAttr;
import com.zhengsr.skinlib.utils.LggUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class SrcDelegate implements ISkinDelegate<ImageView> {
    @Override
    public void apply(ImageView view, SkinAttr attr, Resources resources, String pkgName) {
        int res = resources.getIdentifier(attr.entryName,attr.typeName,pkgName);
        if (res != 0){
            Drawable drawable = resources.getDrawable(res);
            view.setImageDrawable(drawable);
        }
    }
}
