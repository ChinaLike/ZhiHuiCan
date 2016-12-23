package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CloseServiceEvent;
import cn.sczhckj.order.data.event.QRCodeVerifyEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 扫码完成服务
 * @author: Like on 2016/11/29.
 * @Email: 572919350@qq.com
 */

public class QRCodeService extends Service implements Callback<Bean<ResponseCommonBean>> {
    /**
     * 设备ID
     */
    private String deviceID = BaseFragment.deviceId;

    private Call<Bean<ResponseCommonBean>> call;

    private Handler mHandler;

    private Runnable mRunnable;
    /**
     * 二维码刷新时间，以秒为单位
     */
    private final static long TIME = 20;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        delay();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开始请求
     */
    private void startRequest() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceID);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.REFRESH_QRCODE)
                .time()
                .bean(bean);
        call = RetrofitRequest.service().refreshQRCodeIsVerify(restRequest.toRequestString());
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
        Bean<ResponseCommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            EventBus.getDefault().post(new QRCodeVerifyEvent(bean.getCode(), bean.getMessage() + ""));
            call.cancel();
            stopSelf();
        } else {
            delay();
        }
    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        delay();
    }

    /**
     * 20s请求一次
     */
    private void delay() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                startRequest();
            }
        };

        mHandler.postDelayed(mRunnable, TIME * 1000);

    }

    /**
     * 接收到关闭服务事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeService(CloseServiceEvent event) {
        mHandler.removeCallbacks(mRunnable);
        stopSelf();
    }

}
