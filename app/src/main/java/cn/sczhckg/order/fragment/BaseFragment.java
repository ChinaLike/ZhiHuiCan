package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.DishesAdapter;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.listener.OnGiftListenner;
import cn.sczhckg.order.overwrite.DashlineItemDivider;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public abstract class BaseFragment extends Fragment {

    protected static DishesAdapter mDishesAdapter = null;

    protected List<DishesBean> parentDishesList = new ArrayList<>();
    /**
     * 打赏接口
     */
    protected static OnGiftListenner onGiftListenner;

    /**
     * 点餐类型 0-单桌点餐  1-并桌点餐
     */
    public static final int ALONE_ORDER = 0;
    public static final int MERGER_ORDER = 1;

    protected int orderType = ALONE_ORDER;

    /**
     * 界面标识  0-锅底必选  1-点菜界面
     */
    public static int flag = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract void setData(Object object);

    public abstract void init();

    /**
     * 菜品适配
     *
     * @param mRecyclerView
     */
    protected void initDishesAdapter(RecyclerView mRecyclerView) {
        if (mDishesAdapter == null) {
            mDishesAdapter = new DishesAdapter(getContext(), parentDishesList);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
        mRecyclerView.setAdapter(mDishesAdapter);
    }

    public void setOnGiftListenner(OnGiftListenner onGiftListenner) {
        this.onGiftListenner = onGiftListenner;
    }

    public int getFlag() {
        return flag;
    }
}
