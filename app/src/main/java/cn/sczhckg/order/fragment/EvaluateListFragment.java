package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.EvaluateListAdapter;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckg.order.data.event.EvaluateListEvent;

/**
 * @describe: 菜品评价列表
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class EvaluateListFragment extends BaseFragment {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private EvaluateListAdapter adapter;

    private static int tag=0;

    private List<SettleAccountsDishesItemBean> dishesItemList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void init() {
        adapter = new EvaluateListAdapter(getContext(), dishesItemList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        if (tag==0){
            tag++;
            title.setTextColor(getContext().getResources().getColor(R.color.text_color_red));
        }else {
            title.setTextColor(getContext().getResources().getColor(R.color.text_color_person));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cancel, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getActivity().finish();
                break;
            case R.id.title:
                tag++;
                if (tag%2==1){
                    title.setTextColor(getContext().getResources().getColor(R.color.text_color_red));
                    EventBus.getDefault().post(new EvaluateListEvent(EvaluateListEvent.ALL));
                }else {
                    title.setTextColor(getContext().getResources().getColor(R.color.text_color_person));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 单品菜品选择性评价
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listEvent(EvaluateListEvent event){
        title.setTextColor(getContext().getResources().getColor(R.color.text_color_person));
    }
}
