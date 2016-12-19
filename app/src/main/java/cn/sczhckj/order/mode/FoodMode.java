package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.food.ImageBean;
import cn.sczhckj.order.data.bean.food.PriceBean;
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

    /**
     * 获取菜品详细信息
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void info(RequestCommonBean bean, Callback<Bean<FoodBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_INFO)
                .time()
                .bean(bean);
        Call<Bean<FoodBean>> foodsCall = RetrofitRequest.service().foodInfo(restRequest.toRequestString());
        foodsCall.enqueue(callback);
    }

    /**
     * 获取菜品轮播图片
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void images(RequestCommonBean bean, Callback<Bean<List<ImageBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_IMAGE)
                .time()
                .bean(bean);
        Call<Bean<List<ImageBean>>> imageCall = RetrofitRequest.service().images(restRequest.toRequestString());
        imageCall.enqueue(callback);
    }

    /**
     * 获取菜品价格
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void prices(RequestCommonBean bean, Callback<Bean<List<PriceBean>>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_PRICE)
                .time()
                .bean(bean);
        Call<Bean<List<PriceBean>>> imageCall = RetrofitRequest.service().prices(restRequest.toRequestString());
        imageCall.enqueue(callback);
    }

    /**
     * 退菜
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void refund(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_REFUND)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> imageCall = RetrofitRequest.service().refund(restRequest.toRequestString());
        imageCall.enqueue(callback);
    }

    /**
     * 点赞
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void favor(RequestCommonBean bean, Callback<Bean<ResponseCommonBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.FOOD_FAVOR)
                .time()
                .bean(bean);
        Call<Bean<ResponseCommonBean>> imageCall = RetrofitRequest.service().favor(restRequest.toRequestString());
        imageCall.enqueue(callback);
    }
}
