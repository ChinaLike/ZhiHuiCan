package cn.sczhckj.order.data.network;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.ClassifyBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.DetailsBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.EvaluateBean;
import cn.sczhckj.order.data.bean.GrouponBean;
import cn.sczhckj.order.data.bean.food.ImageBean;
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.data.bean.service.ServiceBean;
import cn.sczhckj.order.data.bean.table.OpenInfoBean;
import cn.sczhckj.order.data.bean.QRCodeBean;
import cn.sczhckj.order.data.bean.SettleAccountsBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.bean.UserLoginBean;
import cn.sczhckj.order.data.bean.VersionBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @describe: 接口定义
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface RetrofitService {

    /**
     * 开桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/openInfo")
    Call<Bean<OpenInfoBean>> openInfo(@Field("p") String p);

    /**
     * 获取台桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/info")
    Call<Bean<InfoBean>> tableInfo(@Field("p") String p);

    /**
     * 开桌
     */
    @FormUrlEncoded
    @POST("rest/table/open")
    Call<Bean<ResponseCommonBean>> open(@Field("p") String p);

    /**
     * 获取台桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/setPersonNum")
    Call<Bean<ResponseCommonBean>> setPersonNum(@Field("p") String p);

    /**
     * 菜品分类
     */
    @FormUrlEncoded
    @POST("rest/food/cates")
    Call<Bean<CateBean>> cates(@Field("p") String p);

    /**
     * 菜品列表
     */
    @FormUrlEncoded
    @POST("rest/food/foods")
    Call<Bean<List<FoodBean>>> foods(@Field("p") String p);

    /**
     * 菜品轮播图片
     */
    @FormUrlEncoded
    @POST("rest/food/images")
    Call<Bean<List<ImageBean>>> images(@Field("p") String p);

    /**
     * 菜品详细信息
     */
    @FormUrlEncoded
    @POST("rest/food/info")
    Call<Bean<FoodBean>> foodInfo(@Field("p") String p);

    /**
     * 菜品价格列表
     */
    @FormUrlEncoded
    @POST("rest/food/prices")
    Call<Bean<List<PriceBean>>> prices(@Field("p") String p);

    /**
     * 退菜
     */
    @FormUrlEncoded
    @POST("rest/food/refund")
    Call<Bean<ResponseCommonBean>> refund(@Field("p") String p);

    /**
     * 点赞
     */
    @FormUrlEncoded
    @POST("rest/food/favor")
    Call<Bean<ResponseCommonBean>> favor(@Field("p") String p);

    /**
     * 购物车数据提交
     */
    @FormUrlEncoded
    @POST("rest/order/order")
    Call<Bean<ResponseCommonBean>> order(@Field("p") String p);

    /**
     * 服务列表
     */
    @FormUrlEncoded
    @POST("rest/service/services")
    Call<Bean<List<ServiceBean>>> services(@Field("p") String p);

    /**
     * 呼叫服务
     */
    @FormUrlEncoded
    @POST("rest/service/call")
    Call<Bean<ResponseCommonBean>> call(@Field("p") String p);

    /**
     * 取消服务
     */
    @FormUrlEncoded
    @POST("rest/service/abort")
    Call<Bean<ResponseCommonBean>> abort(@Field("p") String p);

    /**
     * 获取结账清单
     */
    @FormUrlEncoded
    @POST("rest/bill/bill")
    Call<Bean<List<BillBean>>> bill(@Field("p") String p);





    /**
     * 版本管理信息
     */
    @FormUrlEncoded
    @POST("rest/device/version")
    Call<Bean<VersionBean>> version(@Field("params") String params);

    /**
     * 会员登录
     */
    @FormUrlEncoded
    @POST("rest/user/login")
    Call<Bean<UserLoginBean>> login(@Field("params") String params);

    /**
     * 获取短信验证码
     */
    @FormUrlEncoded
    @POST("rest/user/smsAuthCode")
    Call<Bean<Integer>> smsAuthCode(@Field("params") String params);

    /**
     * 购物车数据提交
     */
    @FormUrlEncoded
    @POST("rest/openTable/openTableVerify")
    Call<Bean<ResponseCommonBean>> openTable(@Field("params") String params);

    /**
     * 消费者选择点菜方式后请求分类列表
     */
    @FormUrlEncoded
    @POST("rest/order/claaify")
    Call<Bean<ClassifyBean>> classify(@Field("params") String params);

    /**
     * 菜品信息
     */
    @FormUrlEncoded
    @POST("rest/order/dishesList")
    Call<Bean<List<FoodBean>>> dishes(@Field("params") String params);

    /**
     * 菜品详情
     */
    @FormUrlEncoded
    @POST("rest/order/deatails")
    Call<Bean<DetailsBean>> dishesDeatails(@Field("params") String params);

    /**
     * 结账清单数据请求
     */
    @FormUrlEncoded
    @POST("rest/accounts/accountsList")
    Call<Bean<SettleAccountsBean>> settleAccountsList(@Field("params") String params);

    /**
     * 团购券验证
     */
    @FormUrlEncoded
    @POST("rest/accounts/accountsGroupon")
    Call<Bean<GrouponBean>> verifyGroup(@Field("params") String params);

    /**
     * 结账
     */
    @FormUrlEncoded
    @POST("rest/accounts/accountsPay")
    Call<Bean<QRCodeBean>> pay(@Field("params") String params);

    /**
     * 评价请求数据
     */
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluateShow")
    Call<Bean<List<EvaluateBean>>> getEvaluate(@Field("params") String params);

    /**
     * 评价发送数据
     */
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluate")
    Call<Bean<ResponseCommonBean>> postEvaluate(@Field("params") String params);

    /**
     * 服务呼叫
     */
    @FormUrlEncoded
    @POST("rest/sercive")
    Call<Bean<ResponseCommonBean>> service(@Field("params") String params);

    /**
     * 申请办理VIP
     */
    @FormUrlEncoded
    @POST("rest/user/applyForVip")
    Call<Bean<ResponseCommonBean>> applyForVip(@Field("params") String params);

    /**
     * 刷新购物车菜品
     */
    @FormUrlEncoded
    @POST("rest/refresh/cartDishes")
    Call<Bean<List<FoodBean>>> refreshCartDishes(@Field("params") String params);

    /**
     * 刷新二维码是否验证成功
     */
    @FormUrlEncoded
    @POST("rest/refresh/QRCodeVerify")
    Call<Bean<ResponseCommonBean>> refreshQRCodeIsVerify(@Field("params") String params);

    /**
     * 优惠类型验证
     */
    @FormUrlEncoded
    @POST("rest/accounts/favorableTypeVerify")
    Call<Bean<SettleAccountsBean>> favorableTypeVerify(@Field("params") String params);

}
