package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.DishesAdapter;
import cn.sczhckg.order.adapter.PersonChooseAdapter;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.NainPagerShow;
import cn.sczhckg.order.data.bean.PersonBean;
import cn.sczhckg.order.data.listener.OnDishesChooseListener;
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
    @Bind(R.id.dishes_parent)
    RelativeLayout dishesParent;

    /**
     * 默认显示几列备选人数
     */
    public static int DEFAULT_PERSON = 3;

    private PersonChooseAdapter personAdapter;

    private DishesAdapter dishesAdapter;

    private List<PersonBean> personList;

    private List<DishesBean> dishesList;

    private List<DishesBean> potList;

    private OnDishesChooseListener onDishesChooseListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        potParent.setClickable(false);
        dishesParent.setClickable(false);
    }

    @Override
    public void setData(Object object) {
        potParent.setClickable(true);
        dishesParent.setClickable(true);
        NainPagerShow bean = (NainPagerShow) object;
        DEFAULT_PERSON = bean.getPerson().size();
        /**========初始化人数========*/
        personChoose.setLayoutManager(new LinearLayoutManager(getActivity()));
        personAdapter = new PersonChooseAdapter(getActivity(), initPerson(bean));
        personChoose.setAdapter(personAdapter);
        /**设置分割线*/
        personChoose.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 5, 1, DEFAULT_PERSON));
        /**========初始化菜品========*/
        dishesChoose.setLayoutManager(new LinearLayoutManager(getActivity()));
        dishesList = bean.getDishesList();
        potList = bean.getPotList();
        dishesAdapter = new DishesAdapter(getActivity(), potList,onDishesChooseListener);
        dishesChoose.setAdapter(dishesAdapter);
        dishesChoose.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
    }

    /**
     * 刷新数据
     * @param bean
     */
    public void upData(DishesBean bean){
        String id=bean.getId();
        String dishesName=bean.getName();
        for (DishesBean item:potList) {
            if (item.getId().equals(id)&&item.getName().equals(dishesName)){
                item.setNumber(bean.getNumber());
                dishesAdapter.notifyDataSetChanged();
            }
        }
        for (DishesBean item:dishesList) {
            if (item.getId().equals(id)&&item.getName().equals(dishesName)){
                item.setNumber(bean.getNumber());
                dishesAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 监听传递
     * @param onDishesChooseListener
     */
    public void onDishesChooseListenner(OnDishesChooseListener onDishesChooseListener){
        this.onDishesChooseListener =onDishesChooseListener;
    }

    /**
     * 初始化桌面人数
     *
     * @param bean
     * @return
     */
    private List<PersonBean> initPerson(NainPagerShow bean) {
        /**添加人数选择*/
        PersonBean mPersonBean = new PersonBean();
        mPersonBean.setNumber(bean.getDefaultNumber());
        mPersonBean.setTableName(bean.getTableNumber());
        /**初始化默认人数，避免消费者未选择时此数为0*/
        MainActivity.person=bean.getDefaultNumber();
        personList = bean.getPerson();
        personList.add(mPersonBean);
        return personList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.pot_parent, R.id.dishes_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pot_parent:
                potLine.setVisibility(View.VISIBLE);
                dishesLine.setVisibility(View.GONE);
                dishesAdapter.notifyDataSetChanged(potList);
                break;
            case R.id.dishes_parent:
                potLine.setVisibility(View.GONE);
                dishesLine.setVisibility(View.VISIBLE);
                dishesAdapter.notifyDataSetChanged(dishesList);
                break;
        }
    }
}
