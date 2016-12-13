package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.CateBean;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @describe: 菜品数据请求
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class FoodMode {

    /**
     * 获取菜品分类
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void cates(RequestCommonBean bean, Callback<Bean<CateBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_CATE)
                .time()
                .bean(bean);
        Call<Bean<CateBean>> catesCall = RetrofitRequest.service().cates(restRequest.toRequestString());
        catesCall.enqueue(callback);
    }

    /**
     * 获取菜品列表
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void foods(RequestCommonBean bean, Callback<Bean<List<FoodBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_LIST)
                .time()
                .bean(bean);
        Call<Bean<List<FoodBean>>> foodsCall = RetrofitRequest.service().foods(restRequest.toRequestString());
        foodsCall.enqueue(callback);
    }

}
