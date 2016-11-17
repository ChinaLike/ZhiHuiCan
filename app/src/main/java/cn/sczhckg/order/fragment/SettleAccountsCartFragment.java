package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckg.order.data.event.LoginEvent;
import cn.sczhckg.order.data.event.SettleAountsCartEvent;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;
import cn.sczhckg.order.data.listener.OnGiftListenner;
import cn.sczhckg.order.data.listener.OnPayTypeListenner;

/**
 * @describe: 结账购物车界面
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsCartFragment extends BaseFragment implements OnGiftListenner, OnPayTypeListenner {

    @Bind(R.id.shoppingcart_total_price)
    TextView shoppingcartTotalPrice;
    @Bind(R.id.favorable_price)
    TextView favorablePrice;
    @Bind(R.id.favorable_parent)
    LinearLayout favorableParent;
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

    private List<FavorableTypeBean> favorableList = new ArrayList<>();

    private List<PayTypeBean> payList = new ArrayList<>();

    private SettleAountsFavorableAdapter favorableAdapter;

    private SettleAountsPayAdapter payAdapter;

    private static int i = 0;

    private PayTypeBean payTypeBean;

    private List<SettleAccountsDishesBean> dishesBeen = new ArrayList<>();

    /**
     * 打赏金额
     */
    private int giftMoney = 0;

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
        favorableAdapter = new SettleAountsFavorableAdapter(getContext(), favorableList);
        cartFavorable.setLayoutManager(new GridLayoutManager(getContext(), 2));
        cartFavorable.setAdapter(favorableAdapter);

        payAdapter = new SettleAountsPayAdapter(getContext(), payList);
        cartPay.setLayoutManager(new GridLayoutManager(getContext(), 2));
        payAdapter.setOnPayTypeListenner(this);
        cartPay.setAdapter(payAdapter);
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
            i = 0;
            cartGift.setSelected(false);
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

    @OnClick({R.id.cart_gift, R.id.shoppingcart_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_gift:
                i++;
                if (i % 2 == 0) {
                    cartGift.setSelected(false);
                } else {
                    cartGift.setSelected(true);
                    EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.GTYPE));
                }
                break;
            case R.id.shoppingcart_button:
                if (payTypeBean != null) {
                    int type = payTypeBean.getId();
                    EventBus.getDefault().post(new SettleAountsTypeEvent(payTypeBean, SettleAountsTypeEvent.PTYPE));
                }
                break;
        }
    }

    @Override
    public void money(int money) {
        giftMoney = money;
        exceptionalPrice.setText("¥ " + money);
        countTotalPrice();
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

}
