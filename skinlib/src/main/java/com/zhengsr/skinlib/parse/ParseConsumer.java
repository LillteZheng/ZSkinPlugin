package com.zhengsr.skinlib.parse;

import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.zhengsr.skinlib.emtity.SkinAttr;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 解析 attribute 属性，并返回列表
 */
public class ParseConsumer {

    public static HashMap<String, SkinAttr> parseSkinAttr(@NonNull View view, AttributeSet attrs,  String... tag){
        int count = attrs.getAttributeCount();

        HashMap<String,SkinAttr> viewAttrs = new LinkedHashMap<>();
        //拿到所有属性
        //todo 处理 style 类型，避免theme的数据被覆盖

        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);


            //先判断是否有 tag 标签,如果tag为null，则解析所有标签
            //如果tag有数据，则过滤不是 tag标签里面的数据
            if (!filterTags(attrName,tag)){
                continue;
            }

            //比如 @color/white , 拿到的数据时 @xxx 一串数值
            if (attrValue.startsWith("@")) {

                SkinAttr attr = new SkinAttr();
                attr.attrValueId = Integer.valueOf(attrValue.substring(1));
                attr.entryName = view.getContext().getResources().getResourceEntryName(attr.attrValueId);
                attr.typeName = view.getContext().getResources().getResourceTypeName(attr.attrValueId);
                attr.attrName = attrName;
                viewAttrs.put(attrName,attr);

            }
        }

        return viewAttrs;
    }


    /**
     * 是否需要过滤tag标签
     * @param name
     * @param tag
     * @return
     */
    private static boolean filterTags(String name, String... tag){
        //如果tag为null，也认为它是不需要过滤的
        if (tag == null || tag.length == 0){
            return true;
        }
        for (String s : tag) {
            if (s.trim().equals(name)){
                return true;
            }
        }
        return false;
    }
}
