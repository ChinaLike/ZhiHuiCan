package cn.sczhckj.order.data.bean.push;

import cn.sczhckj.order.data.bean.user.MemberBean;

/**
 * @ describe:  WebSocket通用Bean
 * @ author: Like on 2016/12/21.
 * @ email: 572919350@qq.com
 */

public class PushCommonBean {

    private Integer id;//菜品ID
    private Integer cateId;//分类ID
    private Integer arriveCount;//上菜数量

    private MemberBean user;//用户信息

    private String message;//消息内容

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(Integer arriveCount) {
        this.arriveCount = arriveCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                "id=" + id +
                ", cateId=" + cateId +
                ", arriveCount=" + arriveCount +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}
