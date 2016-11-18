package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 服务界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ServiceFragment extends BaseFragment implements Callback<CommonBean> {

    @Bind(R.id.service_request)
    Button serviceRequest;

    private int selectFlag = 0;

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
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        selectFlag = 0;
    }

    @OnClick(R.id.service_request)
    public void onClick() {
        selectFlag++;
        if (selectFlag % 2 == 0) {
            serviceRequest.setText("呼叫");
            postService(CALL);
        } else {
            serviceRequest.setText("取消呼叫");
            postService(CANCEL);
        }
    }

    private void postService(int type) {
        Call<CommonBean> call = RetrofitRequest.service().service(MainActivity.table, type);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
        CommonBean bean = response.body();
        if (bean.getStatus() == 0) {
            Toast.makeText(getContext(),bean.getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<CommonBean> call, Throwable t) {

    }
}
