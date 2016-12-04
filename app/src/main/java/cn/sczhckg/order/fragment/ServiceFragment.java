package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.Bean;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.OP;
import cn.sczhckg.order.data.bean.RequestCommonBean;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.data.response.ResponseCode;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 服务界面呼叫界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ServiceFragment extends BaseFragment implements Callback<Bean<CommonBean>> {

    @Bind(R.id.service_title)
    TextView serviceTitle;
    @Bind(R.id.service_phone)
    ImageView servicePhone;
    @Bind(R.id.service_hint)
    TextView serviceHint;
    @Bind(R.id.service_gif)
    GifImageView serviceGif;

    private GifDrawable mGifDrawable = null;


    /**
     * 呼叫
     */
    private final static int CALL = 0;
    /**
     * 取消呼叫
     */
    private final static int CANCEL = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        try {
            mGifDrawable = new GifDrawable(getResources(), R.drawable.service_bg);
            serviceGif.setImageResource(R.drawable.service_bg_call);
            serviceTitle.setText("请点击呼叫按钮，服务员将火速前来！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void postService(int type) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setType(type);

        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.SERVICE)
                .time()
                .bean(bean);

        Call<Bean<CommonBean>> call = RetrofitRequest.service().service(restRequest.toRequestString());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
        Bean<CommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            if (servicePhone.isSelected()){
                serviceTitle.setText(bean.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {

    }

    @OnClick(R.id.service_parent)
    public void onClick() {
        if (servicePhone.isSelected()) {
            stopCall();
        } else {
            startCall();
        }
    }

    /**
     * 开始呼叫
     */
    private void startCall() {
        if (mGifDrawable!=null){
            serviceGif.setImageDrawable(mGifDrawable);
        }
        servicePhone.setSelected(true);
        postService(CALL);
        serviceHint.setText("挂断");
        serviceTitle.setText("服务员呼叫中，请耐心等待...");
    }

    /**
     * 停止呼叫
     */
    private void stopCall() {
        serviceGif.setImageResource(R.drawable.service_bg_call);
        servicePhone.setSelected(false);
        postService(CANCEL);
        serviceHint.setText("呼叫");
        serviceTitle.setText("请点击呼叫按钮，服务员将火速前来！");
    }

}
