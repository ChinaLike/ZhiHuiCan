package cn.sczhckj.order.mode;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe:  用户模块数据
 * @ author: Like on 2016/12/20.
 * @ email: 572919350@qq.com
 */

public class UserMode {

    /**
     * 用户登录
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void login(RequestCommonBean bean, Callback<Bean<MemberBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.USER_LOGIN)
                .time()
                .bean(bean);
        Call<Bean<MemberBean>> loginCall = RetrofitRequest.service().login(restRequest.toRequestString());
        loginCall.enqueue(callback);
    }

    /**
     * 发送验证码
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void sms(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.USER_SMS)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> smsCall = RetrofitRequest.service().sms(restRequest.toRequestString());
        smsCall.enqueue(callback);
    }

    /**
     * 快捷登录
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void smsLogin(RequestCommonBean bean, Callback<Bean<MemberBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.USER_SMSLOGIN)
                .time()
                .bean(bean);
        Call<Bean<MemberBean>> smsLoginCall = RetrofitRequest.service().smsLogin(restRequest.toRequestString());
        smsLoginCall.enqueue(callback);
    }

    /**
     * 用户登录
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void userInfo(RequestCommonBean bean, Callback<Bean<MemberBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.USER_INFO)
                .time()
                .bean(bean);
        Call<Bean<MemberBean>> infoCall = RetrofitRequest.service().userInfo(restRequest.toRequestString());
        infoCall.enqueue(callback);
    }

}
