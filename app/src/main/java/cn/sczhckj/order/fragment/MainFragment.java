package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.overwrite.MyDialog;
import cn.sczhckj.order.until.show.L;

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
    @Bind(R.id.main_hint_choose)
    LinearLayout mainHintChoose;
    @Bind(R.id.main_bottom_viewPager)
    FrameLayout mainBottomViewPager;

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
     * 评价
     */
    private EvaluateFragment mEvaluateFragment;

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
        if (isConsuming()) {
            if (MyApplication.tableBean.getStatus() == Constant.TABLE_STATUS_BILL) {
                index = 2;
            } else {
                index = 0;
            }
            current = index;
        }
        init();
        setBottomBtn(false);
        /**获取点菜方式*/
        getOrderType();

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
        if (index == 0) {
//            showOrderType((Integer) MyApplication.mStorage.getData(Constant.STORAGR_SHOW_TYPE, Constant.DIS_SHOW_TYPE));
            showOrderType(MyApplication.tableBean.getIsShow());
        } else if (index == 1) {
            initServiceFragment();
        } else if (index == 2) {
            initBillFragment();
        }
    }

    /**
     * 初始化点菜界面
     */
    public void initOrderFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mOrderFragment == null) {
            mOrderFragment = new OrderFragment();
            transaction.add(R.id.main_bottom_viewPager, mOrderFragment);
        }
        hideFragment(transaction);
        transaction.show(mOrderFragment);
        transaction.commit();
    }

    /**
     * 初始化服务界面
     */
    public void initServiceFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mServiceFragment == null) {
            mServiceFragment = new ServiceFragment();
            transaction.add(R.id.main_bottom_viewPager, mServiceFragment);
        }
        hideFragment(transaction);
        transaction.show(mServiceFragment);
        transaction.commit();
    }

    /**
     * 初始化结账界面
     */
    public void initBillFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mEvaluateFragment == null) {
            mEvaluateFragment = new EvaluateFragment();
            transaction.add(R.id.main_bottom_viewPager, mEvaluateFragment);
        }
        hideFragment(transaction);
        transaction.show(mEvaluateFragment);
        transaction.commit();
    }

    /**
     * 隐藏界面
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mOrderFragment != null) {
            transaction.hide(mOrderFragment);
        }
        if (mServiceFragment != null) {
            transaction.hide(mServiceFragment);
        }
        if (mEvaluateFragment != null) {
            transaction.hide(mEvaluateFragment);
        }
    }

    /**
     * 是否显示点菜方式
     */
    public void showOrderType(int showType) {
        if (showType == Constant.SHOW_TYPE) {
            mainHintChoose.setVisibility(View.VISIBLE);
            mainBottomViewPager.setVisibility(View.GONE);
        } else {
            setBottomBtn(true);
            mainHintChoose.setVisibility(View.GONE);
            mainBottomViewPager.setVisibility(View.VISIBLE);
            initOrderFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
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
                if (index != current) {
                    initOrderFragment();
                    EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.BOTTOM_ORDER));
                }
                break;
            case R.id.main_service:
                index = 1;
                if (index != current) {
                    initServiceFragment();
                    EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.BOTTOM_SERVICE));
                }
                break;
            case R.id.main_settle_accounts:
                index = 2;
                if (index != current) {
                    initBillFragment();
                    EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.BOTTOM_BILL));
                }
                break;
            case R.id.main_alone_order:
                /**单桌点菜*/
                setBottomBtn(true);
                orderType = Constant.ORDER_TYPE_ALONE;
                showOrderType(Constant.DIS_SHOW_TYPE);
                /**如果已经点击，再次进来不显示点菜方式*/
//                MyApplication.mStorage.setData(Constant.STORAGR_SHOW_TYPE, Constant.DIS_SHOW_TYPE);
                MyApplication.tableBean.setOrderType(Constant.DIS_SHOW_TYPE);
                /**设置已选择点菜*/
                BaseFragment.setOrderType(orderType);
                break;
            case R.id.main_merger_order:
                /**并桌点餐*/
                dialog();
                break;
        }
        if (index != current) {
            imageViews[index].setSelected(true);
            textViews[index].setSelected(true);
            imageViews[current].setSelected(false);
            textViews[current].setSelected(false);
            current = index;
        }

    }

    /**
     * 设置底部菜单是否可点击
     *
     * @param isClick
     */
    private void setBottomBtn(boolean isClick) {
        mainOrder.setClickable(isClick);
        mainService.setClickable(isClick);
        mainSettleAccounts.setClickable(isClick);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 并桌点餐提示框
     */
    private void dialog() {
        final MyDialog dialog = new MyDialog(mContext);
        dialog.setTitle(mContext.getString(R.string.dialog_title));
        dialog.setContextText(mContext.getString(R.string.dialog_merge));
        dialog.setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomBtn(true);
                orderType = Constant.ORDER_TYPE_MERGE;
                showOrderType(Constant.DIS_SHOW_TYPE);
                /**如果已经点击，再次进来不显示点菜方式*/
//                MyApplication.mStorage.setData(Constant.STORAGR_SHOW_TYPE, Constant.DIS_SHOW_TYPE);
                MyApplication.tableBean.setOrderType(Constant.DIS_SHOW_TYPE);
                /**设置已选择点菜*/
                BaseFragment.setOrderType(orderType);
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
