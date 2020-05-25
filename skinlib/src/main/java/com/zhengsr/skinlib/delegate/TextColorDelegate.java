package com.zhengsr.skinlib.delegate;

import android.widget.TextView;

import com.zhengsr.skinlib.callback.ISkinDelegate;
import com.zhengsr.skinlib.entity.SkinAttr;
import com.zhengsr.skinlib.utils.LggUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class TextColorDelegate extends ISkinDelegate<TextView> {


    @Override
    public void onApply(TextView view, SkinAttr attr) {
        if (getResId(attr)!=0) {
            LggUtils.d("TextColorDelegate - onApply: ");
            view.setTextColor(getColor(attr));
        }
    }
}
