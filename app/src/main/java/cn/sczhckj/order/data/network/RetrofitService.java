package cn.sczhckj.order.data.network;

import java.util.List;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.bean.device.ExceptionBean;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.bean.eval.EvalBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.food.ImageBean;
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.data.bean.service.ServiceBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @describe: 接口定义
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface RetrofitService {

    /**
     * 获取台桌初始化信息
     */
    @FormUrlEncoded
    @POST("rest/table/tableInit")
    Call<Bean<TableBean>> tableInit(@Field("p") String p);

    /**
     * 开桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/openInfo")
    Call<Bean<TableBean>> openInfo(@Field("p") String p);

    /**
     * 获取台桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/info")
    Call<Bean<InfoBean>> tableInfo(@Field("p") String p);

    /**
     * 开桌
     */
    @FormUrlEncoded
    @POST("rest/table/open")
    Call<Bean<ResponseCommonBean>> open(@Field("p") String p);

    /**
     * 获取台桌信息
     */
    @FormUrlEncoded
    @POST("rest/table/setPersonNum")
    Call<Bean<ResponseCommonBean>> setPersonNum(@Field("p") String p);

    /**
     * 菜品分类
     */
    @FormUrlEncoded
    @POST("rest/food/cates")
    Call<Bean<CateBean>> cates(@Field("p") String p);

    /**
     * 菜品列表
     */
    @FormUrlEncoded
    @POST("rest/food/foods")
    Call<Bean<List<FoodBean>>> foods(@Field("p") String p);

    /**
     * 菜品轮播图片
     */
    @FormUrlEncoded
    @POST("rest/food/images")
    Call<Bean<List<ImageBean>>> images(@Field("p") String p);

    /**
     * 菜品详细信息
     */
    @FormUrlEncoded
    @POST("rest/food/info")
    Call<Bean<FoodBean>> foodInfo(@Field("p") String p);

    /**
     * 菜品价格列表
     */
    @FormUrlEncoded
    @POST("rest/food/prices")
    Call<Bean<List<PriceBean>>> prices(@Field("p") String p);

    /**
     * 退菜
     */
    @FormUrlEncoded
    @POST("rest/food/refund")
    Call<Bean<ResponseCommonBean>> refund(@Field("p") String p);

    /**
     * 点赞
     */
    @FormUrlEncoded
    @POST("rest/food/favor")
    Call<Bean<ResponseCommonBean>> favor(@Field("p") String p);

    /**
     * 购物车数据提交
     */
    @FormUrlEncoded
    @POST("rest/order/order")
    Call<Bean<ResponseCommonBean>> order(@Field("p") String p);

    /**
     * 刷新菜品
     */
    @FormUrlEncoded
    @POST("rest/order/refresh")
    Call<Bean<List<FoodBean>>> refresh(@Field("p") String p);

    /**
     * 服务列表
     */
    @FormUrlEncoded
    @POST("rest/service/services")
    Call<Bean<List<ServiceBean>>> services(@Field("p") String p);

    /**
     * 呼叫服务
     */
    @FormUrlEncoded
    @POST("rest/service/call")
    Call<Bean<ResponseCommonBean>> call(@Field("p") String p);

    /**
     * 取消服务
     */
    @FormUrlEncoded
    @POST("rest/service/abort")
    Call<Bean<ResponseCommonBean>> abort(@Field("p") String p);

    /**
     * 获取结账清单
     */
    @FormUrlEncoded
    @POST("rest/bill/bill")
    Call<Bean<List<BillBean>>> bill(@Field("p") String p);

    /**
     * 打赏参数
     */
    @FormUrlEncoded
    @POST("rest/bill/awards")
    Call<Bean<List<Integer>>> awards(@Field("p") String p);

    /**
     * 结账提交
     */
    @FormUrlEncoded
    @POST("rest/bill/commit")
    Call<Bean<ResponseCommonBean>> billCommit(@Field("p") String p);

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("rest/user/login")
    Call<Bean<MemberBean>> login(@Field("p") String p);

    /**
     * 发送短信验证码
     */
    @FormUrlEncoded
    @POST("rest/user/sms")
    Call<Bean<ResponseCommonBean>> sms(@Field("p") String p);

    /**
     * 快捷登录
     */
    @FormUrlEncoded
    @POST("rest/user/smsLogin")
    Call<Bean<MemberBean>> smsLogin(@Field("p") String p);

    /**
     * 获取会员信息
     */
    @FormUrlEncoded
    @POST("rest/user/info")
    Call<Bean<MemberBean>> userInfo(@Field("p") String p);

    /**
     * 优惠信息列表
     */
    @FormUrlEncoded
    @POST("rest/card/info")
    Call<Bean<CardInfoBean>> cardInfo(@Field("p") String p);

    /**
     * 用户办卡申请
     */
    @FormUrlEncoded
    @POST("rest/card/apply")
    Call<Bean<ResponseCommonBean>> apply(@Field("p") String p);

    /**
     * 获取评价信息
     */
    @FormUrlEncoded
    @POST("rest/eval/items")
    Call<Bean<EvalBean>> evalInfo(@Field("p") String p);

    /**
     * 提交评价
     */
    @FormUrlEncoded
    @POST("rest/eval/commit")
    Call<Bean<ResponseCommonBean>> evalCommit(@Field("p") String p);

    /**
     * 版本管理信息
     */
    @FormUrlEncoded
    @POST("rest/device/update")
    Call<Bean<VersionBean>> version(@Field("p") String p);

    /**
     * 异常信息提交
     */
    @FormUrlEncoded
    @POST("rest/device/exception")
    Call<Bean<ResponseCommonBean>> exception(@Field("p") String p);

}
