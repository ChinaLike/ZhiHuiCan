package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.EvaluateHotWordAdapter;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.EvaluateBean;
import cn.sczhckg.order.data.event.EvaluateListEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
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
    private String hotWordId = "";
    /**
     * 用户输入意见
     */
    private String opinion = "";

    private EvaluateHotWordAdapter adapter;

    private List<EvaluateBean.EvaluateListBean> mList = new ArrayList<>();

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
        Log.d("====","=====");
        Call<EvaluateBean> getEvaluateCall = RetrofitRequest.service().getEvaluate(id);
        getEvaluateCall.enqueue(new Callback<EvaluateBean>() {
            @Override
            public void onResponse(Call<EvaluateBean> call, Response<EvaluateBean> response) {
                EvaluateBean bean = response.body();
                if (bean != null) {
                    mList = bean.getEvaluateListBean();
                    adapter.notifyDataSetChanged(mList);
                }
            }

            @Override
            public void onFailure(Call<EvaluateBean> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 上传评价信息
     */
    private void postEvaluate() {
        Call<CommonBean> postEvaluateCall = RetrofitRequest.service().postEvaluate(id, ratingBar1.getRating(), ratingBar2.getRating(), ratingBar3.getRating(), ratingBar4.getRating(), hotWordId, opinion);
        postEvaluateCall.enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
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
