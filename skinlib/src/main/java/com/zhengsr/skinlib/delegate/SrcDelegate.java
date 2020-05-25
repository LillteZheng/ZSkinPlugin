package com.zhengsr.skinlib.delegate;

import android.widget.ImageView;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.entity.SkinAttr;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class SrcDelegate extends ISkinDelegate<ImageView> {


    @Override
    public void onApply(ImageView view, SkinAttr attr) {
        if (getResId(attr) != 0){
            view.setImageDrawable(getDrawable(attr));
        }
    }
}
