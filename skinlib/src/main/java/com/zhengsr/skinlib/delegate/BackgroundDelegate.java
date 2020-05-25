package com.zhengsr.skinlib.delegate;

import android.view.View;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.entity.SkinAttr;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class BackgroundDelegate extends ISkinDelegate<View> {
    private final String COLOR = "color";



    @Override
    public void onApply(View view, SkinAttr attr) {
        if (getResId(attr)!=0) {
            if (COLOR.equals(attr.typeName)){
                view.setBackgroundColor(getColor(attr));
            }else{
                view.setBackgroundDrawable(getDrawable(attr));
            }
        }
    }
}
