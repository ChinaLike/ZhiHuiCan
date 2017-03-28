package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.SettleAccountsAdapter;
import cn.sczhckj.order.adapter.TipAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.BillMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 结账界面
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class BillFragment extends BaseFragment implements Callback<Bean<List<BillBean>>>, OnItemClickListener {


    @Bind(R.id.cart_top_parent)
    LinearLayout cartTopParent;
    @Bind(R.id.cart_tip)
    RecyclerView cartTip;
    @Bind(R.id.cart_total_price)
    TextView totalPrice;
    @Bind(R.id.cart_tip_money)
    TextView tipMoney;
    @Bind(R.id.cart_favor_price)
    TextView favorPrice;
    @Bind(R.id.shoppingcart_button)
    Button shoppingcartButton;
    @Bind(R.id.cart_bottom_parent)
    LinearLayout cartBottomParent;
    @Bind(R.id.cart_top_view)
    View cartTopView;
    @Bind(R.id.cart_bottom_view)
    View cartBottomView;
    @Bind(R.id.cart_favorable)
    ImageView cartFavorable;
    @Bind(R.id.cart_bill_list)
    ExpandableListView cartBillList;

    /**
     * 清单数据获取
     */
    private BillMode mBillMode;
    /**
     * ExpandableListView数据
     */
    private List<BillBean> mList = new ArrayList<>();
    /**
     * ExpandableListView适配器
     */
    private SettleAccountsAdapter mSettleAccountsAdapter;
    /**
     * 打赏适配
     */
    private TipAdapter mTipAdapter;
    /**
     * 总价
     */
    private Double totalP = 0.0;
    /**
     * 总优惠
     */
    private Double totalF = 0.0;
    /**
     * 打赏金额
     */
    private int awards = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    public int setLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initBill();
        initTip();
    }

    @Override
    public void setData(Object object) {
        /**费消费中执行*/
        //初始打赏金额
        awards = 0;
        //文本初始化
        tipMoney.setText("0");
        //再次初始化避免上次已经选择数据Btn状态为改变
        initTipAdapter();
        initBill();
        initTip();
    }

    @Override
    public void init() {
        buttonStatus(true);
        mBillMode = new BillMode();
        initFavorableLocation();
        initBillAdapter();
        initTipAdapter();
    }

    @Override
    public void initFail() {
        /**加载失败，重新加载*/
        setData(null);
    }

    @Override
    public void loadingFail() {
        /**结账*/
        initCommit();
    }

    /**
     * 初始化结账清单适配器
     */
    private void initBillAdapter() {
        //设置 属性 GroupIndicator 去掉默认向下的箭头
        cartBillList.setGroupIndicator(null);
        mSettleAccountsAdapter = new SettleAccountsAdapter(mContext, mList);
        cartBillList.setAdapter(mSettleAccountsAdapter);
    }

    /**
     * 初始化打赏适配器
     */
    private void initTipAdapter() {
        mTipAdapter = new TipAdapter(mContext, null);
        mTipAdapter.setOnItemClickListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cartTip.setLayoutManager(linearLayoutManager);
        cartTip.setAdapter(mTipAdapter);
    }

    /**
     * 初始化账单数据
     */
    private void initBill() {
        initing(mContext.getResources().getString(R.string.bill_fragment_loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setRecordId(MyApplication.tableBean.getRecordId());
        mBillMode.bill(bean, this);
    }

    /**
     * 初始化打赏数据
     */
    private void initTip() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        mBillMode.awards(bean, awardsCallback);
    }

    /**
     * 初始化结账
     */
    private void initCommit() {
        showProgress(getString(R.string.bill_fragment_commit));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setRecordId(MyApplication.tableBean.getRecordId());
        bean.setAwards(awards);
        mBillMode.billCommit(bean, commitCallback);
    }

    /**
     * 初始化更多优惠的位置
     */
    private void initFavorableLocation() {
        cartFavorable.setTranslationX((float) (AppSystemUntil.width(mContext) * 0.25));
        cartFavorable.setTranslationY(ConvertUtils.dip2px(mContext, -8));
    }

    /**
     * 计算总价和总优惠
     *
     * @param list
     */
    private void compute(List<BillBean> list) {
        /**总价*/
        totalP = 0.0;
        /**总优惠*/
        totalF = 0.0;
        for (BillBean beanP : list) {
            totalP = totalP + beanP.getSum();
            for (FoodBean beanC : beanP.getFoods()) {
                totalF = totalF + (beanC.getOriginPrice() - beanC.getPrice()) * beanC.getCount();
            }
        }
        /**设置总价*/
        this.totalPrice.setText("" + totalP);
        /**设置总优惠*/
        this.favorPrice.setText("" + totalF);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.shoppingcart_button, R.id.cart_favorable})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shoppingcart_button:
                /**结账*/
                initCommit();
                break;
            case R.id.cart_favorable:
                /**更多优惠*/
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE));
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<List<BillBean>>> call, Response<Bean<List<BillBean>>> response) {
        Bean<List<BillBean>> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            initSuccess();
            mList = bean.getResult();
            compute(mList);
            mSettleAccountsAdapter.notifyDataSetChanged(mList);
        } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
            initFailer(bean.getMessage());
        } else {
            initFailer(mContext.getResources().getString(R.string.bill_fragment_loading_fail));
        }
    }

    @Override
    public void onFailure(Call<Bean<List<BillBean>>> call, Throwable t) {
        initFailer(mContext.getResources().getString(R.string.bill_fragment_loading_fail));
    }

    /**
     * 打赏回调
     */
    Callback<Bean<List<Integer>>> awardsCallback = new Callback<Bean<List<Integer>>>() {
        @Override
        public void onResponse(Call<Bean<List<Integer>>> call, Response<Bean<List<Integer>>> response) {
            Bean<List<Integer>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                mTipAdapter.notifyDataSetChanged(bean.getResult());
            }
        }

        @Override
        public void onFailure(Call<Bean<List<Integer>>> call, Throwable t) {

        }
    };

    /**
     * 结账回调
     */
    Callback<Bean<ResponseCommonBean>> commitCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                //结账成功后按钮不可点击
                buttonStatus(false);
                dismissProgress();
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                loadingFail(bean.getMessage());
            } else {
                loadingFail(getString(R.string.bill_fragment_bill_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
            loadingFail(getString(R.string.bill_fragment_bill_fail));
        }
    };

    @Override
    public void onItemClick(View view, int position, Object bean) {
        if (position == -1) {
            awards = 0;
            tipMoney.setText("0");
            totalPrice.setText("" + totalP);
        } else {
            awards = (int) bean;
            tipMoney.setText("" + bean);
            totalPrice.setText("" + (totalP + (Integer) bean));
        }
    }

    /**
     * 按钮状态
     * @param isClick   是否可以点击
     */
    private void buttonStatus(boolean isClick){
        shoppingcartButton.setClickable(isClick);
        shoppingcartButton.setSelected(!isClick);
        if (isClick) {
            /**可点击*/
            shoppingcartButton.setText("结账");
            shoppingcartButton.setTextColor(ContextCompat.getColor(mContext,R.color.button_text));
        }else {
            /**不可点击*/
            shoppingcartButton.setText("已结账");
            shoppingcartButton.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_person));
        }
    }

    /**
     * 结账完成
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (event.getType() == WebSocketEvent.TYPE_BILL_FINISH) {
            /**结账完成*/
            finish();
        }
    }

}