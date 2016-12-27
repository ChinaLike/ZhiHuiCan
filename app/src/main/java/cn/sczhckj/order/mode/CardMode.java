package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe: 会员卡数据请求
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class CardMode {


    /**
     * 优惠信息列表
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void info(RequestCommonBean bean, Callback<Bean<CardInfoBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.CARD_INFO)
                .time()
                .bean(bean);
        Call<Bean<CardInfoBean>> billCall = RetrofitRequest.service().cardInfo(restRequest.toRequestString());
        billCall.enqueue(callback);
    }

    /**
     * 用户办卡申请
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void apply(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.CARD_APPLY)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> billCall = RetrofitRequest.service().apply(restRequest.toRequestString());
        billCall.enqueue(callback);
    }

}
