package cn.sczhckg.order.data.network;

import cn.sczhckg.order.Config;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @describe: Retrofit数据请求封装类，包含GET，POST
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class RetrofitRequest {

    /**
     * service 请求
     */
    public static RetrofitService service(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.HOST)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }

}
