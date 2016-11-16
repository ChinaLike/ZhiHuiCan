package cn.sczhckg.order.data.network;

import java.util.List;
import java.util.Map;

import cn.sczhckg.order.data.bean.ClassifyBean;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.DetailsBean;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.EvaluateBean;
import cn.sczhckg.order.data.bean.GrouponBean;
import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.QRCodeBean;
import cn.sczhckg.order.data.bean.SettleAccountsBean;
import cn.sczhckg.order.data.bean.UserLoginBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface RetrofitService {

    /**会员登录*/
    @GET("android/test/login")
    Call<UserLoginBean> vipLogin(@QueryMap Map<String, String> options);
    /**主页锅底选择*/
    @FormUrlEncoded
    @POST("android/test/openReady")
    Call<MainPagerShow> potDataShow(@Field("id") String id, @Field("deviceId") String deviceId);
    /**请求开桌*/
    @FormUrlEncoded
    @POST("android/test/cart/open")
    Call<CommonBean> openTable(@Field("table") String table, @Field("type") int type, @Field("params") String params, @Field("person") int person);
    /**消费者选择点菜方式后请求分类列表*/
    @FormUrlEncoded
    @POST("android/test/order/classify")
    Call<ClassifyBean> classify(@Field("table") String table, @Field("type") int type);
    /**菜品信息*/
    @FormUrlEncoded
    @POST("android/test/order/dishes")
    Call<List<DishesBean>> dishes(@Field("id") int id, @Field("table") String table, @Field("type") int type);
    /**菜品详情*/
    @FormUrlEncoded
    @POST("android/test/order/show/details")
    Call<DetailsBean> dishesDeatails(@Field("id") String id, @Field("name") String name);
    /**菜品选择好后数据提交*/
    @FormUrlEncoded
    @POST("android/test/cart/order")
    Call<CommonBean> chooseGood(@Field("table") String table, @Field("orderType") int orderType,@Field("type") int type,@Field("params") String params);
    /**结账清单数据请求*/
    @FormUrlEncoded
    @POST("android/test/payTheBills/show")
    Call<SettleAccountsBean> settleAccountsList(@Field("table") String table);
    /**团购券验证*/
    @FormUrlEncoded
    @POST("android/test/payTheBills/groupon")
    Call<GrouponBean> verifyGroup(@Field("table") String table, @Field("groupon") String groupon);
    /**结账*/
    @FormUrlEncoded
    @POST("android/test/payTheBills/pay")
    Call<QRCodeBean> pay(@Field("table") String table, @Field("favorableType") int favorableType, @Field("userNmae") String userNmae, @Field("coupon") String coupon, @Field("payType") int payType, @Field("giftMoney") int giftMoney);
    /**评价请求数据*/
    @FormUrlEncoded
    @POST("android/test/payTheBills/evaluate")
    Call<EvaluateBean> getEvaluate(@Field("id") String id);
    /**评价发送数据*/
    @FormUrlEncoded
    @POST("android/test/payTheBills/evaluatePost")
    Call<CommonBean> postEvaluate(@Field("id") String id,@Field("attitude") float attitude,@Field("quality") float quality,@Field("speed") float speed,@Field("other") float other,@Field("hotword") String hotword,@Field("opinion") String opinion);
}
