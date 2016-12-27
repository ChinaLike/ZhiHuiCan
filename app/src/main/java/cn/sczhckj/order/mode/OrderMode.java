package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.constant.OP;
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
    public void order(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ORDER_ORDER)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> commonCall = RetrofitRequest.service().order(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }


    /**
     * 刷新菜品
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void refresh(RequestCommonBean bean, Callback<Bean<List<FoodBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ORDER_REFRESH)
                .time()
                .bean(bean);
        Call<Bean<List<FoodBean>>> commonCall = RetrofitRequest.service().refresh(restRequest.toRequestString());
        commonCall.enqueue(callback);
    }

}
