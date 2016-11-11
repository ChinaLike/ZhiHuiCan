package cn.sczhckg.order.data.network;

import java.util.List;
import java.util.Map;

import cn.sczhckg.order.data.bean.ClassifyBean;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.MainPagerShow;
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
    /**菜品选择好后数据提交*/
    @FormUrlEncoded
    @POST("android/test/cart/order")
    Call<CommonBean> chooseGood(@Field("table") String table, @Field("orderType") int orderType,@Field("type") int type,@Field("params") String params);

}
