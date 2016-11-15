package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.sczhckg.order.R;
import cn.sczhckg.order.data.event.EvaluateListEvent;

/**
 * @describe: 评价界面
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class EvaluateFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate, null, false);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 根据对应评价菜品选择对应的参数
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listEvent(EvaluateListEvent event){
        Toast.makeText(getContext(),event.getBean().getName(),Toast.LENGTH_SHORT).show();
    }


}
