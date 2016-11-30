package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.EvaluateListAdapter;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;

/**
 * @describe: 菜品评价列表
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class EvaluateListFragment extends BaseFragment {

    @Bind(R.id.list_recyclerview)
    RecyclerView recyclerview;
    private EvaluateListAdapter adapter;

    private List<SettleAccountsDishesItemBean> dishesItemList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null, false);
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
        List<SettleAccountsDishesBean> mList = (List<SettleAccountsDishesBean>) object;
        for (int i = 0; i < mList.size(); i++) {
            List<SettleAccountsDishesItemBean> item = mList.get(i).getItemDishes();
            for (int j = 0; j < item.size(); j++) {
                dishesItemList.add(item.get(j));
            }
        }
        /**设置整体评价Item*/
        SettleAccountsDishesItemBean item=new SettleAccountsDishesItemBean();
        item.setId(-1+"");
        item.setName("整体评价");
        dishesItemList.add(0,item);
        adapter.notifyDataSetChanged(dishesItemList);
    }

    @Override
    public void init() {
        adapter = new EvaluateListAdapter(getContext(), dishesItemList);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return manager.getSpanCount();
                }
                return 1;
            }
        });
        recyclerview.setLayoutManager(manager);


        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.list_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
