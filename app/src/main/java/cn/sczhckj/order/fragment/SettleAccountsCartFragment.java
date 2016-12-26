package cn.sczhckj.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.SettleAountsFavorableAdapter;
import cn.sczhckj.order.adapter.SettleAountsPayAdapter;
import cn.sczhckj.order.adapter.VipFavorableAdapter;
import cn.sczhckj.order.data.bean.FavorableTypeBean;
import cn.sczhckj.order.data.bean.PayTypeBean;
import cn.sczhckj.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckj.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckj.order.data.bean.VipFavorableBean;
import cn.sczhckj.order.data.event.SettleAountsCartEvent;
import cn.sczhckj.order.data.event.SettleAountsTypeEvent;
import cn.sczhckj.order.data.listener.OnAccountsListenner;
import cn.sczhckj.order.data.listener.OnTipListenner;
import cn.sczhckj.order.overwrite.DashlineItemDivider;

/**
 * @describe: 结账购物车界面
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsCartFragment extends BaseFragment implements OnTipListenner, OnAccountsListenner {

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
    @Bind(R.id.contextParent)
    RelativeLayout contextParent;
    @Bind(R.id.loading_title)
    TextView loadingTitle;
    @Bind(R.id.loading_parent)
    LinearLayout loadingItemParent;
    @Bind(R.id.loading_fail_title)
    TextView loadingFailTitle;
    @Bind(R.id.loading_fail)
    LinearLayout loadingFail;
    @Bind(R.id.loadingParent)
    LinearLayout loadingParent;
    @Bind(R.id.vip_favorable_recyclerview)
    RecyclerView vipFavorableRecyclerview;

    private List<FavorableTypeBean> favorableList = new ArrayList<>();

    private List<PayTypeBean> payList = new ArrayList<>();

    private SettleAountsFavorableAdapter favorableAdapter;

    private SettleAountsPayAdapter payAdapter;

    private PayTypeBean payTypeBean;

    private List<SettleAccountsDishesBean> dishesBeen = new ArrayList<>();

    /**
     * 打赏金额
     */
    private int giftMoney = 0;
    /**
     * 会员优惠适配器
     */
    private VipFavorableAdapter mVipFavorableAdapter;

    private List<VipFavorableBean> vipFavorableBeanList = new ArrayList<>();


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
        /**初始化会员显示*/
        initFavorable();
        loading(getContext().getResources().getString(R.string.loading));
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        favorableAdapter = new SettleAountsFavorableAdapter(getContext(), favorableList, this);
        cartFavorable.setLayoutManager(new GridLayoutManager(getContext(), 3));
        cartFavorable.setAdapter(favorableAdapter);

        payAdapter = new SettleAountsPayAdapter(getContext(), payList, this);
        cartPay.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
        loadingSuccess();
        if (event.getType() == SettleAountsCartEvent.LOADING) {
            buttonLoosen(cartGift,getContext());
            /**刷新会员优惠类型*/
            mVipFavorableAdapter.notifyDataSetChanged(event.getBean().getVipFavorable());
            dishesBeen = event.getBean().getSettleAccountsDishesBeen();
            favorableList = event.getBean().getFavorableType();
            payList = event.getBean().getPayTypeBeen();
            favorableAdapter.notifyDataSetChanged(favorableList);
            payAdapter.notifyDataSetChanged(payList);
            countTotalPrice();
            countTotalFavorable();

        } else if (event.getType() == SettleAountsCartEvent.REFRESH) {
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
        shoppingcartTotalPrice.setText("¥ " + (total + giftMoney));
    }

    /**
     * 计算总优惠价
     */
    private void countTotalFavorable() {
        int total = 0;
        for (SettleAccountsDishesBean bean : dishesBeen) {
            List<SettleAccountsDishesItemBean> list = bean.getItemDishes();
            for (SettleAccountsDishesItemBean item : list) {
                if (item.getPriceTypeBean() != null) {
//                    total = total + (item.getPrice() - item.getPriceTypeBean().getPrice()) * item.getNumber();
                }
            }
        }
        favorablePrice.setText("¥ " + total);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cart_gift, R.id.shoppingcart_button, R.id.list_flag, R.id.accounts_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_gift:
                /**进入打赏界面*/
                if (cartGift.isSelected()) {
                    buttonLoosen(cartGift,getContext());
                    money(0);
                } else {
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
                if (listFlag.isSelected()) {
                    listFlag.setSelected(false);
                    vipFavorableRecyclerview.setVisibility(View.GONE);
                } else {
                    listFlag.setSelected(true);
                    vipFavorableRecyclerview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.accounts_bottom:
                /**显示不同VIP优惠价格*/
                if (listFlag.isSelected()) {
                    listFlag.setSelected(false);
                    vipFavorableRecyclerview.setVisibility(View.GONE);
                } else {
                    listFlag.setSelected(true);
                    vipFavorableRecyclerview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.loadingParent:
                /**加载失败显示*/

                break;
        }
    }

    @Override
    public void money(int money) {
        giftMoney = money;
        exceptionalPrice.setText("¥ " + money);
        countTotalPrice();
        /**有价格回调时，才把打赏点亮*/
        if (money != 0) {
            buttonSelect(cartGift,getContext());
        }
    }

    @Override
    public void favorableType(FavorableTypeBean bean) {
        EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.FTYPE, bean));
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
     * 初始化会员显示
     */
    private void initFavorable() {
        mVipFavorableAdapter = new VipFavorableAdapter(vipFavorableBeanList, getContext());
        vipFavorableRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        vipFavorableRecyclerview.setAdapter(mVipFavorableAdapter);
        vipFavorableRecyclerview.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
        listFlag.setSelected(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 加载中
     */
    private void loading(String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
        loadingTitle.setText(str);
    }

    /**
     * 加载成功
     */
    private void loadingSuccess() {
        loadingParent.setVisibility(View.GONE);
        contextParent.setVisibility(View.VISIBLE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    private void loadingFail(String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.GONE);
        loadingFail.setVisibility(View.VISIBLE);
        loadingFailTitle.setText(str);
    }

    /**
     * 按钮被按下
     * @param btn
     */
    public static void buttonSelect(Button btn, Context mContext){
        btn.setSelected(true);
        btn.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_main_select));
    }

    /**
     * 按钮松开
     * @param btn
     */
    public static void buttonLoosen(Button btn, Context mContext){
        btn.setSelected(false);
        btn.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_person));
    }

}