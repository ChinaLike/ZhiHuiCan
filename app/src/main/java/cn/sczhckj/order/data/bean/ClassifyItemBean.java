package cn.sczhckj.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 分类子项
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class ClassifyItemBean implements Serializable {

    private boolean isSelect;//是否被标记

    private int id;

    private String name;

    private int permiss;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermiss() {
        return permiss;
    }

    public void setPermiss(int permiss) {
        this.permiss = permiss;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "{" +
                "isSelect=" + isSelect +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", permiss=" + permiss +
                ", url='" + url + '\'' +
                '}';
    }
}
