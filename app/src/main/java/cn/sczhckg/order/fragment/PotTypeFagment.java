package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.PersonChooseAdapter;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.PersonBean;
import cn.sczhckg.order.data.event.CartNumberEvent;
import cn.sczhckg.order.overwrite.DashlineItemDivider;

/**
 * @describe: 锅底选择和推荐菜品
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class PotTypeFagment extends BaseFragment {


    @Bind(R.id.pot_line)
    View potLine;
    @Bind(R.id.dishes_line)
    View dishesLine;
    @Bind(R.id.person_choose)
    RecyclerView personChoose;
    @Bind(R.id.dishes_choose)
    RecyclerView dishesChoose;
    @Bind(R.id.pot_parent)
    RelativeLayout potParent;
    @Bind(R.id.order_dishes_parent)
    RelativeLayout dishesParent;

    /**
     * 默认显示几列备选人数
     */
    public static int DEFAULT_PERSON = 3;

    private PersonChooseAdapter personAdapter;

    private List<PersonBean> personList;

    private List<DishesBean> dishesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**注册事件监听*/
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pot_type, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        Log.d("=========","222222222222222");
        potParent.setClickable(false);
        dishesParent.setClickable(false);
        initDishesAdapter(dishesChoose);
    }

    @Override
    public void setData(Object object) {
        Log.d("=========","1111111111111111111");
        potParent.setClickable(true);
        dishesParent.setClickable(true);
        MainPagerShow bean = (MainPagerShow) object;
        DEFAULT_PERSON = bean.getPerson().size();
        /**========初始化人数========*/
        personChoose.setLayoutManager(new LinearLayoutManager(getActivity()));
        personAdapter = new PersonChooseAdapter(getActivity(), initPerson(bean));
        personChoose.setAdapter(personAdapter);
        /**设置分割线*/
        personChoose.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 5, 1, DEFAULT_PERSON));
        /**========初始化菜品========*/
        dishesList = bean.getDishesList();
        parentDishesList = bean.getPotList();
        mDishesAdapter.notifyDataSetChanged(parentDishesList);
    }

    /**
     * 购物车数据变化后选择栏的数据同步刷新
     *
     * @param bean
     */
    public void upData(DishesBean bean) {
        String id = bean.getId();
        String dishesName = bean.getName();
        for (DishesBean item : dishesList) {
            if (item.getId().equals(id) && item.getName().equals(dishesName)) {
                item.setNumber(bean.getNumber());
                mDishesAdapter.notifyDataSetChanged();
            }
        }
        for (DishesBean item : parentDishesList) {
            if (item.getId().equals(id) && item.getName().equals(dishesName)) {
                item.setNumber(bean.getNumber());
                mDishesAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 初始化桌面人数
     *
     * @param bean
     * @return
     */
    private List<PersonBean> initPerson(MainPagerShow bean) {
        /**添加人数选择*/
        PersonBean mPersonBean = new PersonBean();
        mPersonBean.setNumber(bean.getDefaultNumber());
        mPersonBean.setTableName(bean.getTableNumber());
        /**初始化默认人数，避免消费者未选择时此数为0*/
        MainActivity.person = bean.getDefaultNumber();
        personList = bean.getPerson();
        personList.add(mPersonBean);
        return personList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        /**取消事件监听*/
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        upData(event.getBean());
    }

    @OnClick({R.id.pot_parent, R.id.order_dishes_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pot_parent:
                potLine.setVisibility(View.VISIBLE);
                dishesLine.setVisibility(View.GONE);
                mDishesAdapter.notifyDataSetChanged(parentDishesList);
                break;
            case R.id.order_dishes_parent:
                potLine.setVisibility(View.GONE);
                dishesLine.setVisibility(View.VISIBLE);
                mDishesAdapter.notifyDataSetChanged(dishesList);
                break;
        }
    }
}
