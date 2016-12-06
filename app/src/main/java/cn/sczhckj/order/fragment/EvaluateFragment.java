package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.EvaluateHotWordAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.CommonBean;
import cn.sczhckj.order.data.bean.EvaluateBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.EvaluateListEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 评价界面, 获取服务器评价信息
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class EvaluateFragment extends BaseFragment {

    @Bind(R.id.ratingBar1)
    RatingBar ratingBar1;
    @Bind(R.id.ratingBar2)
    RatingBar ratingBar2;
    @Bind(R.id.ratingBar3)
    RatingBar ratingBar3;
    @Bind(R.id.ratingBar4)
    RatingBar ratingBar4;
    @Bind(R.id.evaluate_other)
    RecyclerView evaluateOther;
    @Bind(R.id.evaluate_finish)
    Button evaluateFinish;

    private String id = "0";
    /**
     * 热词ID
     */
    private int hotWordId =-1;
    /**
     * 用户输入意见
     */
    private String opinion = "";

    private EvaluateHotWordAdapter adapter;

    private List<EvaluateBean> mList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate, null, false);
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
        adapter = new EvaluateHotWordAdapter(getContext(), mList);
        evaluateOther.setLayoutManager(new GridLayoutManager(getContext(), 4));
        evaluateOther.setAdapter(adapter);
        getEvaluate(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 根据对应评价菜品选择对应的参数
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listEvent(EvaluateListEvent event) {
        id = event.getBean().getId();
        getEvaluate(id);
    }

    /**
     * 获取评价信息
     *
     * @param id
     */
    private void getEvaluate(String id) {
        RequestCommonBean bean=new RequestCommonBean();
        bean.setId(id);
        RestRequest<RequestCommonBean> restRequest= JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_EVALUATE_SHOW)
                .time()
                .bean(bean);

        Call<Bean<List<EvaluateBean>>> getEvaluateCall = RetrofitRequest.service().getEvaluate(restRequest.toRequestString());
        getEvaluateCall.enqueue(new Callback<Bean<List<EvaluateBean>>>() {
            @Override
            public void onResponse(Call<Bean<List<EvaluateBean>>> call, Response<Bean<List<EvaluateBean>>> response) {
                Bean<List<EvaluateBean>> bean = response.body();
                if (bean != null&&bean.getCode()== ResponseCode.SUCCESS) {
                    mList = bean.getResult();
                    adapter.notifyDataSetChanged(mList);
                }
            }

            @Override
            public void onFailure(Call<Bean<List<EvaluateBean>>> call, Throwable t) {
                try {
                    Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传评价信息
     */
    private void postEvaluate() {
        RequestCommonBean bean=new RequestCommonBean();
        bean.setId(id);
        bean.setRatingBar1(ratingBar1.getRating());
        bean.setRatingBar2(ratingBar2.getRating());
        bean.setRatingBar3(ratingBar3.getRating());
        bean.setRatingBar4(ratingBar4.getRating());
        bean.setHotWordId(hotWordId);
        bean.setOpinion(opinion);

        RestRequest<RequestCommonBean> restRequest=JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_EVALUATE)
                .time()
                .bean(bean);

        Call<Bean<CommonBean>> postEvaluateCall = RetrofitRequest.service().postEvaluate(restRequest.toRequestString());
        postEvaluateCall.enqueue(new Callback<Bean<CommonBean>>() {
            @Override
            public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
                Bean<CommonBean> beanBean=response.body();
                if (beanBean!=null&&beanBean.getCode()==ResponseCode.SUCCESS) {
                    Toast.makeText(getContext(), beanBean.getMessage(), Toast.LENGTH_SHORT).show();
                    /**成功评价后关闭*/
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.evaluate_finish)
    public void onClick() {
        postEvaluate();
    }
}
