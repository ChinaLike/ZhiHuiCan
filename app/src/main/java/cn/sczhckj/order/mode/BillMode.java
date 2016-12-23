package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe:  结账
 * @ author: Like on 2016/12/19.
 * @ email: 572919350@qq.com
 */

public class BillMode {

    /**
     * 获取结账清单
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void bill(RequestCommonBean bean, Callback<Bean<List<BillBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.BILL_BILL)
                .time()
                .bean(bean);
        Call<Bean<List<BillBean>>> billCall = RetrofitRequest.service().bill(restRequest.toRequestString());
        billCall.enqueue(callback);
    }

}
