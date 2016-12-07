package cn.sczhckj.order.data.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @describe: 下载接口
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public interface DownloadApi {

    @GET
    Call<ResponseBody> retrofitDownload(@Url String url);

}
