package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.ClassifyAdapter;
import cn.sczhckg.order.data.bean.ClassifyBean;
import cn.sczhckg.order.data.bean.ClassifyItemBean;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 点餐界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class OrderFragment extends BaseFragment implements Callback<ClassifyBean>{


    @Bind(R.id.dishes_tab)
    RecyclerView dishesTab;
    @Bind(R.id.dishes_classify)
    RecyclerView dishesClassify;
    @Bind(R.id.dishes_show)
    RecyclerView dishesShow;

    /**分类适配器*/
    private ClassifyAdapter mClassifyAdapter;
    /**默认显示第几个*/
    private int defaultItem=0;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null, false);
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

    /**
     * 加载菜单分类项
     *
     * @param type
     */
    public void loadingDishes(int type) {
        Call<ClassifyBean> classify = RetrofitRequest.service(Config.HOST).classify(MainActivity.table, type);
        classify.enqueue(this);
    }

    @Override
    public void onResponse(Call<ClassifyBean> call, Response<ClassifyBean> response) {
        ClassifyBean bean = response.body();
        if (bean!=null) {
            defaultItem = bean.getDefaultClassify();
            List<ClassifyItemBean> itemBeen = bean.getClassifyItemList();
            ClassifyItemBean item=itemBeen.get(defaultItem);
            item.setSelect(true);
            initClassify(itemBeen);
        }
    }

    @Override
    public void onFailure(Call<ClassifyBean> call, Throwable t) {
        Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }

    private void initClassify(List<ClassifyItemBean> itemBeen) {
        mClassifyAdapter =new ClassifyAdapter(getContext(),itemBeen);
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        dishesClassify.setLayoutManager(mLinearLayoutManager);
        dishesClassify.setAdapter(mClassifyAdapter);
        dishesClassify.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 5, 1));
        mClassifyAdapter.setManager(mLinearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
