package cn.sczhckj.order.mode;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.bean.eval.EvalBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe: 评价数据请求
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class EvalMode {

    /**
     * 获取评价信息
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void evalInfo(RequestCommonBean bean, Callback<Bean<EvalBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.EVAL_INFO)
                .time()
                .bean(bean);
        Call<Bean<EvalBean>> billCall = RetrofitRequest.service().evalInfo(restRequest.toRequestString());
        billCall.enqueue(callback);
    }

    /**
     * 提交评价
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void evalCommit(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.EVAL_COMMIT)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> billCall = RetrofitRequest.service().evalCommit(restRequest.toRequestString());
        billCall.enqueue(callback);
    }
}
