package cn.sczhckj.order.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.FavorableActivity;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.adapter.CartAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.CommonBean;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.event.RefreshCartEvent;
import cn.sczhckj.order.data.listener.OnButtonClickListener;
import cn.sczhckj.order.data.listener.OnTotalNumberListener;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.overwrite.MyEditTextDialog;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 购物车
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class CartFragment extends BaseFragment implements OnTotalNumberListener, Callback<Bean<CommonBean>> {


    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.cart_recyclerView)
    RecyclerView cartRecyclerView;
    @Bind(R.id.has_thing)
    RelativeLayout hasThing;
    @Bind(R.id.shoppingcart_total_price)
    TextView shoppingcartTotalPrice;
    @Bind(R.id.shoppingcart_part_number)
    TextView shoppingcartPartNumber;
    @Bind(R.id.shoppingcart_dishes_number)
    TextView shoppingcartDishesNumber;
    @Bind(R.id.shoppingcart_button)
    Button shoppingcartButton;
    @Bind(R.id.cart_favorable)
    ImageView cartFavorable;

    private CartAdapter mCartAdapter;

    private List<FoodBean> mList = new ArrayList<>();

    private OnButtonClickListener onButtonClickListener;
    /**
     * Button按钮类型 0-开桌 1-选菜 3- 4-
     */
    private int buttonType = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        /**初始化弹窗*/
        initLoadingPop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null, true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initFavorableLocation();
    }

    @Override
    public void setData(Object object) {

    }

    /**
     * 初始化更多优惠的位置
     */
    private void initFavorableLocation() {
        cartFavorable.setTranslationX((float) (AppSystemUntil.width(getContext()) * 0.25));
        cartFavorable.setTranslationY(ConvertUtils.dip2px(getContext(), -8));
    }

    /**
     * 刷新购物车
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCart(RefreshCartEvent event) {
//        shoppingcartButton.setClickable(true);
//        shoppingcartButton.setTextColor(getResources().getColor(R.color.button_text));
//        /**本地加菜*/
//        if (event.getBean() != null) {
//            List<FoodBean> list = initList(event.getBean());
//            /**在没有任何菜品时把锅底设置为0*/
//            isEmptyList(list);
//            isHaveData(list);
//            mCartAdapter.notifyDataSetChanged(list);
//        }
//        /**后台加菜*/
//        if (event.getBeanList() != null) {
//            List<FoodBean> list = refreshMainTableAddDishes(event.getBeanList());
//            /**在没有任何菜品时把锅底设置为0*/
//            isEmptyList(list);
//            isHaveData(list);
//            mCartAdapter.notifyDataSetChanged(list);
//        }
    }

    /**
     * 判断是否是空的list
     *
     * @param list
     */
    private void isEmptyList(List<FoodBean> list) {
        if (list.size() == 0) {
            totalPotNumber = 0;
        }
    }


    /**
     * 通过菜品信息来判断是否已经添加次菜品，如果已经添加刷新数量，处理本地加菜
     *
     * @param bean
     */
    private List<FoodBean> initList(FoodBean bean) {
        Map<String, FoodBean> localMap = listToMap();
        if (localMap.containsKey(bean.getId())) {
            int postion = mList.indexOf(localMap.get(bean.getId()));
            int number = bean.getNumber();
            if (number == 0) {
                mList.remove(mList.get(postion));
            } else {
                mList.get(postion).setNumber(number);
            }
        } else {
            if (bean.getNumber() != 0) {
                mList.add(bean);
            }
        }
        return mList;
    }

    /**
     * 处理通过后台即主桌加菜
     *
     * @param beenList
     * @return
     */
    private List<FoodBean> refreshMainTableAddDishes(List<FoodBean> beenList) {
        Map<String, FoodBean> localMap = listToMap();
        for (int i = 0; i < beenList.size(); i++) {
            Integer id = beenList.get(i).getId();
            FoodBean bean = beenList.get(i);
            if (localMap.containsKey(id)) {
                int postion = mList.indexOf(localMap.get(id));
                int number = mList.get(postion).getNumber() + bean.getNumber();
                mList.get(postion).setNumber(number);
                bean.setNumber(number);
            } else {
                mList.add(bean);
            }
            /**刷新点菜界面数据*/
            EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 0, bean));
        }
        return mList;
    }

    /**
     * 把List集合转换为Map集合
     *
     * @return
     */
    private Map<String, FoodBean> listToMap() {
        Map<String, FoodBean> localMap = new HashMap<>();
        for (int i = 0; i < mList.size(); i++) {
            localMap.put(mList.get(i).getId()+"", mList.get(i));
        }
        return localMap;
    }

    @Override
    public void init() {
        isHaveData(mList);
        mCartAdapter = new CartAdapter(mList, getActivity());
        mCartAdapter.setOnTotalNumberListener(this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartRecyclerView.setAdapter(mCartAdapter);
        cartRecyclerView.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.cart_line), 100000, 1));
    }

    /**
     * 处理购物车为空的状态
     */
    private void isHaveData(List<FoodBean> mList) {
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

    /**
     * 开桌
     */
    private void openTable() {
        buttonType = 0;
        cartVerify(0);
        showProgress("数据提交中，清请稍后...");
    }

    /**
     * 密码验证
     */
    private void passwordVerify() {
        final MyEditTextDialog dialog = new MyEditTextDialog(getContext());
        dialog.setTitle("请输入服务密码");
        dialog.setEditTextHint("请输入密码");
        dialog.setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openTablePassword.equals(dialog.getEditText())) {
                    openTable();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "密码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    /**
     * 购物车数据验证
     */
    private void cartVerify(int type) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setDishesList(mList);
        bean.setPerson(MainActivity.person);
        if (type == 0) {
            bean.setType(Constant.OPEN_TABLE);
        } else if (type == 1) {
            bean.setType(Constant.ORDER);
            bean.setOrderType(orderType);
        }
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.OPEN_TABLE_VERIFY)
                .time()
                .bean(bean);
        Call<Bean<CommonBean>> mainShow = RetrofitRequest.service().openTable(restRequest.toRequestString());
        mainShow.enqueue(this);
    }

    @Override
    public void totalNumber(int totalPrice, int potNumber, int dishesNumber) {
        /**设置本次锅底数量*/
        totalPotNumber = potNumber;
        shoppingcartTotalPrice.setText("" + totalPrice);
        shoppingcartPartNumber.setText(potNumber + "");
        shoppingcartDishesNumber.setText(dishesNumber + "");
        /**发送消息，点菜界面收到后判断数量与提示数量是否相符合*/
        EventBus.getDefault().post(new MoreDishesHintEvent(dishesNumber));
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
        Bean<CommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            /**关闭进度框*/
            dismissProgress();

            if (buttonType == 0) {
                shoppingcartButton.setText(R.string.choose_good);
                if (onButtonClickListener != null) {
                    flag = 1;
                    onButtonClickListener.onClick(Constant.ORDER, bean.getResult().getShowType());
                }
            } else if (buttonType == 1) {
                Toast.makeText(getContext(), bean.getMessage(), Toast.LENGTH_SHORT).show();
                /**如果状态是0，则是提交数据成功，此时把按钮变成灰色，当有加菜时，再次亮起*/
                shoppingcartButton.setClickable(false);
                shoppingcartButton.setTextColor(getResources().getColor(R.color.white));
            }
        }

    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {
        loadingFail("提交失败，点击重新提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonType == 0) {
                    cartVerify(0);
                } else if (buttonType == 1) {
                    cartVerify(1);
                }
            }
        });
    }

    /**
     * 当购物车数据有变化返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        shoppingcartButton.setClickable(true);
        shoppingcartButton.setTextColor(getResources().getColor(R.color.button_text));
    }

    @OnClick({R.id.shoppingcart_button, R.id.cart_favorable})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shoppingcart_button:
                if (shoppingcartButton.getText().toString().equals(getResources().getString(R.string.openTable))) {
                    /**开桌*/
                    if (openTablePassword != null && !openTablePassword.equals("")) {
                        /**验证密码*/
                        passwordVerify();
                    } else {
                        /**不用验证密码*/
                        openTable();
                    }
                } else if (shoppingcartButton.getText().toString().equals(getResources().getString(R.string.choose_good))) {
                    /**选好了*/
                    buttonType = 1;
                    cartVerify(1);
                    showProgress("数据提交中，清请稍后...");
                }
                break;
            case R.id.cart_favorable:
                /**更多优惠*/
                Intent intent = new Intent(getActivity(), FavorableActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }
}
