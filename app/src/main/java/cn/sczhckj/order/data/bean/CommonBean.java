package cn.sczhckj.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 通用数据
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class CommonBean implements Serializable {

    private int status;//返回字节码

    private String msg;//错误信息

    private int showType;//显示点菜方式

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
