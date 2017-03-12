package cn.sczhckj.order.mode;

import android.content.Context;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @describe: 开桌信息
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class TableMode {

    /**
     * 初始化台桌信息
     *
     * @param context     参数对象
     * @param callback 回调
     */
    public void tableInit(Context context, Callback<Bean<TableBean>> callback) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(context));
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_INIT)
                .time()
                .bean(bean);
        Call<Bean<TableBean>> openInfo = RetrofitRequest.service().tableInit(restRequest.toRequestString());
        openInfo.enqueue(callback);
    }

    /**
     * 获取开桌信息
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void openInfo(RequestCommonBean bean, Callback<Bean<TableBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_OPEN_INFO)
                .time()
                .bean(bean);
        Call<Bean<TableBean>> openInfo = RetrofitRequest.service().openInfo(restRequest.toRequestString());
        openInfo.enqueue(callback);
    }

    /**
     * 获取台桌信息
     * @param bean
     * @param callback
     */
    public void info(RequestCommonBean bean, Callback<Bean<List<InfoBean>>> callback){
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_INFO)
                .time()
                .bean(bean);
        Call<Bean<List<InfoBean>>> info=RetrofitRequest.service().tableInfo(restRequest.toRequestString());
        info.enqueue(callback);
    }

    /**
     * 开桌
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void open(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_OPEN)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> commonCall = RetrofitRequest.service().open(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }

    /**
     * 设置台桌人数
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void setPersonNum(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_SET_PERSON_NUM)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> commonCall = RetrofitRequest.service().setPersonNum(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }

    /**
     * 台桌状态切换
     * @param bean
     * @param callback
     */
    public void switchStatus(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback){
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_SWITCH_STATUS)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> commonCall = RetrofitRequest.service().switchStatus(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }

}
