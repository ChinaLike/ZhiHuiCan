package cn.sczhckg.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 通用数据
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class UserLoginBean implements Serializable {

    private String id;//用户ID
    private String name;//用户名字
    private String password;//用户密码
    private String url;//用户头像地址
    private int vip;//用户VIP等级
    private String table;//如果主桌已经登录，返回主桌桌号
    private String vipUrl;//会员身份标识地址
    private String msg;//消息提醒

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getVipUrl() {
        return vipUrl;
    }

    public void setVipUrl(String vipUrl) {
        this.vipUrl = vipUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", vip=" + vip +
                ", table='" + table + '\'' +
                ", vipUrl='" + vipUrl + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
