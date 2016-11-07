package cn.sczhckg.order.data.network;

import java.util.Map;

import cn.sczhckg.order.data.bean.UserLoginBean;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface RetrofitService {

    @GET("android/test/login")
    Call<String> vip(@QueryMap Map<String, String> options);

}
