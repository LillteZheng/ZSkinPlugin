package com.zhengsr.skinlib.entity;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
public class SkinAttr {
    /**
     * view 的属性
     */
    public String attrName;

    /**
     * 属性的 reference，比如 R.mipmap.ic_launcher 对应的 R id 值
     */
    public int attrValueId;

    /**
     * reference id 对应的名字，比如 R.mipmap.ic_launcher 中的 ic_launcher
     */
    public String entryName;

    /**
     * reference 对应的类型，比如 R.mipmap.ic_launcher 中的 mipmap
     */
    public String typeName;

    public SkinAttr(String attrName, int attrValueId, String entryName, String typeName) {
        this.attrName = attrName;
        this.attrValueId = attrValueId;
        this.entryName = entryName;
        this.typeName = typeName;
    }

    public SkinAttr() {
    }

    @Override
    public String toString() {
        return "SkinAttr{" +
                "attrName='" + attrName + '\'' +
                ", attrValueId='" + attrValueId + '\'' +
                ", entryName='" + entryName + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
