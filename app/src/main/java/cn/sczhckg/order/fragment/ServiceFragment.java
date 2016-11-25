package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 服务界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ServiceFragment extends BaseFragment implements Callback<Bean<CommonBean>> {


    /**
     * 呼叫
     */
    private final static int CALL = 0;
    /**
     * 取消呼叫
     */
    private final static int CANCEL = 1;
    @Bind(R.id.call_parent)
    RelativeLayout callParent;
    @Bind(R.id.cancel_parent)
    LinearLayout cancelParent;


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
        callParent.setVisibility(View.VISIBLE);
        cancelParent.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void postService(int type) {
        RequestCommonBean bean=new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setType(type);

        RestRequest<RequestCommonBean> restRequest= JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.SERVICE)
                .time()
                .bean(bean);

        Call<Bean<CommonBean>> call = RetrofitRequest.service().service(restRequest.toRequestString());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
        Bean<CommonBean> bean = response.body();
        if (bean!=null&&bean.getCode() == ResponseCode.SUCCESS) {
            Toast.makeText(getContext(), bean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {

    }

    @OnClick({R.id.calling, R.id.cancel_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calling:
                postService(CALL);
                callParent.setVisibility(View.GONE);
                cancelParent.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_call:
                postService(CANCEL);
                callParent.setVisibility(View.VISIBLE);
                cancelParent.setVisibility(View.GONE);
                break;
        }
    }
}
