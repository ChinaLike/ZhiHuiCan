package cn.sczhckj.order.mode;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.produce.TableAttrBean;
import cn.sczhckj.order.data.bean.produce.TableBean;
import cn.sczhckj.order.data.bean.produce.TableCateBean;
import cn.sczhckj.order.data.bean.produce.WaitressBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @ describe: 服务员模式
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class ProduceMode {

    /**
     * 服务员登录
     *
     * @param bean
     * @param cllback
     */
    public void waitressLogin(RequestCommonBean bean, Callback<Bean<WaitressBean>> cllback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.PRODUCE_WAITRESSLOGIN)
                .time()
                .bean(bean);
        Call<Bean<WaitressBean>> call = RetrofitRequest.service().waitressLogin(restRequest.toRequestString());
        call.enqueue(cllback);
    }

    /**
     * 获取台桌分类
     *
     * @param bean
     * @param cllback
     */
    public void tableCate(RequestCommonBean bean, Callback<Bean<List<TableCateBean>>> cllback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.PRODUCE_TABLECATE)
                .time()
                .bean(bean);
        Call<Bean<List<TableCateBean>>> call = RetrofitRequest.service().tableCate(restRequest.toRequestString());
        call.enqueue(cllback);
    }

    /**
     * 获取台桌属性
     *
     * @param bean
     * @param cllback
     */
    public void tableAttr(RequestCommonBean bean, Callback<Bean<List<TableAttrBean>>> cllback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.PRODUCE_TABLEATTR)
                .time()
                .bean(bean);
        Call<Bean<List<TableAttrBean>>> call = RetrofitRequest.service().tableAttr(restRequest.toRequestString());
        call.enqueue(cllback);
    }

    /**
     * 获取所有台桌
     *
     * @param bean
     * @param cllback
     */
    public void allTable(RequestCommonBean bean, Callback<Bean<List<TableBean>>> cllback) {
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.PRODUCE_ALL_TABLE)
                .time()
                .bean(bean);
        Call<Bean<List<TableBean>>> call = RetrofitRequest.service().allTable(restRequest.toRequestString());
        call.enqueue(cllback);
    }

}
