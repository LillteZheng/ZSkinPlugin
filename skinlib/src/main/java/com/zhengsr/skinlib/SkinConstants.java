package com.zhengsr.skinlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkinConstants {
    /***
     * 支持的命名空间,自定义一个，方便获取对应的数据
     */
    public static final String SKIN_XML_NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String SKIN_ATTRS = "enable";
    public static final String SKIN_TAG = "tag";


    /**
     * 支持的类型
     */

    public static final String TEXTCOLOR = "textColor";
    public static final String BACKGROUND = "background";
    public static final String SRC = "src";

    static List<String> DEFAULTTYPE = Arrays.asList(TEXTCOLOR,BACKGROUND,SRC);

    /**
     * sharepreference key
     */
    public static final String KEY_LOAD_NOW = "KEY_LOAD_NOW";
    public static final String KEY_PKG_NAME = "KEY_PKG_NAME";
    public static final String KEY_PLUGIN_PATH = "KEY_PLUGIN_PATH";

}
