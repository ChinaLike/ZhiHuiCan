package cn.sczhckg.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.sczhckg.order.data.bean.Bean;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.OP;
import cn.sczhckg.order.data.bean.RequestCommonBean;
import cn.sczhckg.order.data.event.CloseServiceEvent;
import cn.sczhckg.order.data.event.QRCodeVerifyEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.data.response.ResponseCode;
import cn.sczhckg.order.fragment.BaseFragment;
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

public class QRCodeService extends Service implements Callback<Bean<CommonBean>> {
    /**
     * 设备ID
     */
    private String deviceID = BaseFragment.deviceId;

    private Call<Bean<CommonBean>> call;

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
    public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
        Bean<CommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            EventBus.getDefault().post(new QRCodeVerifyEvent(bean.getCode(), bean.getMessage() + ""));
            call.cancel();
            stopSelf();
        } else {
            delay();
        }
    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {
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
