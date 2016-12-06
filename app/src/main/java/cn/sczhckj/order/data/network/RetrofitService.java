package cn.sczhckj.order.data.network;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ClassifyBean;
import cn.sczhckj.order.data.bean.CommonBean;
import cn.sczhckj.order.data.bean.DetailsBean;
import cn.sczhckj.order.data.bean.DishesBean;
import cn.sczhckj.order.data.bean.EvaluateBean;
import cn.sczhckj.order.data.bean.GrouponBean;
import cn.sczhckj.order.data.bean.MainPagerShow;
import cn.sczhckj.order.data.bean.QRCodeBean;
import cn.sczhckj.order.data.bean.SettleAccountsBean;
import cn.sczhckj.order.data.bean.UserLoginBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface RetrofitService {

    /**会员登录*/
    @FormUrlEncoded
    @POST("rest/user/login")
    Call<Bean<UserLoginBean>> login(@Field("params") String params);
    /**主页锅底选择*/
    @FormUrlEncoded
    @POST("rest/openTable/openTableData")
    Call<Bean<MainPagerShow>> potDataShow(@Field("params") String params);
    /**购物车数据提交*/
    @FormUrlEncoded
    @POST("rest/openTable/openTableVerify")
    Call<Bean<CommonBean>> openTable(@Field("params") String params);
    /**消费者选择点菜方式后请求分类列表*/
    @FormUrlEncoded
    @POST("rest/order/claaify")
    Call<Bean<ClassifyBean>> classify(@Field("params") String params);
    /**菜品信息*/
    @FormUrlEncoded
    @POST("rest/order/dishesList")
    Call<Bean<List<DishesBean>>> dishes(@Field("params") String params);
    /**菜品详情*/
    @FormUrlEncoded
    @POST("rest/order/deatails")
    Call<Bean<DetailsBean>> dishesDeatails(@Field("params") String params);
    /**结账清单数据请求*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsList")
    Call<Bean<SettleAccountsBean>> settleAccountsList(@Field("params") String params);
    /**团购券验证*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsGroupon")
    Call<Bean<GrouponBean>> verifyGroup(@Field("params") String params);
    /**结账*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsPay")
    Call<Bean<QRCodeBean>> pay(@Field("params") String params);
    /**评价请求数据*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluateShow")
    Call<Bean<List<EvaluateBean>>> getEvaluate(@Field("params") String params);
    /**评价发送数据*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluate")
    Call<Bean<CommonBean>> postEvaluate(@Field("params") String params);
    /**服务呼叫*/
    @FormUrlEncoded
    @POST("rest/sercive")
    Call<Bean<CommonBean>> service(@Field("params") String params);
    /**申请办理VIP*/
    @FormUrlEncoded
    @POST("rest/user/applyForVip")
    Call<Bean<CommonBean>> applyForVip(@Field("params") String params);
    /**刷新购物车菜品*/
    @FormUrlEncoded
    @POST("rest/refresh/cartDishes")
    Call<Bean<List<DishesBean>>> refreshCartDishes(@Field("params") String params);
    /**刷新二维码是否验证成功*/
    @FormUrlEncoded
    @POST("rest/refresh/QRCodeVerify")
    Call<Bean<CommonBean>> refreshQRCodeIsVerify(@Field("params") String params);
    /**优惠类型验证*/
    @FormUrlEncoded
    @POST("rest/accounts/favorableTypeVerify")
    Call<Bean<SettleAccountsBean>> favorableTypeVerify(@Field("params") String params);

}
