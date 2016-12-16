package cn.sczhckj.order.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.sczhckj.order.data.bean.CartBean;
import cn.sczhckj.order.data.bean.CateBean;
import cn.sczhckj.order.data.bean.CommonBean;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.bean.PriceBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.event.RefreshCartEvent;
import cn.sczhckj.order.data.listener.OnButtonClickListener;
import cn.sczhckj.order.data.listener.OnTotalNumberListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.OrderMode;
import cn.sczhckj.order.mode.impl.DialogImpl;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
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
    @Bind(R.id.cart_dis_order)
    RecyclerView disCartOrder;
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
    @Bind(R.id.cart_view1)
    View cartView1;
    @Bind(R.id.cart_order_flag)
    ImageView cartOrderFlag;
    @Bind(R.id.cart_order)
    RecyclerView cartOrder;
    @Bind(R.id.cart_view3)
    View cartView3;
    @Bind(R.id.cart_dis_order_flag)
    ImageView cartDisOrderFlag;
    @Bind(R.id.order_parent)
    LinearLayout orderParent;
    @Bind(R.id.disOrder_parent)
    LinearLayout disOrderParent;


    /**
     * 未下单适配器
     */
    private CartAdapter mDisOrderAdapter;

    /**
     * 已下单适配器
     */
    private CartAdapter mOrderAdapter;

    private OnButtonClickListener onButtonClickListener;

    /**
     * 下单的菜品，即已下单菜品
     */
    private List<FoodBean> orderList = new ArrayList<>();
    /**
     * 未下单的菜品，即购物车菜品
     */
    private List<FoodBean> disOrderList = new ArrayList<>();

    /**
     * 弹窗
     */
    private DialogImpl mDialog;

    /**
     * 数据请求
     */
    private OrderMode mOrderMode;

    /**
     * 开桌密码
     */
    private String password = null;

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
        initCart(disOrderList);
        initFavorableLocation();
    }

    @Override
    public void init() {
        mDialog = new DialogImpl(getContext());
        mOrderMode = new OrderMode();
        initOrder();
        initDisOrder();
    }

    @Override
    public void setData(Object object) {

    }

    /**
     * 初始化已下单
     */
    private void initOrder() {
        mOrderAdapter = new CartAdapter(orderList, getActivity());
        mOrderAdapter.setOnTotalNumberListener(this);
        cartOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartOrder.setAdapter(mOrderAdapter);
        cartOrder.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.cart_line), 100000, 1));
    }

    /**
     * 初始化购物车
     */
    private void initDisOrder() {
        mDisOrderAdapter = new CartAdapter(disOrderList, getActivity());
        mDisOrderAdapter.setOnTotalNumberListener(this);
        disCartOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        disCartOrder.setAdapter(mDisOrderAdapter);
        disCartOrder.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.cart_line), 100000, 1));
    }

    /**
     * 初始化更多优惠的位置
     */
    private void initFavorableLocation() {
        cartFavorable.setTranslationX((float) (AppSystemUntil.width(getContext()) * 0.25));
        cartFavorable.setTranslationY(ConvertUtils.dip2px(getContext(), -8));
    }

    /**
     * 初始化购物车
     */
    private void initCart(List<FoodBean> list) {
        if (orderList.size() == 0 && list.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
            hasThing.setVisibility(View.GONE);
        } else {
            nothing.setVisibility(View.GONE);
            hasThing.setVisibility(View.VISIBLE);
        }
        /**判断已下单菜品*/
        if (orderList.size() == 0) {
            orderParent.setVisibility(View.GONE);
            cartOrder.setVisibility(View.GONE);
        } else {
            orderParent.setVisibility(View.VISIBLE);
            cartOrder.setVisibility(View.VISIBLE);
        }
        /**判断购物车菜品*/
        if (list.size() == 0) {
            disOrderParent.setVisibility(View.GONE);
            disCartOrder.setVisibility(View.GONE);
        } else {
            disOrderParent.setVisibility(View.VISIBLE);
            disCartOrder.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置Button的属性
     *
     * @param isClick
     */
    private void buttonAttr(boolean isClick) {
        shoppingcartButton.setClickable(isClick);
        if (isClick) {
            shoppingcartButton.setSelected(false);
            shoppingcartButton.setTextColor(ContextCompat.getColor(getContext(), R.color.button_text));
        } else {
            shoppingcartButton.setSelected(true);
            shoppingcartButton.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_999999));
        }
    }

    /**
     * 刷新购物车
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCart(RefreshCartEvent event) {
        buttonAttr(true);
        /**本地加菜*/
        if (event.getBean() != null) {
            List<FoodBean> list = initList(event.getBean());
            initCart(list);
            mDisOrderAdapter.notifyDataSetChanged(list);
            setFoodinfo(shoppingcartDishesNumber, shoppingcartTotalPrice, shoppingcartPartNumber, list);
        }
        /**后台加菜*/
