# ZSkinPlugin
一键换肤，低入侵，享受换肤快乐

## 一、关联
```
allprojects {
    repositories {
       ...
        maven { url 'https://jitpack.io' }
        
    }
}
```
```
implementation 'com.github.LillteZheng:ZSkinPlugin:v1.0'
```


**androidx 的话直接关联即可**

效果如下：

<img src="https://github.com/LillteZheng/ZSkinPlugin/raw/master/pic/skin.png"  width="410" height="660">



## 二、制作皮肤包

ZSkinPlugin 目前支持应用内换肤和插件换肤。
这里介绍插件换肤的制作。新建一个moudle，不需要 activity；然后再 res 相同的文件夹下，导入你的皮肤资源，比如：


<img src="https://github.com/LillteZheng/ZSkinPlugin/raw/master/pic/skin.png"  width="480" height="510">

然后生成apk包即可，为了避免冲突，建议对 apk 命名成 x.skin ，然后放到指定的路径下即可。


## 三、如何使用
目前来看，ZSkinPlugin 不需要你做额外的操作，只需要你在 xml 中设置 以下属性

- 在 xml 添加命名空间，xmlns:skin="http://schemas.android.com/android/skin"
- 在需要换肤的控件中，添加 skin:enable = "true"，这样才会支持换肤;

如下：
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:skin="http://schemas.android.com/android/skin"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    ....
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        skin:enable = "true"
        skin:tag="background"
        android:text="test"
        android:textColor="@color/colorPrimaryDark"
        android:background="@mipmap/skin_bg"/>

</LinearLayout>
```

其中为什么要加   skin:tag="src"  呢？如果不加 tag，则工具会遍历所有的属性，比如 textColor;所以，这里我们只需要
指定更新背景 background 即可。如果不写，当也检测到 colorPrimaryDark 资源，也会替换掉  textColor。


### 3.1 动态替换

比如一个 dialog，用 recyclerview 去加载空间，由于不在 xml 中配置的，那肯定是不行，所以这里也支持动态替换。
比如：
```
//前提你已经配置了 ZSkin.loadSkin(file.getAbsolutePath());
if (ZSkin.isLoadSkin()){
    data.drawable = ZSkin.getDrawable(RESID[i]);
}
```

### 3.2 自定义控件

比如，我有一个自定义控件，那怎么替换呢？很简单，xml 也使用  skin:enable="true" 去配置，比如：

```
<com.hk.sharewhitebroad.view.BottomItemView
    android:id="@+id/bottom_pen"
    android:layout_width="@dimen/bottom_img_size"
    android:layout_height="match_parent"
    app:bt_img_height="@dimen/bottom_img_height"
    app:bt_img_src="@mipmap/bottom_tool_write_laser_white"
    app:bt_img_width="@dimen/bottom_img_width"
    app:bt_txt_text="@string/pen"
    skin:enable="true"
    skin:tag="bt_img_src" />

```
然后我们需要先在 application 或者 onCreate 之前配置 delegate，比如：

```
//添加自定义view
ZSkin.addDelegate(BottomItemView.class,new BottomImgDelegate());

```
其实，BottomImgDelegate 实现也很简单，就是集成 ICusSkinDelegate：

```
public class BottomImgDelegate extends ICusSkinDelegate<BottomItemView> {
    private static final String TAG = "BottomImgDelegate";
    
    @Override
    public void onApply(BottomItemView view, Map<String, SkinAttr> maps) {
        Set<Map.Entry<String, SkinAttr>> entrySet = maps.entrySet();
        for (Map.Entry<String, SkinAttr> entry : entrySet) {
            String key = entry.getKey();
            SkinAttr attr = entry.getValue();
            if (getResId(attr) != 0) {
                if ("bt_img_src".equals(key)){
                    //替换皮肤的
                    view.updateImg(getDrawable(attr));
                }
            }

        }
    }
}

```

如果你想一开始就替换皮肤包，想我这样配置也可以：
```

String path =  getFilesDir().getAbsolutePath()
String name = "skin_2k.skin";
String assetName = "skin/skin_2k.skin";
//直接改变了
if (!DrawConfig.isV811()) {

    File file = new File(path,name);
    //如果不存在，则从 assets copy 过去
    if (!file.exists()) {
        ZUtils.copyAssetFileToStorage(this,assetName,path,name);
    }

    //添加自定义view
    ZSkin.addDelegate(BottomItemView.class,new BottomImgDelegate());
    ZSkin.addDelegate(CenterImageView.class,new CenterImageDelegate());
    //立刻加载
    ZSkin.loadSkin(file.getAbsolutePath());

}
```