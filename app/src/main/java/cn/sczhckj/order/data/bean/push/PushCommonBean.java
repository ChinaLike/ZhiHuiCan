package cn.sczhckj.order.data.bean.push;

import java.util.List;

import cn.sczhckj.order.data.bean.user.MemberBean;

/**
 * @ describe:  WebSocket通用Bean
 * @ author: Like on 2016/12/21.
 * @ email: 572919350@qq.com
 */

public class PushCommonBean {


    private String message;//消息内容

    private Integer status;//台桌状态

    private List<ArriveBean> food;//推送指定菜品刷新

    private MemberBean user;//用户信息

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ArriveBean> getFood() {
        return food;
    }

    public void setFood(List<ArriveBean> food) {
        this.food = food;
    }

    public MemberBean getUser() {
        return user;
    }

    public void setUser(MemberBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", food=" + food +
                ", user=" + user +
                '}';
    }
}
