package cn.sczhckj.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
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
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.push.PushCommonBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnButtonClickListener;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
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
     * 通过WebSocket与客户端建立连接
     */
    private WebSocketImpl mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        /**注册WebSocket*/
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

    @Override
    protected void init() {
        mDialog = new DialogImpl(this);
        personNumber = 0;
        initViewPager();
    }

    private void initViewPager() {
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        adapter.setList(initRightFragment());
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
        mBillFragment = new BillFragment();
        mList.add(mBillFragment);
        /**优惠列表*/
        mFavorableFragment = new FavorableFragment();
        mList.add(mFavorableFragment);

        adapter.setList(mList);
        cart.setAdapter(adapter);
    }


    /**
     * 初始化右侧数据
     *
     * @return
     */
    private List<Fragment> initRightFragment() {
        List<Fragment> mList = new ArrayList<>();
        /**锅底必选*/
        mRequiredFagment = new RequiredFagment();
        mRequiredFagment.setOnTableListenner(this);
        mRequiredFagment.setUserId(userId);
        /**菜品选择主页*/
        mMainFragment = new MainFragment();
        /**菜品详情*/
        mDetailsFragment = new DetailsFragment();
        /**办卡界面*/
        mCardFragment = new CardFragment();

        mList.add(mRequiredFagment);
        mList.add(mMainFragment);
        mList.add(mDetailsFragment);
        mList.add(mCardFragment);
        /**预加载所有*/
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
        /**人数清零*/
        personNumber = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**Activity重新获取焦点时，开启Glide请求*/
        Glide.with(getApplicationContext()).resumeRequests();
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

    /**
     * 界面切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchViewEventBus(SwitchViewEvent event) {
        switch (event.getType()) {
            case SwitchViewEvent.BOTTOM_ORDER:
                /**点菜*/
                tableInfoParent.setVisibility(View.VISIBLE);
                cart.setCurrentItem(CART_DISHES, false);
                break;
            case SwitchViewEvent.BOTTOM_SERVICE:
                /**服务*/
                tableInfoParent.setVisibility(View.VISIBLE);
                break;
            case SwitchViewEvent.BOTTOM_BILL:
                /**结账*/
                tableInfoParent.setVisibility(View.VISIBLE);
                cart.setCurrentItem(CART_BILL, false);
                mBillFragment.setData(null);
                break;
            case SwitchViewEvent.DISHES_DETAILS_IN:
                /**进入菜品详情*/
                tableInfoParent.setVisibility(View.GONE);
                viewPager.setCurrentItem(FRAGMENT_DETAILS, false);
                mDetailsFragment.setData(event.getBean());
                mDetailsFragment.setBeanList(event.getBeanList());
                break;
            case SwitchViewEvent.DISHES_DETAILS_OUT:
                /**退出菜品详情*/
                tableInfoParent.setVisibility(View.VISIBLE);
                if (BaseFragment.isOpen) {
                    viewPager.setCurrentItem(FRAGMENT_MAIN, false);
                } else {
                    viewPager.setCurrentItem(FRAGMENT_REQUIRED, false);
                }
                break;
            case SwitchViewEvent.FAVORABLE:
                /**更多优惠*/
                cart.setCurrentItem(CART_FAVORABLE,false);
                viewPager.setCurrentItem(FRAGMENT_CARD,false);
                break;
        }

    }

}
