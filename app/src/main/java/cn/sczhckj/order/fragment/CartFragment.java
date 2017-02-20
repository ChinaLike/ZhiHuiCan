package cn.sczhckj.order.fragment;

import android.content.Context;
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
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.adapter.CartAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.CartBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnButtonClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.OrderMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.DialogImpl;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 购物车
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class CartFragment extends BaseFragment implements Callback<Bean<ResponseCommonBean>> {


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

    /**
     * 弹窗
     */
    private DialogImpl mDialog;

    /**
     * 下单数据请求
     */
    private OrderMode mOrderMode;
    /**
     * 开桌
     */
    private TableMode mTableMode;

    /**
     * 开桌密码
     */
    private String password = null;

    /**
     * 单次退菜数量
     */
    private final int COUNT = 1;
    /**
     * 是否退菜
     */
    private boolean isCommit = false;
    /**
     * 布局视图
     */
    private View view;
    /**
     * 提交菜品刷新
     */
    private final int COMMIT_TYPE = 0;
    /**
     * 单独刷新
     */
    private final int REFRESH = 1;
    /**
     * 刷新菜品类型
     */
    private int refreshType = COMMIT_TYPE;

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
        view = inflater.inflate(R.layout.fragment_cart, null, true);
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
        /**首先把购物车图标置为不可点*/
        mDialog = new DialogImpl(mContext);
        mOrderMode = new OrderMode();
        mTableMode = new TableMode();
        initOrder();
        initDisOrder();
        initRefresh(COMMIT_TYPE);
    }

    @Override
    public void setData(Object object) {

    }

    /**
     * 初始化已下单
     */
    private void initOrder() {
        mOrderAdapter = new CartAdapter(orderList, getActivity());
        mOrderAdapter.setType(CartAdapter.ORDER_TYPE);
        cartOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartOrder.setAdapter(mOrderAdapter);
        cartOrder.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.cart_line), 100000, 1));
    }

    /**
     * 初始化购物车
     */
    private void initDisOrder() {
        mDisOrderAdapter = new CartAdapter(disOrderList, getActivity());
        mDisOrderAdapter.setType(CartAdapter.DIS_ORDER_TYPE);
        disCartOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        disCartOrder.setAdapter(mDisOrderAdapter);
        disCartOrder.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.cart_line), 100000, 1));
    }

    /**
     * 初始化更多优惠的位置
     */
    private void initFavorableLocation() {
        cartFavorable.setTranslationX((float) (AppSystemUntil.width(mContext) * 0.25));
        cartFavorable.setTranslationY(ConvertUtils.dip2px(mContext, -8));
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
            if (list.size() == 0) {
                orderParent.setVisibility(View.GONE);
            } else {
                orderParent.setVisibility(View.VISIBLE);
            }
            cartOrder.setVisibility(View.VISIBLE);
        }
        /**判断购物车菜品*/
        if (list.size() == 0) {
            disOrderParent.setVisibility(View.GONE);
            disCartOrder.setVisibility(View.GONE);
        } else {
            if (orderList.size() == 0) {
                disOrderParent.setVisibility(View.GONE);
            } else {
                disOrderParent.setVisibility(View.VISIBLE);
                cartOrder.setVisibility(View.GONE);
                cartOrderFlag.setSelected(true);
            }
            disCartOrder.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置Button的属性
     *
     * @param isClick
     */
    private void buttonAttr(boolean isClick) {
        if (disOrderList.size() > 0) {
            shoppingcartButton.setClickable(true);
        } else {
            shoppingcartButton.setClickable(isClick);
            if (isClick) {
                /**可选状态*/
                shoppingcartButton.setSelected(false);
                shoppingcartButton.setTextColor(ContextCompat.getColor(mContext, R.color.button_text));
            } else {
                /**变为灰色*/
                shoppingcartButton.setSelected(true);
                shoppingcartButton.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_999999));
            }
        }

        if (!isOpen) {
            shoppingcartButton.setText(getString(R.string.openTable));
        } else {
            shoppingcartButton.setText(getString(R.string.choose_good));
        }

    }

    /**
     * 通过菜品信息来加菜
     *
     * @param bean
     */
    private List<FoodBean> addFood(FoodBean bean) {
        int id = bean.getId();
        int cateId = bean.getCateId();
        boolean isAdd = false;
        for (FoodBean item : disOrderList) {
            if (item.getId() == id && item.getCateId() == cateId) {
                item.setCount(item.getCount());
                isAdd = true;
            }
        }
        if (!isAdd) {
            bean.setCount(1);
            disOrderList.add(bean);
        }
        return disOrderList;
    }

    /**
     * 通过菜品信息来减菜
     *
     * @param bean
     */
    private List<FoodBean> minusFood(FoodBean bean) {
        int id = bean.getId();
        int cateId = bean.getCateId();
        List<FoodBean> mList = new ArrayList<>();
        mList.addAll(disOrderList);
        for (FoodBean item : disOrderList) {
            if (item.getId() == id && item.getCateId() == cateId) {
                int num = item.getCount();
                mList.remove(item);
                if (num > 0) {
                    item.setCount(num);
                    mList.add(item);
                }
            }
        }
        return mList;
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
            number = number + bean.getCount();
            Double currPrice = bean.getPrice();
            price = price + currPrice * bean.getCount();
            favorPrice = favorPrice + (bean.getOriginPrice() - bean.getPrice()) * bean.getCount();
        }
        numberView.setText(number + "");
        priceView.setText(price + "");
        favorView.setText(favorPrice + "");
        /**发送消息，点菜界面收到后判断数量与提示数量是否相符合*/
        if (isOpen) {
            EventBus.getDefault().post(new MoreDishesHintEvent(number));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 下单数据初始化
     *
     * @param password
     */
    private void openTable(String password) {
        showProgress("数据提交中，请稍后...");
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setPassword(password);
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setPersonCount(MainActivity.personNumber);
        bean.setFoods(infoSwitch());
        mTableMode.open(bean, this);

    }

    /**
     * 已开桌，点菜
     */
    private void order() {
        showProgress("数据提交中，请稍后...");
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setRecordId(MyApplication.tableBean.getRecordId());
        bean.setFoods(infoSwitch());
        mOrderMode.order(bean, this);
    }

    /**
     * 刷新已下单菜品
     */
    private void initRefresh(int type) {
        refreshType = type;
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setRecordId(MyApplication.tableBean.getRecordId());
        mOrderMode.refresh(bean, refreshFoodCallback);
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
            cart.setNumber(bean.getCount());
            cart.setType(bean.getType());
            cart.setPrice(bean.getPrice());
            cart.setOriginPrice(bean.getOriginPrice());
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
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
        Bean<ResponseCommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            isCommit = true;
            if (isOpen) {
                /**已开桌*/
                T.showShort(mContext, bean.getMessage());
            } else {
                /**未开桌*//**设置点菜方式*/
                MyApplication.tableBean.setOrderType(bean.getResult().getShowType());
                /**设置菜品过多提醒*/
                warmPromptNumber = bean.getResult().getFoodCountHint() != null ? bean.getResult().getFoodCountHint() : 0;
                MyApplication.tableBean.setFoodCountHint(warmPromptNumber);
                /**设置消费记录ID*/
                MyApplication.tableBean.setRecordId(bean.getResult().getRecordId());
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.MAIN, bean.getResult().getShowType()));
                isOpen = true;
            }
            /**刷新购物车数据*/
            initRefresh(COMMIT_TYPE);

        } else {
            commit("" + bean.getMessage());
        }

    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        commit("提交失败，点击重新提交");
    }

    /**
     * 刷新菜品回调
     */
    Callback<Bean<List<FoodBean>>> refreshFoodCallback = new Callback<Bean<List<FoodBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<FoodBean>>> call, Response<Bean<List<FoodBean>>> response) {
            Bean<List<FoodBean>> bean = response.body();
            if (refreshType == COMMIT_TYPE) {
                /**提交刷新*/
                if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                    /**关闭进度框*/
                    dismissProgress();
                    cartToOrder(bean.getResult());
                } else {
                    commit("提交失败，点击重新提交");
                }
            } else {
                /**推送刷新*/
                if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                    orderList = bean.getResult();
                    mOrderAdapter.notifyDataSetChanged(orderList);
                }
            }
        }

        @Override
        public void onFailure(Call<Bean<List<FoodBean>>> call, Throwable t) {
            if (refreshType == COMMIT_TYPE) {
                commit("提交失败，点击重新提交");
            }
        }
    };


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
     * 菜品，价格合计，优惠显示刷新
     */
    private void baseInfoRefresh() {
        if (disOrderList == null || disOrderList.size() == 0) {
            /**显示已下单价格*/
            setFoodinfo(shoppingcartDishesNumber, shoppingcartTotalPrice, shoppingcartPartNumber, orderList);
        } else {
            /**显示未下单价格*/
            setFoodinfo(shoppingcartDishesNumber, shoppingcartTotalPrice, shoppingcartPartNumber, disOrderList);
        }
    }

    /**
     * 购物车中数据转化到已下单中
     */
    private void cartToOrder(List<FoodBean> mList) {
        /**把购物车清空*/
        orderList = mList;
        if (isCommit) {
            disOrderList = new ArrayList<>();
        }
        mDisOrderAdapter.notifyDataSetChanged(disOrderList);
        initCart(disOrderList);
        mOrderAdapter.notifyDataSetChanged(orderList);
        /**数据提交成功*/
        EventBus.getDefault().post(new RefreshFoodEvent(RefreshFoodEvent.CART_COMMIT));
        /**初始导航*/
        setTitleStatus(cartOrderFlag, cartOrder);
        /**底部显示*/
        baseInfoRefresh();
        buttonAttr(false);
    }


    @OnClick({R.id.shoppingcart_button, R.id.cart_favorable, R.id.order_parent, R.id.disOrder_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shoppingcart_button:
                isAddFood = false;
                /**数据提交前先进行信息验证*/
                if (isOpen) {
                    /**已开桌，加菜*/
                    order();
                } else {
                    CateBean.CateItemBean bean = requiredVerify();
                    if (bean == null) {
                        /**未开桌，提交菜品*/
                        infoVerify();
                    } else {
                        mDialog.aloneDialog(getString(R.string.dialog_title),
                                "尊敬的顾客您好！\n【" + bean.getName() + "】为必选菜品，只有选择规定数量\n才能提交信息，还请谅解！",
                                "继续点餐").show();
                    }
                }

                break;
            case R.id.cart_favorable:
                /**更多优惠*/
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE));
                break;

            case R.id.order_parent:
                /**已经下单*/
                setTitleStatus(cartOrderFlag, cartOrder);
                break;
            case R.id.disOrder_parent:
                /**还未下单，在购物车*/
                setTitleStatus(cartDisOrderFlag, disCartOrder);
                break;
        }
    }

    /**
     * 设置购物车中已下单和购物车切换状态
     */
    private void setTitleStatus(ImageView flag, RecyclerView recyclerView) {
        if (orderList.size() != 0 && disOrderList.size() != 0) {
            orderParent.setVisibility(View.VISIBLE);
            disOrderParent.setVisibility(View.VISIBLE);
            if (flag.isSelected()) {
                flag.setSelected(false);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                flag.setSelected(true);
                recyclerView.setVisibility(View.GONE);
            }

        } else {
            orderParent.setVisibility(View.GONE);
            disOrderParent.setVisibility(View.GONE);
        }
    }

    /**
     * 退菜
     *
     * @param bean
     */
    private void refund(final FoodBean bean) {
        showProgress("退菜中...");
        final RequestCommonBean requestCommonBean = new RequestCommonBean();
        requestCommonBean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        requestCommonBean.setFoodId(bean.getId());
        requestCommonBean.setCateId(bean.getCateId());
        requestCommonBean.setCount(COUNT);
        requestCommonBean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        requestCommonBean.setRecordId(MyApplication.tableBean.getRecordId());
        requestCommonBean.setOperateID(Constant.OPERATE_ID);
        requestCommonBean.setPriceTypeID(bean.getType());
        new FoodMode().refund(requestCommonBean, new Callback<Bean<ResponseCommonBean>>() {
            @Override
            public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
                Bean<ResponseCommonBean> rBean = response.body();
                if (rBean != null && rBean.getCode() == ResponseCode.SUCCESS) {
                    isCommit = false;
                    /**调用后台刷新*/
                    initRefresh(REFRESH);
                    baseInfoRefresh();
                } else {
                    dismissProgress();
                    T.showShort(mContext, rBean == null ? "退菜失败" : rBean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
                dismissProgress();
                T.showShort(mContext, "退菜失败");
            }
        });
    }

    /**
     * 刷新购物车
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshFoodEventBus(RefreshFoodEvent event) {
        /**把按钮状态buttonAttr(true)写在里面，避免了其他命令影响按钮状态*/
        if (event.getType() == RefreshFoodEvent.ADD_FOOD) {
            buttonAttr(true);
            /**加菜*/
            disOrderList = addFood(event.getBean());
            initCart(disOrderList);
            mDisOrderAdapter.notifyDataSetChanged(disOrderList);
            baseInfoRefresh();
        } else if (event.getType() == RefreshFoodEvent.MINUS_FOOD) {
            buttonAttr(true);
            /**减菜*/
            disOrderList = minusFood(event.getBean());
            initCart(disOrderList);
            mDisOrderAdapter.notifyDataSetChanged(disOrderList);
            baseInfoRefresh();
        } else if (event.getType() == RefreshFoodEvent.CART_REFUND) {
            buttonAttr(true);
            /**退菜*/
            refund(event.getBean());
        }

    }

    /**
     * 推送已完成数量,通知事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (event.getType() == WebSocketEvent.TYPE_FOOD_ARRIVE) {
            /**菜品完成全部刷新*/
            initRefresh(REFRESH);
        } else if (WebSocketEvent.REFRESH_RECORD == event.getType()) {
            /**刷新点菜记录的已下单*/
            if (isOpen) {
                /**开桌后才会刷新已下单*/
                initRefresh(REFRESH);
            }
            /**刷新点菜记录的未下单*/
            //TODO: 2017-01-16 这里是不是应该添加单个菜品刷新接口，还是通过foodId和cateId来请求所有数据一一比较刷新
        }
    }
}
