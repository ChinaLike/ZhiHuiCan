package cn.sczhckj.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.Config;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.ViewPagerAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.bean.FavorableTypeBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.PayTypeBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.push.PushCommonBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.event.ApplyForVipCardEvent;
import cn.sczhckj.order.data.event.BottomChooseEvent;
import cn.sczhckj.order.data.event.CloseServiceEvent;
import cn.sczhckj.order.data.event.SettleAountsTypeEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnButtonClickListener;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.fragment.ApplyForVipCardFragment;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.fragment.CartFragment;
import cn.sczhckj.order.fragment.DetailsFragment;
import cn.sczhckj.order.fragment.EvaluateFragment;
import cn.sczhckj.order.fragment.EvaluateListFragment;
import cn.sczhckj.order.fragment.TipFragment;
import cn.sczhckj.order.fragment.GrouponFragment;
import cn.sczhckj.order.fragment.MainFragment;
import cn.sczhckj.order.fragment.BillSuccessFragment;
import cn.sczhckj.order.fragment.RequiredFagment;
import cn.sczhckj.order.fragment.SettleAccountsCartFragment;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.DialogImpl;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.overwrite.NoScrollViewPager;
import cn.sczhckj.order.overwrite.RoundImageView;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:主界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity implements OnButtonClickListener, OnTableListenner,
        Callback<Bean<ResponseCommonBean>>, OnWebSocketListenner {

    /**
     * Item=0，放置开桌锅底选择，推荐菜品；Item=1，点菜主界面
     */
    @Bind(R.id.viewPager)
    NoScrollViewPager viewPager;
    @Bind(R.id.table_number)
    TextView tableName;
    @Bind(R.id.waitress)
    TextView waitress;
    @Bind(R.id.no_login)
    ImageView noLogin;
    @Bind(R.id.has_login)
    LinearLayout hasLogin;
    @Bind(R.id.cart)
    NoScrollViewPager cart;
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
    /**
     * 事物管理器
     */
    private FragmentManager mFm;
    /**
     * 适配Adapter
     */
    private ViewPagerAdapter adapter;

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
     * 菜品评价列表
     */
    private EvaluateListFragment mEvaluateListFragment;

    /**
     * 购物车
     */
    private CartFragment mCartFragment;

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
    private TipFragment mTipFragment;
    /**
     * 二维码支付界面
     */
    private BillSuccessFragment mBillSuccessFragment;
    /**
     * 评价
     */
    private EvaluateFragment mEvaluateFragment;
    /**
     * 菜品详情
     */
    private DetailsFragment mDetailsFragment;
    /**
     * 申请办理VIP
     */
    private ApplyForVipCardFragment mApplyForVipCardFragment;
    /**
     * 桌号
     */
    public static String table;

    private int favorableType = 0;

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
     * 通过WebSocket与客户端建立连接
     */
    private WebSocketImpl mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        connectionWebSocket(AppSystemUntil.getAndroidID(this));

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
            MemberBean bean = (MemberBean) getIntent().getExtras().getSerializable(Constant.USER_INFO);
            userId = bean.getId() + "";
            login(bean);
        }
    }

    /**
     * 获取网络数据
     */
    @Override
    protected void initNetData() {

    }

    @Override
    protected void init() {
        mDialog = new DialogImpl(this);
        personNumber = 0;
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        adapter.setList(initFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * 与服务器通过WebSocket连接
     */
    private void connectionWebSocket(String deviceId) {
        mWebSocket = new WebSocketImpl();
        /**连接菜品完成推送*/
        mWebSocket.push(Config.URL_FOOD_SERVICE + deviceId, this);
        /**完成服务终止推送*/
        mWebSocket.push(Config.URL_SERVICE_SERVICE + deviceId, this);
    }

    /**
     * 初始化左侧数据
     */
    private void initLeftFragment() {
        List<Fragment> mList = new ArrayList<>();
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        /**购物车*/
        mCartFragment = new CartFragment();
        mCartFragment.setOnButtonClickListener(this);
        mList.add(mCartFragment);
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
        mRequiredFagment = new RequiredFagment();
        mRequiredFagment.setOnTableListenner(this);
        mRequiredFagment.setUserId(userId);
        /**菜品选择主页*/
        mMainFragment = new MainFragment();
        /**团购券*/
        mGrouponFragment = new GrouponFragment();
        /**打赏*/
        mTipFragment = new TipFragment();
        /**二维码*/
        mBillSuccessFragment = new BillSuccessFragment();
        /**评价*/
        mEvaluateFragment = new EvaluateFragment();
        /**评价详情*/
        mDetailsFragment = new DetailsFragment();
        /**申请办理VIP*/
        mApplyForVipCardFragment = new ApplyForVipCardFragment();

        mList.add(mRequiredFagment);
        mList.add(mMainFragment);
        mList.add(mGrouponFragment);
        mList.add(mTipFragment);
        mList.add(mBillSuccessFragment);
        mList.add(mEvaluateFragment);
        mList.add(mDetailsFragment);
        mList.add(mApplyForVipCardFragment);

        viewPager.setOffscreenPageLimit(mList.size());

        return mList;
    }


    @Override
    public void onClick(int type, int isShow) {
        if (tableInfoParent.getVisibility() == View.GONE) {
            tableInfoParent.setVisibility(View.VISIBLE);
        }
        /**开桌成功后回调*/
        if (type == Constant.ORDER) {
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
            if (isShow == 0) {
                mMainFragment.showOrderType();
            } else {
                mMainFragment.getData();
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
        /**销毁时关闭服务*/
        EventBus.getDefault().post(new CloseServiceEvent());
        /**人数清零*/
        personNumber = 0;
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
//            mSettleAccountsCartFragment.showPop();
        } else if (event.getType() == Constant.DISHES_DETAILS_IN) {
            /**进入菜品详情*/
            tableInfoParent.setVisibility(View.GONE);
            viewPager.setCurrentItem(FRAGMENT_DETAILS, false);
            mDetailsFragment.setData(event.getBean());
            mDetailsFragment.setBeanList(event.getBeanList());
        } else if (event.getType() == Constant.DISHES_DETAILS_OUT) {
            /**退出菜品详情*/
            if (BaseFragment.isOpen) {
                viewPager.setCurrentItem(FRAGMENT_MAIN, false);
            } else {
                viewPager.setCurrentItem(FRAGMENT_POT_TYPE, false);
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
                    intent.putExtra(Constant.INTENT_FLAG, Constant.MAIN_TO_LOGIN);
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
            mBillSuccessFragment.getCode(id, favorableType, mGrouponFragment.getGrouponList(), mSettleAccountsCartFragment.getGiftMoney());
            if (id == 0) {
                /**现金*/
                mBillSuccessFragment.setData(getResources().getString(R.string.cash_title));
            } else if (id == 1) {
                /**微信*/
                mBillSuccessFragment.setData(getResources().getString(R.string.weixin_pay_title));
            } else if (id == 2) {
                /**银行卡*/
                mBillSuccessFragment.setData(getResources().getString(R.string.bank_card_title));
            } else if (id == 3) {
                /**支付宝*/
                mBillSuccessFragment.setData(getResources().getString(R.string.aliPay_title));
            }
        } else if (event.getType() == SettleAountsTypeEvent.GTYPE) {
            /**打赏*/
            viewPager.setCurrentItem(FRAGMENT_GIFT, false);
        } else if (event.getType() == SettleAountsTypeEvent.TTYPE) {
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
        } else if (event.getType() == SettleAountsTypeEvent.ETYPE) {
            viewPager.setCurrentItem(FRAGMENT_EVALUATE, false);
            cart.setCurrentItem(CART_EVALUATE_LIST, false);
            mEvaluateListFragment.setData(mMainFragment.getBillFragment().getBillList());
        }
    }

    /**
     * 申请办理会员卡
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void applyForVipEventBus(ApplyForVipCardEvent event) {
        if (event.getType() == event.APPLY) {
            /**申请办理VIP*/
            viewPager.setCurrentItem(APPLY_FOR_VIP_CARD, false);
            if (mApplyForVipCardFragment != null) {
                mApplyForVipCardFragment.setData(event.getmList());
            }
        } else if (event.getType() == event.CANCEL_APPLY) {
            /**取消办理VIP*/
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
        } else if (event.getType() == event.CLOSE) {
            /**取消办理VIP*/
            viewPager.setCurrentItem(FRAGMENT_MAIN, false);
        }
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
            }
        } else {
            T.showShort(this, bean.getMessage());
        }
    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        T.showShort(this, "设置失败，请重新设置！");
    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onClose(int code, String reason) {
        Log.d("ws=====", "reason:" + reason);
        /**断开后尝试再次连接*/
//        connectionWebSocket(AppSystemUntil.getAndroidID(this));
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onTextMessage(String payload) {
        RestRequest<PushCommonBean> restRequest
                = JSONRestRequest.Parser.parse(payload, PushCommonBean.class);
        if (OP.PUSH_ARRIVE.equals(restRequest.getOp())) {
            /**菜品完成*/
            EventBus.getDefault().post(
                    new WebSocketEvent(WebSocketEvent.TYPE_FOOD_ARRIVE, restRequest.getBean()));
        } else if (OP.PUSH_COMPLETE.equals(restRequest.getOp())) {
            /**服务完成*/
            EventBus.getDefault().post(
                    new WebSocketEvent(WebSocketEvent.TYPE_SERVICE_COMPLETE, restRequest.getBean()));
        }
    }
}
