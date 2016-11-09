package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.ShoppingCartAdapter;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.NainPagerShow;
import cn.sczhckg.order.data.listener.OnButtonClickListener;
import cn.sczhckg.order.data.listener.OnShoppingCartListener;
import cn.sczhckg.order.data.listener.OnTotalNumberListener;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 购物车
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class ShoppingCartFragment extends BaseFragment implements OnTotalNumberListener, Callback<NainPagerShow>{


    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.cart_recyclerView)
    RecyclerView cartRecyclerView;
    @Bind(R.id.has_thing)
    LinearLayout hasThing;
    @Bind(R.id.shoppingcart_total_price)
    TextView shoppingcartTotalPrice;
    @Bind(R.id.shoppingcart_part_number)
    TextView shoppingcartPartNumber;
    @Bind(R.id.shoppingcart_dishes_number)
    TextView shoppingcartDishesNumber;
    @Bind(R.id.shoppingcart_button)
    Button shoppingcartButton;

    private ShoppingCartAdapter mShoppingCartAdapter;

    private List<DishesBean> mList = new ArrayList<>();

    private OnShoppingCartListener onShoppingCartListener;

    private OnButtonClickListener onButtonClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, null, true);
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
        List<DishesBean> bean = (List<DishesBean>) object;
        mList=bean;
        isHaveData(bean);
        mShoppingCartAdapter.notifyDataSetChanged(bean);
    }

    @Override
    public void init() {
        isHaveData(mList);
        mShoppingCartAdapter = new ShoppingCartAdapter(mList, getActivity(),onShoppingCartListener);
        mShoppingCartAdapter.setOnTotalNumberListener(this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(mShoppingCartAdapter);
        cartRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
    }

    public void setOnShoppingCartListener(OnShoppingCartListener onShoppingCartListener) {
        this.onShoppingCartListener = onShoppingCartListener;
    }

    /**
     * 处理购物车为空的状态
     */
    private void isHaveData(List<DishesBean> mList) {
        if (mList.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
            hasThing.setVisibility(View.GONE);
        } else {
            if (nothing.getVisibility()==View.VISIBLE&&hasThing.getVisibility()==View.GONE) {
                nothing.setVisibility(View.GONE);
                hasThing.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.shoppingcart_button)
    public void onClick() {
        /**开桌*/
        Call<NainPagerShow> openTable = RetrofitRequest.service(Config.HOST).openTable(MainActivity.table,Constant.OPEN_TABLE,mList.toString(),MainActivity.person);
        openTable.enqueue(this);

    }

    @Override
    public void totalNumber(int totalPrice, int potNumber, int dishesNumber) {
        shoppingcartTotalPrice.setText("¥  "+totalPrice);
        shoppingcartPartNumber.setText(potNumber+"");
        shoppingcartDishesNumber.setText(dishesNumber+"");
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onResponse(Call<NainPagerShow> call, Response<NainPagerShow> response) {

    }

    @Override
    public void onFailure(Call<NainPagerShow> call, Throwable t) {

    }
}
