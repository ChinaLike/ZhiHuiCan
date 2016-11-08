package cn.sczhckg.order.data.network;

import java.util.Map;

import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.UserLoginBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<MainPagerShow> potDataShow(@Field("id") String id,@Field("deviceId") String deviceId);

}
