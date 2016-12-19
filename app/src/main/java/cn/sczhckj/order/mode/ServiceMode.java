package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.service.ServiceBean;
import cn.sczhckj.order.data.bean.table.OpenInfoBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe:  服务请求
 * @ author: Like on 2016/12/19.
 * @ email: 572919350@qq.com
 */

public class ServiceMode {

    /**
     * 获取服务类型列表
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void services(RequestCommonBean bean, Callback<Bean<List<ServiceBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.SERVICE_SERVICES)
                .time()
                .bean(bean);
        Call<Bean<List<ServiceBean>>> serviceCall = RetrofitRequest.service().services(restRequest.toRequestString());
        serviceCall.enqueue(callback);
    }

    /**
     * 呼叫服务
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void call(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.SERVICE_CALL)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> serviceCall = RetrofitRequest.service().call(restRequest.toRequestString());
        serviceCall.enqueue(callback);
    }

    /**
     * 取消服务
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void abort(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.SERVICE_ABORT)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> serviceCall = RetrofitRequest.service().abort(restRequest.toRequestString());
        serviceCall.enqueue(callback);
    }

}
