package cn.sczhckg.order.data.network;

import java.util.List;
import java.util.Map;

import cn.sczhckg.order.data.bean.Bean;
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
    @FormUrlEncoded
    @POST("rest/user/login")
    Call<Bean<UserLoginBean>> login(@Field("parames") String parames);
    /**主页锅底选择*/
    @FormUrlEncoded
    @POST("rest/openTable/openTableData")
    Call<Bean<MainPagerShow>> potDataShow(@Field("parames") String parames);
    /**购物车数据提交*/
    @FormUrlEncoded
    @POST("rest/openTable/openTableVerify")
    Call<Bean<CommonBean>> openTable(@Field("parames") String parames);
    /**消费者选择点菜方式后请求分类列表*/
    @FormUrlEncoded
    @POST("rest/order/claaify")
    Call<Bean<ClassifyBean>> classify(@Field("parames") String parames);
    /**菜品信息*/
    @FormUrlEncoded
    @POST("rest/order/dishesList")
    Call<Bean<List<DishesBean>>> dishes(@Field("parames") String parames);
    /**菜品详情*/
    @FormUrlEncoded
    @POST("rest/dishes/deatails")
    Call<Bean<DetailsBean>> dishesDeatails(@Field("parames") String parames);
    /**菜品选择好后数据提交*/

    @Deprecated
    @FormUrlEncoded
    @POST("rest/order/dishesList")
    Call<CommonBean> chooseGood(@Field("table") String table, @Field("orderType") int orderType,@Field("type") int type,@Field("params") String params);


    /**结账清单数据请求*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsList")
    Call<Bean<SettleAccountsBean>> settleAccountsList(@Field("parames") String parames);
    /**团购券验证*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsGroupon")
    Call<Bean<GrouponBean>> verifyGroup(@Field("parames") String parames);
    /**结账*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsPay")
    Call<Bean<QRCodeBean>> pay(@Field("parames") String parames);
    /**评价请求数据*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluateShow")
    Call<Bean<EvaluateBean>> getEvaluate(@Field("parames") String parames);
    /**评价发送数据*/
    @FormUrlEncoded
    @POST("rest/accounts/accountsEvaluate")
    Call<Bean<CommonBean>> postEvaluate(@Field("parames") String parames);
    /**服务呼叫*/
    @FormUrlEncoded
    @POST("rest/sercive")
    Call<Bean<CommonBean>> service(@Field("parames") String parames);
    /**申请办理VIP*/
    @FormUrlEncoded
    @POST("rest/user/applyForVip")
    Call<Bean<CommonBean>> applyForVip(@Field("parames") String parames);

}
