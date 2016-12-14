package cn.sczhckj.order.mode;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.CommonBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @describe: 订单数据请求或获取
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class OrderMode {


    /**
     * 获取菜品分类
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void order(RequestCommonBean bean, Callback<Bean<CommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ORDER_ORDER)
                .time()
                .bean(bean);
        Call<Bean<CommonBean>> commonCall = RetrofitRequest.service().order(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }

}
