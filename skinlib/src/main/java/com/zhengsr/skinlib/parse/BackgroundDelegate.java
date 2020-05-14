package com.zhengsr.skinlib.parse;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.emtity.SkinAttr;
import com.zhengsr.skinlib.utils.LggUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class BackgroundDelegate implements ISkinDelegate<View> {
    private final String COLOR = "color";

    @Override
    public void apply(View view, SkinAttr attr, Resources resources, String pkgName) {
        int res = resources.getIdentifier(attr.entryName,attr.typeName,pkgName);
        if (res!=0) {
            if (COLOR.equals(attr.typeName)){
                view.setBackgroundColor(resources.getColor(res));
            }else{
                //如果此时 res 能拿到，直接用R.java 的值即可
                view.setBackground(resources.getDrawable(res));
            }
        }
    }
}