//        if (event.getBeanList() != null) {
//            List<FoodBean> list = refreshMainTableAddDishes(event.getBeanList());
//            /**在没有任何菜品时把锅底设置为0*/
//            isEmptyList(list);
//            isHaveData(list);
//            mCartAdapter.notifyDataSetChanged(list);
//        }
    }

    /**
     * 通过菜品信息来判断是否已经添加次菜品，如果已经添加刷新数量，处理本地加菜
     *
     * @param bean
     */
    private List<FoodBean> initList(FoodBean bean) {
        if (disOrderList.size() == 0) {
            disOrderList.add(bean);
        } else {
            boolean isAdd = false;
            for (FoodBean item : disOrderList) {
                /**只有ID和分类ID对应一样才是同一个菜品*/
                if ((item.getId().equals(bean.getId())) && (item.getCateId().equals(bean.getCateId()))) {
                    item.setNumber(bean.getNumber());
                    isAdd = true;
                }
                if (item.getNumber() == 0) {
                    disOrderList.remove(item);
                }
            }
            if (!isAdd) {
                disOrderList.add(bean);
            }
        }
        return disOrderList;
    }

    /**
     * 设置总数量，总价格，总优惠
     *
     * @param numberView
     * @param priceView
     * @param favorView
     * @param beenList
     */
    private void setFoodinfo(TextView numberView, TextView priceView, TextView favorView, List<FoodBean> beenList) {
        int number = 0;
        Double price = 0.0;
        Double favorPrice = 0.0;
        for (FoodBean bean : beenList) {
            number = number + bean.getNumber();
            Double currPrice = getPrice(bean);
            price = price + currPrice * bean.getNumber();
            favorPrice = favorPrice + (bean.getPrice() - currPrice) * bean.getNumber();
        }
        numberView.setText(number + "");
        priceView.setText(price + "");
        favorView.setText(favorPrice + "");
    }

    /**
     * 获取最终价
     *
     * @param bean
     * @return
     */
    private Double getPrice(FoodBean bean) {
        Double price = bean.getPrice();
        if (bean.getPrices() != null && bean.getPrices().size() != 0) {
            for (PriceBean item : bean.getPrices()) {
                if (item.getActive() == Constant.PRICE_ACTIVE) {
                    price = item.getPrice();
                }
            }
        }
        return price;
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
                int postion = disOrderList.indexOf(localMap.get(id));
                int number = disOrderList.get(postion).getNumber() + bean.getNumber();
                disOrderList.get(postion).setNumber(number);
                bean.setNumber(number);
            } else {
                disOrderList.add(bean);
            }
            /**刷新点菜界面数据*/
            EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 0, bean));
        }
        return disOrderList;
    }

    /**
     * 把List集合转换为Map集合
     *
     * @return
     */
    private Map<String, FoodBean> listToMap() {
        Map<String, FoodBean> localMap = new HashMap<>();
        for (int i = 0; i < disOrderList.size(); i++) {
            localMap.put(disOrderList.get(i).getId() + "", disOrderList.get(i));
        }
        return localMap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 下单数据初始化
     *
     * @param password
     */
    private void openTable(String password) {
        showProgress("数据提交中，清请稍后...");
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setPassword(password);
        bean.setCart(infoSwitch());
        mOrderMode.order(bean, this);

    }

    /**
     * 把菜品信息简化后提交给服务器
     *
     * @return
     */
    private List<CartBean> infoSwitch() {
        List<CartBean> cartList = new ArrayList<>();
        for (FoodBean bean : disOrderList) {
            CartBean cart = new CartBean();
            cart.setId(bean.getId());
            cart.setCateId(bean.getCateId());
            cart.setNumber(bean.getNumber());
            cart.setPrice(getPrice(bean));
            cartList.add(cart);
        }
        return cartList;
    }

    /**
     * 必选菜品验证
     */
    private CateBean.CateItemBean requiredVerify() {
        Map<Integer, Integer> map = getCateItemNumber();
        CateBean.CateItemBean curr = null;
        for (CateBean.CateItemBean bean : cateList) {
            if (bean.getRequired() == Constant.REQUIRED) {
                if (map.containsKey(bean.getId())) {
                    curr = null;
                } else {
                    curr = bean;
                    return curr;
                }
            }
        }
        return curr;
    }

    /**
     * 获取分类下的个数和
     */
    private Map<Integer, Integer> getCateItemNumber() {
        /**有分类ID作为Key，个数作为value*/
        Map<Integer, Integer> mapNumber = new HashMap<>();
        for (CartBean item : infoSwitch()) {
            if (mapNumber.containsKey(item.getCateId())) {
                Integer number = mapNumber.get(item.getCateId()) + item.getNumber();
                mapNumber.put(item.getCateId(), number);
            } else {
                mapNumber.put(item.getCateId(), item.getNumber());
            }
        }
        return mapNumber;
    }


    /**
     * 信息验证
     */
    private void infoVerify() {

        mDialog.setEditDialog("信息确认", "就餐人数：" + MainActivity.personNumber + "人", "请输入开桌密码")
                .setLeftButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.editTextDialog().dismiss();
                    }
                })
                .setRightButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        password = mDialog.editTextDialog().getEditText().toString();
                        openTable(password);
                        mDialog.editTextDialog().dismiss();
                    }
                }).show();
    }

    @Override
    public void totalNumber(int totalPrice, int potNumber, int dishesNumber) {

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
        if (bean != null) {
            if (bean.getCode() == ResponseCode.SUCCESS) {
                /**关闭进度框*/
                dismissProgress();
                onButtonClickListener.onClick(Constant.ORDER, bean.getResult().getShowType());
                cartToOrder();
                mOrderAdapter.notifyDataSetChanged(orderList);
            } else {
                commit("" + bean.getMessage());
            }
        } else {
            commit("提交失败，点击重新提交");
        }

    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {
        commit("提交失败，点击重新提交");
    }

    /**
     * 提交失败，重新提交
     *
     * @param title
     */
    private void commit(String title) {
        loadingFail(title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTable(password);
            }
        });
    }

    /**
     * 购物车中数据转化到已下单中
     */
    private void cartToOrder() {
        for (FoodBean bean : disOrderList) {
            orderList.add(bean);
        }
        /**把购物车清空*/
        disOrderList = new ArrayList<>();
        mDisOrderAdapter.notifyDataSetChanged(disOrderList);
        initCart(disOrderList);
    }

    /**
     * 当购物车数据有变化返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        buttonAttr(true);
    }

    @OnClick({R.id.shoppingcart_button, R.id.cart_favorable, R.id.order_parent, R.id.disOrder_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shoppingcart_button:
                /**数据提交前先进行信息验证*/
                CateBean.CateItemBean bean=requiredVerify();
                if (bean == null) {
                    infoVerify();
                } else {
                    mDialog.aloneDialog(getString(R.string.dialog_title),
                            "尊敬的顾客您好！\n【" + bean.getName() + "】为必选菜品，只有选择规定数量\n才能提交信息，还请谅解！",
                            "继续点餐").show();
                }
                break;
            case R.id.cart_favorable:
                /**更多优惠*/
                Intent intent = new Intent(getActivity(), FavorableActivity.class);
                getContext().startActivity(intent);
                break;

            case R.id.order_parent:
                /**已经下单*/
                setTitleStatus(cartOrderFlag, cartOrder, cartDisOrderFlag, disCartOrder);
                break;
            case R.id.disOrder_parent:
                /**还未下单，在购物车*/
                setTitleStatus(cartDisOrderFlag, disCartOrder, cartOrderFlag, cartOrder);
                break;
        }
    }

    /**
     * 设置购物车中已下单和购物车切换状态
     *
     * @param imageView
     * @param recyclerView
     */
    private void setTitleStatus(ImageView imageView, RecyclerView recyclerView, ImageView imageOteher, RecyclerView recyclerViewOther) {
        if (imageView.isSelected()) {
            imageView.setSelected(false);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            imageView.setSelected(true);
            recyclerView.setVisibility(View.GONE);
        }

        imageOteher.setSelected(true);
        recyclerViewOther.setVisibility(View.GONE);

    }

}
