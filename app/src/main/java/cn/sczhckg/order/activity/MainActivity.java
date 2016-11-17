package cn.sczhckg.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.MyApplication;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.ViewPagerAdapter;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.PayTypeBean;
import cn.sczhckg.order.data.bean.UserLoginBean;
import cn.sczhckg.order.data.event.BottomChooseEvent;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;
import cn.sczhckg.order.data.listener.OnButtonClickListener;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.fragment.BaseFragment;
import cn.sczhckg.order.fragment.DetailsFragment;
import cn.sczhckg.order.fragment.EvaluateFragment;
import cn.sczhckg.order.fragment.EvaluateListFragment;
import cn.sczhckg.order.fragment.GiftFragment;
import cn.sczhckg.order.fragment.GrouponFragment;
import cn.sczhckg.order.fragment.MainFragment;
import cn.sczhckg.order.fragment.PotTypeFagment;
import cn.sczhckg.order.fragment.QRCodeFragment;
import cn.sczhckg.order.fragment.SettleAccountsCartFragment;
import cn.sczhckg.order.fragment.ShoppingCartFragment;
import cn.sczhckg.order.image.GlideLoading;
import cn.sczhckg.order.overwrite.NoScrollViewPager;
import cn.sczhckg.order.overwrite.RoundImageView;
import cn.sczhckg.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:主界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity implements Callback<MainPagerShow>, OnButtonClickListener {

    /**
     * Item=0，放置开桌锅底选择，推荐菜品；Item=1，点菜主界面
     */
    @Bind(R.id.viewPager)
    NoScrollViewPager viewPager;
    @Bind(R.id.table_number)
    TextView tableNumber;
    @Bind(R.id.waitress)
    TextView waitress;
    @Bind(R.id.no_login)
    ImageView noLogin;
    @Bind(R.id.has_login)
    LinearLayout hasLogin;
    @Bind(R.id.cart)
    NoScrollViewPager cart;
    @Bind(R.id.parent_show_table)
    LinearLayout parentShowTable;
    @Bind(R.id.table_info_parent)
    RelativeLayout tableInfoParent;
    @Bind(R.id.header)
    RoundImageView header;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.vip_grade)
    ImageView vipGrade;
    /**
     * 事物管理器
     */
    private FragmentManager mFm;
    /**
     * 适配Adapter
     */
    private ViewPagerAdapter adapter;
    /**
     * 设备ID
     */
    private String deviceId = "";
    /**
     * 用户ID
     */
    private String userId = "";
    /**
     * 锅底选择，推荐菜品
     */
    private PotTypeFagment mPotTypeFagment;
    /**
     * 主要导航界面
     */
    private MainFragment mMainFragment;

    /**
     * 菜品评价列表
     */
    private EvaluateListFragment mEvaluateListFragment;

    /**
     * 购物车
     */
    private ShoppingCartFragment mShoppingCartFragment;

    /**
     * 结账
     */
    private SettleAccountsCartFragment mSettleAccountsCartFragment;

    /**
     * 团购券
     */
    private GrouponFragment mGrouponFragment;

    /**
     * 打赏界面
     */
    private GiftFragment mGiftFragment;
    /**
     * 二维码支付界面
     */
    private QRCodeFragment mQrCodeFragment;
    /**
     * 评价
     */
    private EvaluateFragment mEvaluateFragment;
    /**
     * 菜品详情
     */
    private DetailsFragment mDetailsFragment;
    /**
     * 用餐人数
     */
    public static int person;
    /**
     * 桌号
     */
    public static String table;

    private int favorableType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        isLogin();
        init();
        initLeftFragment();
    }

    /**
     * 判断是否登录，登录刷新个人信息
     */
    private void isLogin() {
        /**判断是否登录*/
        if (MyApplication.isLogin) {
            UserLoginBean bean = (UserLoginBean) getIntent().getExtras().getSerializable(Constant.USER_INFO);
            userId = bean.getId();
            login(bean);
        }
        initNetData();
    }

    /**
     * 获取网络数据
     */
    @Override
    protected void initNetData() {
        deviceId = AppSystemUntil.getAndroidID(this);
        Call<MainPagerShow> mainShow = RetrofitRequest.service(Config.HOST).potDataShow(userId, deviceId);
        mainShow.enqueue(this);
    }

    @Override
    protected void init() {
        person = 0;
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        adapter.setList(initFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * 初始化左侧数据
     */
    private void initLeftFragment() {
        List<Fragment> mList = new ArrayList<>();
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        /**购物车*/
        mShoppingCartFragment = new ShoppingCartFragment();
        mShoppingCartFragment.setOnButtonClickListener(this);
        mList.add(mShoppingCartFragment);
        /**结账*/
        mSettleAccountsCartFragment = new SettleAccountsCartFragment();
        mList.add(mSettleAccountsCartFragment);
        /**评价列表*/
        mEvaluateListFragment = new EvaluateListFragment();
        mList.add(mEvaluateListFragment);

        adapter.setList(mList);
        cart.setAdapter(adapter);
    }


    /**
     * 初始化Fragment
     *
     * @return
     */
    private List<Fragment> initFragment() {
        List<Fragment> mList = new ArrayList<>();
        /**锅底必选*/
        mPotTypeFagment = new PotTypeFagment();
        /**菜品选择主页*/
        mMainFragment = new MainFragment();
        /**团购券*/
        mGrouponFragment = new GrouponFragment();
        /**打赏*/
        mGiftFragment = new GiftFragment();
        /**二维码*/
        mQrCodeFragment = new QRCodeFragment();
        /**评价*/
        mEvaluateFragment = new EvaluateFragment();
        /**评价详情*/
        mDetailsFragment = new DetailsFragment();

        mList.add(mPotTypeFagment);
        mList.add(mMainFragment);
        mList.add(mGrouponFragment);
        mList.add(mGiftFragment);
        mList.add(mQrCodeFragment);
        mList.add(mEvaluateFragment);
        mList.add(mDetailsFragment);

        viewPager.setOffscreenPageLimit(mList.size());

        return mList;
    }

    @Override
    public void onResponse(Call<MainPagerShow> call, Response<MainPagerShow> response) {
        MainPagerShow bean = response.body();
        /**获取数据成功*/
        if (bean.getStatus() == 0 && bean != null) {
            table = bean.getTableNumber();
            tableNumber.setText("桌号：" + table);
            waitress.setText("服务员：" + bean.getWaitress());
            if (mPotTypeFagment != null) {
                mPotTypeFagment.setData(bean);
            }
        }
    }

    @Override
    public void onFailure(Call<MainPagerShow> call, Throwable t) {
        Toast.makeText(this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(int type, int isShow) {
        /**开桌成功后回调*/
        if (type == Constant.ORDER) {
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
            if (isShow == 0) {
                mMainFragment.showOrderType();
            } else {
                mMainFragment.getData(BaseFragment.ALONE_ORDER);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        /**重置界面标识*/
        BaseFragment.flag = 0;
        /**退出该界面时退出登录*/
        MyApplication.isLogin = false;
        /**退出时暂停Glide请求*/
        Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**Activity重新获取焦点时，开启Glide请求*/
        Glide.with(getApplicationContext()).resumeRequests();
    }

    /**
     * 根据底部菜单按钮，置换左边试图
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bottomEventBus(BottomChooseEvent event) {
        tableInfoParent.setVisibility(View.VISIBLE);
        if (event.getType() == Constant.BOTTOM_ORDER) {
            cart.setCurrentItem(CART_DISHES, false);
        } else if (event.getType() == Constant.BOTTOM_SERVICE) {

        } else if (event.getType() == Constant.BOTTOM_SETTLE_ACCOUNTS) {
            cart.setCurrentItem(CART_SETTLT_AOUNTS, false);
        } else if (event.getType() == Constant.DISHES_DETAILS_IN) {
            /**进入菜品详情*/
            tableInfoParent.setVisibility(View.GONE);
            viewPager.setCurrentItem(FRAGMENT_DETAILS, false);
            mDetailsFragment.setData(event.getBean());
        } else if (event.getType() == Constant.DISHES_DETAILS_OUT) {
            /**退出菜品详情*/
            if (BaseFragment.flag == 0) {
                viewPager.setCurrentItem(FRAGMENT_POT_TYPE, false);
            } else {
                viewPager.setCurrentItem(FRAGMENT_MAIN, false);
            }
        }
    }

    /**
     * 支付方式信息录入
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void settleAountsTypeEvent(SettleAountsTypeEvent event) {
        if (event.getType() == SettleAountsTypeEvent.FTYPE) {
            FavorableTypeBean favorableTypeBean = event.getFavorableTypeBean();
            int id = favorableTypeBean.getId();
            if (id == 0) {
                /**会员*/
                if (!MyApplication.isLogin) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Constant.MAIN_TO_LOGIN);
                    startActivityForResult(intent, Constant.LOGIN_RESULT_CODE);
                } else {
                    favorableType = favorableTypeBean.getId();
                }
            } else if (id == 1) {
                /**店内促销*/
                favorableType = favorableTypeBean.getId();

            } else if (id == 2) {
                /**团购券*/
                favorableType = favorableTypeBean.getId();
                viewPager.setCurrentItem(FRAGMENT_GROUPON, false);
            }
        } else if (event.getType() == SettleAountsTypeEvent.PTYPE) {
            PayTypeBean payTypeBean = event.getPayTypeBean();
            int id = payTypeBean.getId();
            viewPager.setCurrentItem(FRAGMENT_QRCODE, false);
            mQrCodeFragment.getCode(id, favorableType, mGrouponFragment.getGrouponList(), mSettleAccountsCartFragment.getGiftMoney());
            if (id == 0) {
                /**现金*/
                mQrCodeFragment.setData(getResources().getString(R.string.cash_title));
            } else if (id == 1) {
                /**微信*/
                mQrCodeFragment.setData(getResources().getString(R.string.weixin_pay_title));
            } else if (id == 2) {
                /**银行卡*/
                mQrCodeFragment.setData(getResources().getString(R.string.bank_card_title));
            } else if (id == 3) {
                /**支付宝*/
                mQrCodeFragment.setData(getResources().getString(R.string.aliPay_title));
            }
        } else if (event.getType() == SettleAountsTypeEvent.GTYPE) {
            /**打赏*/
            viewPager.setCurrentItem(FRAGMENT_GIFT, false);
        } else if (event.getType() == SettleAountsTypeEvent.TTYPE) {
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
        } else if (event.getType() == SettleAountsTypeEvent.ETYPE) {
            viewPager.setCurrentItem(FRAGMENT_EVALUATE, false);
            cart.setCurrentItem(CART_EVALUATE_LIST, false);
            mEvaluateListFragment.setData(mMainFragment.getmSettleAccountsFragment().getmList());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.LOGIN_RESULT_CODE:
                if (data != null) {
                    UserLoginBean bean = (UserLoginBean) data.getExtras().get(Constant.USER_INFO);
                    login(bean);
                }
                break;
        }
    }

    /**
     * 如果判断已经有登录信息了，显示登录状态
     */
    private void login(UserLoginBean bean) {
        if (!bean.getMsg().equals("") && bean.getMsg() != null) {
            Toast.makeText(this, bean.getMsg() + "", Toast.LENGTH_SHORT).show();
        }
        hasLogin.setVisibility(View.VISIBLE);
        noLogin.setVisibility(View.GONE);
        /**加载头像*/
        GlideLoading.loadingHeader(this, bean.getUrl(), header);
        userName.setText(bean.getName() + "");
        GlideLoading.loadingDishes(this, bean.getVipUrl(), vipGrade);
    }


    @OnClick(R.id.no_login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_login:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Constant.MAIN_TO_LOGIN);
                startActivityForResult(intent, Constant.LOGIN_RESULT_CODE);
                break;
        }
    }
}
