package cn.sczhckg.order.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import cn.sczhckg.order.adapter.SettleAountsFavorableAdapter;
import cn.sczhckg.order.adapter.SettleAountsPayAdapter;
import cn.sczhckg.order.adapter.VipFavorableAdapter;
import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckg.order.data.bean.VipFavorableBean;
import cn.sczhckg.order.data.event.LoginEvent;
import cn.sczhckg.order.data.event.SettleAountsCartEvent;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;
import cn.sczhckg.order.data.listener.OnAccountsListenner;
import cn.sczhckg.order.data.listener.OnGiftListenner;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import cn.sczhckg.order.until.AppSystemUntil;
import cn.sczhckg.order.until.ConvertUtils;

/**
 * @describe: 结账购物车界面
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsCartFragment extends BaseFragment implements OnGiftListenner, OnAccountsListenner {

    @Bind(R.id.shoppingcart_total_price)
    TextView shoppingcartTotalPrice;
    @Bind(R.id.favorable_price)
    TextView favorablePrice;
    @Bind(R.id.exceptional_price)
    TextView exceptionalPrice;
    @Bind(R.id.exceptional_parent)
    LinearLayout exceptionalParent;
    @Bind(R.id.shoppingcart_button)
    Button shoppingcartButton;
    @Bind(R.id.cart_favorable)
    RecyclerView cartFavorable;
    @Bind(R.id.cart_gift)
    Button cartGift;
    @Bind(R.id.cart_pay)
    RecyclerView cartPay;
    @Bind(R.id.list_flag)
    ImageView listFlag;
    @Bind(R.id.list_view_line)
    View listViewLine;

    private List<FavorableTypeBean> favorableList = new ArrayList<>();

    private List<PayTypeBean> payList = new ArrayList<>();

    private SettleAountsFavorableAdapter favorableAdapter;

    private SettleAountsPayAdapter payAdapter;

    private PayTypeBean payTypeBean;

    private List<SettleAccountsDishesBean> dishesBeen = new ArrayList<>();

    private int POP_HEIGHT=310;

    /**
     * 打赏金额
     */
    private int giftMoney = 0;
    /**
     * 卡种类型
     */
    private PopupWindow mPopupWindow;

    private VipFavorableAdapter mVipFavorableAdapter;

    private List<VipFavorableBean> vipFavorableBeanList = new ArrayList<>();

    /**
     * Pop的高
     */
    private int HEIGHT = 0;
    /**
     * Pop的宽
     */
    private int WIDTH = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setOnGiftListenner(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settle_accounts_cart, null, false);
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

    }

    @Override
    public void init() {
        favorableAdapter = new SettleAountsFavorableAdapter(getContext(), favorableList,this);
        cartFavorable.setLayoutManager(new GridLayoutManager(getContext(), 3));
        cartFavorable.setAdapter(favorableAdapter);

        payAdapter = new SettleAountsPayAdapter(getContext(), payList,this);
        cartPay.setLayoutManager(new GridLayoutManager(getContext(), 3));
        cartPay.setAdapter(payAdapter);
        /**初始化Pop*/
        initPop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件接收
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void settleAountsCartEvent(SettleAountsCartEvent event) {
        /**刷新数据，布局界面*/
        if (event.getType() == SettleAountsCartEvent.LOADING) {
            cartGift.setSelected(false);
            /**刷新会员优惠类型*/
            mVipFavorableAdapter.notifyDataSetChanged(event.getBean().getVipFavorable());

            dishesBeen = event.getBean().getSettleAccountsDishesBeen();
            favorableList = event.getBean().getFavorableType();
            payList = event.getBean().getPayTypeBeen();
            favorableAdapter.notifyDataSetChanged(favorableList);
            payAdapter.notifyDataSetChanged(payList);
            countTotalPrice();
            countTotalFavorable();
        }
    }

    /**
     * 计算总价
     */
    private void countTotalPrice() {
        int total = 0;
        for (SettleAccountsDishesBean bean : dishesBeen) {
            total = total + bean.getTotalPrice();
        }
        shoppingcartTotalPrice.setText("¥  " + (total + giftMoney));
    }

    /**
     * 计算总优惠价
     */
    private void countTotalFavorable() {
        int total = 0;
        for (SettleAccountsDishesBean bean : dishesBeen) {
            List<SettleAccountsDishesItemBean> list = bean.getItemDishes();
            for (SettleAccountsDishesItemBean item : list) {
                total = total + (item.getPrice() - item.getPriceTypeBean().getPrice()) * item.getNumber();
            }
        }
        favorablePrice.setText("¥  " + total);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cart_gift, R.id.shoppingcart_button, R.id.list_flag,R.id.accounts_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_gift:
                /**进入打赏界面*/
                if (cartGift.isSelected()){
                    cartGift.setSelected(false);
                    money(0);
                }else {
                    EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.GTYPE));
                }
                break;
            case R.id.shoppingcart_button:
                /**结账*/
                if (payTypeBean != null) {
                    int type = payTypeBean.getId();
                    EventBus.getDefault().post(new SettleAountsTypeEvent(payTypeBean, SettleAountsTypeEvent.PTYPE));
                }
                break;
            case R.id.list_flag:
                /**显示不同VIP优惠价格*/
                if (listFlag.isSelected()){
                    listFlag.setSelected(false);
                    mPopupWindow.dismiss();
                }else {
                    showPop();
                }
                break;
            case R.id.accounts_bottom:
                /**显示不同VIP优惠价格*/
                if (listFlag.isSelected()){
                    listFlag.setSelected(false);
                    mPopupWindow.dismiss();
                }else {
                    showPop();
                }
                break;
        }
    }

    /**
     * 显示Pop
     */
    public void showPop(){
        listFlag.setSelected(true);
        int[] location = new int[2];
        listViewLine.getLocationOnScreen(location);
//                    mPopupWindow.showAtLocation(listViewLine, Gravity.NO_GRAVITY, 0, AppSystemUntil.height(getContext())-location[1]);
        mPopupWindow.showAtLocation(listViewLine, Gravity.NO_GRAVITY, 0, location[1]-POP_HEIGHT);
    }

    @Override
    public void money(int money) {
        giftMoney = money;
        exceptionalPrice.setText("¥ " + money);
        countTotalPrice();
        /**有价格回调时，才把打赏点亮*/
        if (money!=0) {
            cartGift.setSelected(true);
        }
    }

    @Override
    public void favorableType(FavorableTypeBean bean) {
        EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.FTYPE,bean));
    }

    @Override
    public void payType(PayTypeBean bean) {
        payTypeBean = bean;
    }

    /**
     * 获取打赏金额
     *
     * @return
     */
    public int getGiftMoney() {
        return giftMoney;
    }

    /**
     * 登陆成功，通知价格刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {

    }

    /**
     * 初始化Pop
     */
    private void initPop() {
        initRect();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_vip_favorable, null);
        mPopupWindow = new PopupWindow(view, WIDTH, POP_HEIGHT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView vipRecyclerView = (RecyclerView) view.findViewById(R.id.vip_favorable_recyclerview);
        mVipFavorableAdapter = new VipFavorableAdapter(vipFavorableBeanList, getContext());
        vipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vipRecyclerView.setAdapter(mVipFavorableAdapter);
        vipRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listFlag.setSelected(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 初始化Pop的宽高
     */
    private void initRect() {
        WIDTH = AppSystemUntil.width(getContext()) * 10 / 26 - ConvertUtils.dip2px(getContext(), 5) / 2;
    }

}