package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.ViewPagerAdapter;
import cn.sczhckg.order.data.listener.OnDishesChooseListener;

/**
 * @describe: 开桌后主界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class MainFragment extends BaseFragment {

    @Bind(R.id.main_order_img)
    ImageView mainOrderImg;
    @Bind(R.id.main_order_text)
    TextView mainOrderText;
    @Bind(R.id.main_order)
    RelativeLayout mainOrder;
    @Bind(R.id.main_service_img)
    ImageView mainServiceImg;
    @Bind(R.id.main_service_text)
    TextView mainServiceText;
    @Bind(R.id.main_service)
    RelativeLayout mainService;
    @Bind(R.id.main_settle_accounts_img)
    ImageView mainSettleAccountsImg;
    @Bind(R.id.main_settle_accounts_text)
    TextView mainSettleAccountsText;
    @Bind(R.id.main_settle_accounts)
    RelativeLayout mainSettleAccounts;
    @Bind(R.id.main_bottom_table)
    LinearLayout mainBottomTable;
    @Bind(R.id.main_bottom_hint)
    View mainBottomHint;
    @Bind(R.id.main_alone_order)
    Button mainAloneOrder;
    @Bind(R.id.main_merger_order)
    Button mainMergerOrder;
    @Bind(R.id.main_bottom_viewPager)
    ViewPager mainBottomViewPager;
    @Bind(R.id.main_hint_choose)
    LinearLayout mainHintChoose;

    private int index = 0;

    private int current = 0;

    private final int NUMBER = 3;

    private ImageView[] imageViews = new ImageView[NUMBER];

    private TextView[] textViews = new TextView[NUMBER];
    /**
     * 点菜
     */
    private OrderFragment mOrderFragment;
    /**
     * 服务
     */
    private ServiceFragment mServiceFragment;
    /**
     * 结账
     */
    private SettleAccountsFragment mSettleAccountsFragment;

    private FragmentManager mFm;

    private List<Fragment> fragmentList = new ArrayList<>();

    private ViewPagerAdapter adapter;


    private OnDishesChooseListener onDishesChooseListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null, false);
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
        /**初始化底部菜单*/
        imageViews[0] = mainOrderImg;
        imageViews[1] = mainServiceImg;
        imageViews[2] = mainSettleAccountsImg;
        textViews[0] = mainOrderText;
        textViews[1] = mainServiceText;
        textViews[2] = mainSettleAccountsText;
        imageViews[index].setSelected(true);
        textViews[index].setSelected(true);
        /**初始化界面*/
        initOrderFragment();
        initServiceFragment();
        initSettleAccountsFragment();
        initViewPager();
    }

    /**
     * 初始化点菜界面
     */
    private void initOrderFragment() {
        mOrderFragment = new OrderFragment();
        mOrderFragment.setOnDishesChooseListener(onDishesChooseListener);
    }

    /**
     * 初始化服务界面
     */
    private void initServiceFragment() {
        mServiceFragment = new ServiceFragment();
    }

    /**
     * 初始化结账界面
     */
    private void initSettleAccountsFragment() {
        mSettleAccountsFragment = new SettleAccountsFragment();
    }

    /**
     * 加载界面初始化
     */
    private void initViewPager() {
        mFm = getChildFragmentManager();
        fragmentList.add(mOrderFragment);
        fragmentList.add(mServiceFragment);
        fragmentList.add(mSettleAccountsFragment);
        adapter = new ViewPagerAdapter(mFm);
        adapter.setList(fragmentList);
        mainBottomViewPager.setAdapter(adapter);
        /**预加载所有*/
        mainBottomViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 显示点菜方式
     */
    public void showOrderType() {
        mainHintChoose.setVisibility(View.VISIBLE);
        mainBottomViewPager.setVisibility(View.GONE);
    }

    /**
     * 加载网络数据
     */
    public void getData(int orderType) {
        mainHintChoose.setVisibility(View.GONE);
        mainBottomViewPager.setVisibility(View.VISIBLE);
        if (mOrderFragment != null) {
            mOrderFragment.loadingClassify(orderType);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.main_order, R.id.main_service, R.id.main_settle_accounts, R.id.main_alone_order, R.id.main_merger_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_order:
                index = 0;
                break;
            case R.id.main_service:
                index = 1;
                break;
            case R.id.main_settle_accounts:
                index = 2;
                if (mSettleAccountsFragment!=null) {
                    mSettleAccountsFragment.getData();
                }
                break;
            case R.id.main_alone_order:
                orderType=ALONE_ORDER;
                getData(ALONE_ORDER);
                break;
            case R.id.main_merger_order:
                orderType=MERGER_ORDER;
                getData(MERGER_ORDER);
                break;
        }

        imageViews[index].setSelected(true);
        textViews[index].setSelected(true);
        if (index != current) {
            mainBottomViewPager.setCurrentItem(index);
            imageViews[current].setSelected(false);
            textViews[current].setSelected(false);
            current = index;
        }

    }

    public void setOnDishesChooseListener(OnDishesChooseListener onDishesChooseListener) {
        this.onDishesChooseListener = onDishesChooseListener;
    }
}
