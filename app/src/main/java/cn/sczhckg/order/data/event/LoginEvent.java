package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.UserLoginBean;

/**
 * @describe: 登录信息
 * @author: Like on 2016/11/17.
 * @Email: 572919350@qq.com
 */

public class LoginEvent {

    public final static int LOGIN_SUCCESS=0;
    public final static int LOGIN_FAIL=1;

    private int flag;

    private UserLoginBean bean;

    public LoginEvent(int flag, UserLoginBean bean) {
        this.flag = flag;
        this.bean = bean;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public UserLoginBean getBean() {
        return bean;
    }

    public void setBean(UserLoginBean bean) {
        this.bean = bean;
    }
}
