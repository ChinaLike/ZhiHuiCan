package cn.sczhckg.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 通用数据
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class CommonBean implements Serializable{

    private int code;//返回字节码

    private String msg;//错误信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CommonBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
