package cn.sczhckj.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 通用数据
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class CommonBean implements Serializable {

    private int showType;//显示点菜方式

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
