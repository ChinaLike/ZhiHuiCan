package cn.sczhckj.order.mode;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.OpenInfoBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @describe: 开桌信息
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class TableMode {

    /**
     * 获取开桌信息
     *
     * @param bean     参数对象
     * @param callback 回调
     */
    public void openInfo(RequestCommonBean bean, Callback<Bean<OpenInfoBean>> callback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.TABLE_OPEN_INFO)
                .time()
                .bean(bean);
        Call<Bean<OpenInfoBean>> openInfo = RetrofitRequest.service().openInfo(restRequest.toRequestString());
        openInfo.enqueue(callback);
    }

}
