package cn.sczhckg.order.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @describe: Retrofit数据请求封装类，包含GET，POST
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class RetrofitRequest {

    /**
     * GET 请求
     * @param url 请求地址
     */
    public static RetrofitService GET(String url){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }

}
