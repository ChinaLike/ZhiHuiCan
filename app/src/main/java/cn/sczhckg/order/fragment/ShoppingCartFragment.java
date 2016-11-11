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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.ShoppingCartAdapter;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.event.CartNumberEvent;
import cn.sczhckg.order.data.listener.OnButtonClickListener;
import cn.sczhckg.order.data.listener.OnTotalNumberListener;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 购物车
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class ShoppingCartFragment extends BaseFragment implements OnTotalNumberListener, Callback<CommonBean> {


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

    private OnButtonClickListener onButtonClickListener;
    /**
     * Button按钮类型 0-开桌 1-选菜 3- 4-
     */
    private int buttonType = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        shoppingcartButton.setClickable(true);
        shoppingcartButton.setTextColor(getResources().getColor(R.color.button_text));
        List<DishesBean> bean = (List<DishesBean>) object;
        mList = bean;
        isHaveData(bean);
        mShoppingCartAdapter.notifyDataSetChanged(bean);
    }

    @Override
    public void init() {
        isHaveData(mList);
        mShoppingCartAdapter = new ShoppingCartAdapter(mList, getActivity());
        mShoppingCartAdapter.setOnTotalNumberListener(this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(mShoppingCartAdapter);
        cartRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
    }

    /**
     * 处理购物车为空的状态
     */
    private void isHaveData(List<DishesBean> mList) {
        if (mList.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
            hasThing.setVisibility(View.GONE);
        } else {
            if (nothing.getVisibility() == View.VISIBLE && hasThing.getVisibility() == View.GONE) {
                nothing.setVisibility(View.GONE);
                hasThing.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.shoppingcart_button)
    public void onClick() {
        if (shoppingcartButton.getText().toString().equals(getResources().getString(R.string.openTable))) {
            /**开桌*/
            buttonType = 0;
            Call<CommonBean> openTable = RetrofitRequest.service(Config.HOST).openTable(MainActivity.table, Constant.OPEN_TABLE, mList.toString(), MainActivity.person);
            openTable.enqueue(this);
        } else if (shoppingcartButton.getText().toString().equals(getResources().getString(R.string.choose_good))) {
            /**选好了*/
            buttonType = 1;
            Call<CommonBean> chooseGood = RetrofitRequest.service(Config.HOST).chooseGood(MainActivity.table, orderType, Constant.ORDER, mList.toString());
            chooseGood.enqueue(this);
        }

    }

    @Override
    public void totalNumber(int totalPrice, int potNumber, int dishesNumber) {
        shoppingcartTotalPrice.setText("¥  " + totalPrice);
        shoppingcartPartNumber.setText(potNumber + "");
        shoppingcartDishesNumber.setText(dishesNumber + "");
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
        CommonBean bean = response.body();
        if (buttonType == 0) {
            shoppingcartButton.setText(R.string.choose_good);
            if (bean.getStatus() == 0 && onButtonClickListener != null) {
                onButtonClickListener.onClick(Constant.ORDER, bean.getShowType());
            }
        } else if (buttonType == 1) {
            Toast.makeText(getContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
            if (bean.getStatus() == 0) {
                /**如果状态是0，则是提交数据成功，此时把按钮变成灰色，当有加菜时，再次亮起*/
                shoppingcartButton.setClickable(false);
                shoppingcartButton.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void onFailure(Call<CommonBean> call, Throwable t) {
        Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }

    /**
     * 当购物车数据有变化返回
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void cartEventBus(CartNumberEvent event) {
        shoppingcartButton.setClickable(true);
        shoppingcartButton.setTextColor(getResources().getColor(R.color.button_text));
    }

}
