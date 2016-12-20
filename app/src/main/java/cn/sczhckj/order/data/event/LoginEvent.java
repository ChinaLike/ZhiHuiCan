package cn.sczhckj.order.data.event;

import cn.sczhckj.order.data.bean.user.MemberBean;

/**
 * @describe: 登录信息
 * @author: Like on 2016/11/17.
 * @Email: 572919350@qq.com
 */

public class LoginEvent {

    public final static int LOGIN_SUCCESS = 0;
    public final static int LOGIN_FAIL = 1;

    private int flag;

    private MemberBean bean;

    public LoginEvent(int flag, MemberBean bean) {
        this.flag = flag;
        this.bean = bean;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public MemberBean getBean() {
        return bean;
    }

    public void setBean(MemberBean bean) {
        this.bean = bean;
    }
}
