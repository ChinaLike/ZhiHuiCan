package cn.sczhckj.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 结账界面--优惠类型
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class FavorableTypeBean implements Serializable {

    private int id;

    private String name;

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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
