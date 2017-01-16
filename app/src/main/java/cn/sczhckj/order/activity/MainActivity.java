package cn.sczhckj.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.fragment.BillFragment;
import cn.sczhckj.order.fragment.CardFragment;
import cn.sczhckj.order.fragment.CartFragment;
import cn.sczhckj.order.fragment.DetailsFragment;
import cn.sczhckj.order.fragment.FavorableFragment;
import cn.sczhckj.order.fragment.MainFragment;
import cn.sczhckj.order.fragment.RequiredFagment;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.DialogImpl;
import cn.sczhckj.order.overwrite.RoundImageView;
import cn.sczhckj.order.service.HeartService;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:主界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity implements OnTableListenner,
        Callback<Bean<ResponseCommonBean>> {


    @Bind(R.id.table_number)
    TextView tableName;
    @Bind(R.id.waitress)
    TextView waitress;
    @Bind(R.id.no_login)
    ImageView noLogin;
    @Bind(R.id.has_login)
    LinearLayout hasLogin;
    @Bind(R.id.table_info_parent)
    RelativeLayout tableInfoParent;
    @Bind(R.id.header)
    RoundImageView header;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.vip_grade)
    ImageView vipGrade;
    @Bind(R.id.table_person_num)
    TextView tablePersonNum;
    @Bind(R.id.table_person_parent)
    LinearLayout tablePersonParent;
    @Bind(R.id.cart_area)
    FrameLayout cartArea;
    @Bind(R.id.content_area)
    FrameLayout contentArea;
    /**
     * 用户ID
     */
    private String userId = "";
    /**
     * 锅底选择，推荐菜品
     */
    private RequiredFagment mRequiredFagment;
    /**
     * 主要导航界面
     */
    private MainFragment mMainFragment;

    /**
     * 购物车
     */
    private CartFragment mCartFragment;
    /**
     * 优惠列表
     */
    private FavorableFragment mFavorableFragment;

    /**
     * 办卡界面
     */
    private CardFragment mCardFragment;

    /**
     * 结账
     */
    private BillFragment mBillFragment;
    /**
     * 菜品详情
     */
    private DetailsFragment mDetailsFragment;
    /**
     * 桌号
     */
    public static String table;

    /**
     * 用餐人数
     */
    public static Integer personNumber = 0;

    /**
     * 弹窗
     */
    private DialogImpl mDialog;

    /**
     * 设置就餐人数
     */
    private int personCount = 0;
    /**
     * 左侧进入前标识
     */
    private int leftTag = CART_DISHES;
    /**
     * 右侧进入前标识
     */
    private int rightTag = FRAGMENT_REQUIRED;
    /**
     * 心跳检测
     */
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        startHeart();
        isLogin();
        init();
        disposeIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**Activity重新获取焦点时，开启Glide请求*/
        Glide.with(getApplicationContext()).resumeRequests();
    }


    /**
     * 判断是否登录，登录刷新个人信息
     */
    private void isLogin() {
        /**判断是否登录*/
        if (MyApplication.isLogin) {
            MemberBean bean = (MemberBean) getIntent().getExtras().getSerializable(Constant.USER_INFO);
            login(bean);
        }
    }

    /**
     * 处理Intent
     */
    private void disposeIntent() {
        int status = getIntent().getExtras().getInt(Constant.INTENT_FLAG, Constant.TABLE_STATUS_OTHER);
        switch (status) {
            case Constant.TABLE_STATUS_OPEN:
                /**进入消费中*/
                initMainFragment();
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.BOTTOM_ORDER));
                break;
            case Constant.TABLE_STATUS_BILL:
                /**进入结账*/
                initMainFragment();
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.BOTTOM_BILL));
                break;
        }
    }

    @Override
    protected void init() {
        mDialog = new DialogImpl(this);
        personNumber = 0;
        initCartFragment();
        initRequiredFragment();
    }

    /**
     * 初始化左侧-购物车
     */
    private void initCartFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCartFragment == null) {
            mCartFragment = new CartFragment();
            transaction.add(R.id.cart_area, mCartFragment);
        }
        hideLeftFragment(transaction);
        transaction.show(mCartFragment);
        transaction.commit();
    }

    /**
     * 初始化左侧-结账
     */
    private void initBillFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mBillFragment == null) {
            mBillFragment = new BillFragment();
            transaction.add(R.id.cart_area, mBillFragment);
        } else {
            mBillFragment.setData(null);
        }
        hideLeftFragment(transaction);
        transaction.show(mBillFragment);
        transaction.commit();
    }

    /**
     * 初始化左侧-优惠列表
     */
    private void initFavorableFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mFavorableFragment == null) {
            mFavorableFragment = new FavorableFragment();
            transaction.add(R.id.cart_area, mFavorableFragment);
        } else {
            mFavorableFragment.setData(null);
        }
        hideLeftFragment(transaction);
        transaction.show(mFavorableFragment);
        transaction.commit();
    }

    /**
     * 初始化右侧-必选菜品
     */
    private void initRequiredFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mRequiredFagment == null) {
            mRequiredFagment = new RequiredFagment();
            mRequiredFagment.setOnTableListenner(this);
            mRequiredFagment.setUserId(userId);
            transaction.add(R.id.content_area, mRequiredFagment);
        }
        hideRightFragment(transaction);
        transaction.show(mRequiredFagment);
        transaction.commit();
    }

    /**
     * 初始化右侧-菜品主页列表
     */
    private void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            transaction.add(R.id.content_area, mMainFragment);
        }
        hideRightFragment(transaction);
        transaction.show(mMainFragment);
        transaction.commit();
    }

    /**
     * 初始化右侧-菜品详情
     */
    private void initDetailsFragment(FoodBean bean, List<FoodBean> mList) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mDetailsFragment == null) {
            mDetailsFragment = new DetailsFragment();
            mDetailsFragment.setFoodBean(bean);
            mDetailsFragment.setBeanList(mList);
            transaction.add(R.id.content_area, mDetailsFragment);
        } else {
            mDetailsFragment.setFoodBean(bean);
            mDetailsFragment.setBeanList(mList);
            mDetailsFragment.setData(null);
        }
        hideRightFragment(transaction);
        transaction.show(mDetailsFragment);
        transaction.commit();
    }

    /**
     * 初始化右侧-办理会员
     */
    private void initCardFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCardFragment == null) {
            mCardFragment = new CardFragment();
            transaction.add(R.id.content_area, mCardFragment);
        } else {
            mCardFragment.setData(null);
        }
        hideRightFragment(transaction);
        transaction.show(mCardFragment);
        transaction.commit();
    }

    /**
     * 隐藏左侧Fragment
     *
     * @param transaction
     */
    private void hideLeftFragment(FragmentTransaction transaction) {
        if (mCartFragment != null) {
            transaction.hide(mCartFragment);
        }
        if (mBillFragment != null) {
            transaction.hide(mBillFragment);
        }
        if (mFavorableFragment != null) {
            transaction.hide(mFavorableFragment);
        }
    }

    /**
     * 隐藏右侧Fragment
     *
     * @param transaction
     */
    private void hideRightFragment(FragmentTransaction transaction) {
        if (mRequiredFagment != null) {
            transaction.hide(mRequiredFagment);
        }
        if (mDetailsFragment != null) {
            transaction.hide(mDetailsFragment);
        }
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if (mCardFragment != null) {
            transaction.hide(mCardFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        /**退出该界面时退出登录*/
        MyApplication.setIsLogin(false);
        /**退出时暂停Glide请求*/
        Glide.with(getApplicationContext()).pauseRequests();
        /**人数清零*/
        personNumber = 0;

        stopService(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.LOGIN_RESULT_CODE:
                if (data != null) {
                    MemberBean bean = (MemberBean) data.getExtras().get(Constant.USER_INFO);
                    login(bean);
                }
                break;
        }
    }

    /**
     * 如果判断已经有登录信息了，显示登录状态
     */
    private void login(MemberBean bean) {
        userId = bean.getId() + "";
        hasLogin.setVisibility(View.VISIBLE);
        noLogin.setVisibility(View.GONE);
        /**加载头像*/
        GlideLoading.loadingHeader(this, bean.getHeadImageUrl(), header);
        userName.setText(bean.getShortName() + "");
        GlideLoading.loadingDishes(this, bean.getMemberTypeImageUrl(), vipGrade);
    }

    /**
     * 初始化设置人数
     */
    private void initSetPerson(int number) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        bean.setNumber(number);
        bean.setRecordId(MyApplication.recordId);
        TableMode tableMode = new TableMode();
        tableMode.setPersonNum(bean, this);
    }


    @OnClick({R.id.no_login, R.id.table_person_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_login:
                /**用户登录*/
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra(Constant.INTENT_FLAG, Constant.MAIN_TO_LOGIN);
                startActivityForResult(intent, Constant.LOGIN_RESULT_CODE);
                break;
            case R.id.table_person_parent:
                /**设置台桌人数*/
                if (BaseFragment.isOpen) {
                    mDialog.editTextDialog().setInputType(InputType.TYPE_CLASS_NUMBER);
                    mDialog.setEditDialog("人数设置", null, "请输入人数")
                            .setRightButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        personCount = Integer.parseInt(mDialog.editTextDialog().getEditText().toString());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        personCount = personNumber;
                                    }
                                    initSetPerson(personCount);
                                    mDialog.editTextDialog().dismiss();
                                }
                            })
                            .setLeftButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.editTextDialog().dismiss();
                                }
                            }).show();
                }
                break;
        }
    }

    @Override
    public void table(String tableName) {
        table = tableName;
        this.tableName.setText(tableName);
    }

    @Override
    public void waiter(String waiter) {
        this.waitress.setText(waiter);
    }

    @Override
    public void person(int number) {
        personNumber = number;
        this.tablePersonNum.setText(number + "人");
    }

    @Override
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
        Bean<ResponseCommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            person(personCount);
            /**设置菜品过多提醒*/
            if (bean.getResult() != null && bean.getResult().getFoodCountHint() != null) {
                BaseFragment.warmPromptNumber = bean.getResult().getFoodCountHint();
                MyApplication.mStorage.setData(Constant.STORAGR_HINT, BaseFragment.warmPromptNumber);
            }
        } else {
            T.showShort(this, bean.getMessage());
        }
    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        T.showShort(this, "设置失败，请重新设置！");
    }

    /**
     * 界面切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchViewEventBus(SwitchViewEvent event) {
        switch (event.getType()) {
            case SwitchViewEvent.BOTTOM_ORDER:
                leftTag = CART_DISHES;
                rightTag = FRAGMENT_MAIN;
                /**点菜*/
                tableInfoParent.setVisibility(View.VISIBLE);
                initCartFragment();
                break;
            case SwitchViewEvent.BOTTOM_SERVICE:
                leftTag = CART_DISHES;
                rightTag = FRAGMENT_MAIN;
                /**服务*/
                tableInfoParent.setVisibility(View.VISIBLE);
                break;
            case SwitchViewEvent.BOTTOM_BILL:
                leftTag = CART_BILL;
                rightTag = FRAGMENT_MAIN;
                /**结账*/
                tableInfoParent.setVisibility(View.VISIBLE);
                initBillFragment();
                break;
            case SwitchViewEvent.DISHES_DETAILS_IN:
                leftTag = CART_DISHES;
                rightTag = FRAGMENT_DETAILS;
                /**进入菜品详情*/
                tableInfoParent.setVisibility(View.GONE);
                initDetailsFragment(event.getBean(), event.getBeanList());
                break;
            case SwitchViewEvent.DISHES_DETAILS_OUT:
                leftTag = CART_DISHES;
                /**退出菜品详情*/
                tableInfoParent.setVisibility(View.VISIBLE);
                if (BaseFragment.isOpen) {
                    rightTag = FRAGMENT_MAIN;
                    initMainFragment();
                } else {
                    rightTag = FRAGMENT_REQUIRED;
                    initRequiredFragment();
                }
                break;
            case SwitchViewEvent.FAVORABLE:
                /**更多优惠*/
                initFavorableFragment();
                initCardFragment();
                break;
            case SwitchViewEvent.FAVORABLE_OUT:
                /**更多优惠退出*/
                if (leftTag == CART_DISHES) {
                    initCartFragment();
                } else {
                    initBillFragment();
                }
                if (rightTag == FRAGMENT_REQUIRED) {
                    initRequiredFragment();
                } else if (rightTag == FRAGMENT_DETAILS) {
                    initDetailsFragment(event.getBean(), event.getBeanList());
                } else {
                    initMainFragment();
                }
                break;
            case SwitchViewEvent.FAVORABLE_CARD:
                /**办卡*/
                mCardFragment.card(event.getCard());
                break;
            case SwitchViewEvent.MAIN:
                /**主页*/
                leftTag = CART_DISHES;
                rightTag = FRAGMENT_MAIN;
                initMainFragment();
                break;
        }

    }

    /**
     * 通知事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (event.getType() == WebSocketEvent.REFRESH_USER) {
            MyApplication.setIsLogin(true);
            login(event.getBean().getUser());
        }
    }


    /**
     * 开启心跳
     */
    private void startHeart() {
        intent = new Intent(MainActivity.this, HeartService.class);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //监控/拦截/屏蔽返回键
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
