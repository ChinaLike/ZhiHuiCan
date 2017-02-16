package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.EvalAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.eval.EvalBean;
import cn.sczhckj.order.data.bean.eval.EvalItemBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.listener.OnTagClickListenner;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.EvalMode;
import cn.sczhckj.order.mode.impl.TagCloudImpl;
import cn.sczhckj.order.overwrite.FlowLayout;
import cn.sczhckj.order.overwrite.TagFlowLayout;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 评价界面, 获取服务器评价信息
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class EvaluateFragment extends BaseFragment implements Callback<Bean<EvalBean>>, OnTagClickListenner {


    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;
    @Bind(R.id.loading_title)
    TextView loadingTitle;
    @Bind(R.id.loading_parent)
    LinearLayout loadingItemParent;
    @Bind(R.id.loading_fail_title)
    TextView loadingFailTitle;
    @Bind(R.id.loading_fail)
    LinearLayout loadingFail;
    @Bind(R.id.loadingParent)
    LinearLayout loadingParent;
    @Bind(R.id.evaluate_finish)
    Button evaluateFinish;
    @Bind(R.id.evaluate_list)
    RecyclerView evaluateList;
    @Bind(R.id.words_cloud)
    TagFlowLayout wordsCloud;
    @Bind(R.id.contextParent)
    RelativeLayout contextParent;

    /**
     * 热词实现类
     */
    private TagCloudImpl mTagCloud;
    /**
     * 评价适配器
     */
    private EvalAdapter mEvalAdapter;
    /**
     * 获取评价数据
     */
    private EvalMode mEvalMode;
    /**
     * 已选热词ID
     */
    private List<Integer> wordList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setData(null);
    }

    @Override
    public void setData(Object object) {
        initEvaluate();
    }

    @Override
    public void init() {
        mTagCloud = new TagCloudImpl(mContext);
        mEvalMode = new EvalMode();
        initEvaluateAdapter();
    }

    /**
     * 初始化评价适配器
     */
    private void initEvaluateAdapter() {
        mEvalAdapter = new EvalAdapter(mContext, null);
        evaluateList.setLayoutManager(new LinearLayoutManager(mContext));
        evaluateList.setAdapter(mEvalAdapter);
    }

    /**
     * 初始化评价数据
     */
    private void initEvaluate() {
        loading(loadingParent, contextParent, loadingItemParent, loadingFail, loadingTitle,
                mContext.getResources().getString(R.string.loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        mEvalMode.evalInfo(bean, this);
    }

    /**
     * 提交数据
     */
    private void commit() {
        evaluateFinish.setText("评价提交中...");
        evaluateFinish.setClickable(false);
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setWords(wordList);
        bean.setItems(mEvalAdapter.getmList());
        mEvalMode.evalCommit(bean, commitCallback);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.loadingParent, R.id.evaluate_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadingParent:
                /**重新加载*/
                setData(null);
                break;
            case R.id.evaluate_finish:
                /**提交评价*/
                commit();
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<EvalBean>> call, Response<Bean<EvalBean>> response) {
        Bean<EvalBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            loadingSuccess(loadingParent, contextParent, loadingItemParent, loadingFail);
            mEvalAdapter.notifyDataSetChanged(bean.getResult().getItems());
            mTagCloud.setWord(wordsCloud, bean.getResult().getWords(), this);
        } else {
            loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                    mContext.getResources().getString(R.string.loadingFail));
        }
    }

    @Override
    public void onFailure(Call<Bean<EvalBean>> call, Throwable t) {
        loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                mContext.getResources().getString(R.string.loadingFail));
    }

    /**
     * 评价提交
     */
    Callback<Bean<ResponseCommonBean>> commitCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                evaluateFinish.setText("提交成功");
            } else {
                evaluateFinish.setText("提交失败，请重新提交");
                evaluateFinish.setClickable(true);
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
            evaluateFinish.setText("提交失败，请重新提交");
            evaluateFinish.setClickable(true);
        }
    };

    @Override
    public void onTagClick(View view, int postion, Object bean) {
        Button btn = (Button) view;
        EvalItemBean item = (EvalItemBean) bean;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setTextColor(0xFF666666);
            wordList.remove(item.getId());
        } else {
            btn.setSelected(true);
            btn.setTextColor(ContextCompat.getColor(mContext, R.color.button_text));
            wordList.add(item.getId());
        }
    }
}
