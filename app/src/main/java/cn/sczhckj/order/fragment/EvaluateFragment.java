package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.ReviewAdapter;
import cn.sczhckj.order.mode.impl.TagCloudImpl;
import cn.sczhckj.order.overwrite.TagFlowLayout;

/**
 * @describe: 评价界面, 获取服务器评价信息
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class EvaluateFragment extends BaseFragment {


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
    private ReviewAdapter mReviewAdapter;

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
    }

    @Override
    public void setData(Object object) {
        initEvaluate();
    }

    @Override
    public void init() {
        mTagCloud = new TagCloudImpl(getContext());
        initEvaluateAdapter();
    }

    /**
     * 初始化评价适配器
     */
    private void initEvaluateAdapter() {
        loading(loadingParent, contextParent, loadingItemParent, loadingFail, loadingTitle,
                getContext().getResources().getString(R.string.loading));
        mReviewAdapter = new ReviewAdapter(getContext(), null);
        evaluateList.setLayoutManager(new LinearLayoutManager(getContext()));
        evaluateList.setAdapter(mReviewAdapter);
    }

    /**
     * 初始化评价数据
     */
    private void initEvaluate() {

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
                break;
        }
    }
}
