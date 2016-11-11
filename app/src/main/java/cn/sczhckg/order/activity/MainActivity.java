package cn.sczhckg.order.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.UserLoginBean;
import cn.sczhckg.order.data.listener.OnButtonClickListener;
import cn.sczhckg.order.data.listener.OnDishesChooseListener;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.fragment.BaseFragment;
import cn.sczhckg.order.fragment.MainFragment;
import cn.sczhckg.order.fragment.PotTypeFagment;
import cn.sczhckg.order.fragment.ShoppingCartFragment;
import cn.sczhckg.order.overwrite.NoScrollViewPager;
import cn.sczhckg.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:主界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity implements Callback<MainPagerShow>, OnDishesChooseListener, OnButtonClickListener {

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
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.parent_show_back)
    LinearLayout parentShowBack;
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
     * 购物车
     */
    private ShoppingCartFragment mShoppingCartFragment;

    private OnDishesChooseListener onDishesChooseListener;

    /**
     * 用餐人数
     */
    public static int person;
    /**
     * 桌号
     */
    public static String table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setOnDishesChooseListener(this);
        isLogin();
        init();
        initLeftFragment();
    }

    /**
     * 判断是否登录，登录刷新个人信息
     */
    private void isLogin() {
        deviceId = AppSystemUntil.getAndroidID(this);
        if (MyApplication.isLogin) {
            UserLoginBean bean = (UserLoginBean) getIntent().getExtras().getSerializable("userInfo");
            userId = bean.getId();
        }
        initNetData();
    }

    /**
     * 获取网络数据
     */
    @Override
    protected void initNetData() {
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
        mShoppingCartFragment = new ShoppingCartFragment();
        mShoppingCartFragment.setOnButtonClickListener(this);
        mList.add(mShoppingCartFragment);
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
        mPotTypeFagment = new PotTypeFagment();
        mPotTypeFagment.onDishesChooseListenner(onDishesChooseListener);
        mMainFragment = new MainFragment();
        mMainFragment.setOnDishesChooseListener(onDishesChooseListener);
        mList.add(mPotTypeFagment);
        mList.add(mMainFragment);
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

    public void setOnDishesChooseListener(OnDishesChooseListener onDishesChooseListener) {
        this.onDishesChooseListener = onDishesChooseListener;
    }


    @Override
    public void dishesChoose(List<DishesBean> bean) {
        if (mShoppingCartFragment != null) {
            mShoppingCartFragment.setData(bean);
        }
    }

    @Override
    public void onClick(int type, int isShow) {
        /**开桌成功后回调*/
        if (type == Constant.ORDER) {
            viewPager.setCurrentItem(1);
            if (isShow == 0) {
                mMainFragment.showOrderType();
                parentShowBack.setVisibility(View.VISIBLE);
                parentShowTable.setVisibility(View.GONE);
            } else {
                mMainFragment.getData(BaseFragment.ALONE_ORDER);
                parentShowBack.setVisibility(View.GONE);
                parentShowTable.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        mMainFragment.getData(BaseFragment.ALONE_ORDER);
        parentShowBack.setVisibility(View.GONE);
        parentShowTable.setVisibility(View.VISIBLE);
    }
}
