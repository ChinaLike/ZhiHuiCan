package cn.sczhckg.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 团购券
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class GrouponBean implements Serializable{

    private String table;

    private int status;

    private String msg;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
