package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

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
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.BillMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 结账界面
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class BillFragment extends BaseFragment implements Callback<Bean<List<BillBean>>>, OnItemClickListener {


    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;
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
    @Bind(R.id.cart_loading_success)
    RelativeLayout cartLoadingSuccess;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, null, false);
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
        initBill();
        initTip();
    }

    @Override
    public void init() {
        mBillMode = new BillMode();
        initFavorableLocation();
        initBillAdapter();
        initTipAdapter();
    }

    /**
     * 初始化结账清单适配器
     */
    private void initBillAdapter() {
        //设置 属性 GroupIndicator 去掉默认向下的箭头
        cartBillList.setGroupIndicator(null);
        mSettleAccountsAdapter = new SettleAccountsAdapter(getContext(), mList);
        cartBillList.setAdapter(mSettleAccountsAdapter);
    }

    /**
     * 初始化打赏适配器
     */
    private void initTipAdapter() {
        mTipAdapter = new TipAdapter(getContext(), null);
        mTipAdapter.setOnItemClickListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cartTip.setLayoutManager(linearLayoutManager);
        cartTip.setAdapter(mTipAdapter);
    }

    /**
     * 初始化账单数据
     */
    private void initBill() {
        loading(loadingParent, cartLoadingSuccess, loadingItemParent, loadingFail, loadingTitle,
                getContext().getResources().getString(R.string.loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setMemberCode(MyApplication.memberCode);
        mBillMode.bill(bean, this);
    }

    /**
     * 初始化打赏数据
     */
    private void initTip() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setMemberCode(MyApplication.memberCode);
        mBillMode.awards(bean, awardsCallback);
    }

    /**
     * 初始化结账
     */
    private void initCommit() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setMemberCode(MyApplication.memberCode);
        bean.setRecordId(MyApplication.recordId);
        mBillMode.billCommit(bean, commitCallback);
    }

    /**
     * 初始化更多优惠的位置
     */
    private void initFavorableLocation() {
        cartFavorable.setTranslationX((float) (AppSystemUntil.width(getContext()) * 0.25));
        cartFavorable.setTranslationY(ConvertUtils.dip2px(getContext(), -8));
    }

    /**
     * 计算总价和总优惠
     *
     * @param list
     */
    private void compute(List<BillBean> list) {
        /**总价*/
        Double totalPrice = 0.0;
        /**总优惠*/
        Double totalFavorPrice = 0.0;
        for (BillBean beanP : list) {
            totalPrice = totalPrice + beanP.getSum();
            for (FoodBean beanC : beanP.getFoods()) {
                totalFavorPrice = totalFavorPrice + (beanC.getOriginPrice() - beanC.getPrice()) * beanC.getCount();
            }
        }
        /**设置总价*/
        this.totalPrice.setText("" + totalPrice);
        /**设置总优惠*/
        this.favorPrice.setText("" + totalFavorPrice);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnClick({R.id.shoppingcart_button, R.id.cart_favorable, R.id.loadingParent})
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
            case R.id.loadingParent:
                /**加载失败，重新加载*/
                setData(null);
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<List<BillBean>>> call, Response<Bean<List<BillBean>>> response) {
        Bean<List<BillBean>> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            loadingSuccess(loadingParent, cartLoadingSuccess, loadingItemParent, loadingFail);
            mList = bean.getResult();
            compute(mList);
            mSettleAccountsAdapter.notifyDataSetChanged(mList);
        } else {
            loadingFail(loadingParent, cartLoadingSuccess, loadingItemParent, loadingFail, loadingFailTitle,
                    getContext().getResources().getString(R.string.loadingFail));
        }
    }

    @Override
    public void onFailure(Call<Bean<List<BillBean>>> call, Throwable t) {
        loadingFail(loadingParent, cartLoadingSuccess, loadingItemParent, loadingFail, loadingFailTitle,
                getContext().getResources().getString(R.string.loadingFail));
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
            T.showShort(getContext(), bean.getMessage());
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                finish();
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
            T.showShort(getContext(), "结账失败，请重新提交");
        }
    };

    @Override
    public void onItemClick(View view, int position, Object bean) {
        if (position == -1) {
            tipMoney.setText("0");
        } else {
            tipMoney.setText("" + bean);
        }
    }
}