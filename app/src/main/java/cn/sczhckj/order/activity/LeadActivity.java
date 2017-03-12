package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.until.show.L;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:引导界面，提供用户VIP登录，用户可选择登录，也可不选择登录，不登录路直接跳转选菜界面，如果
 * 想要登录就跳转登录界面。
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */

public class LeadActivity extends Activity implements Callback<Bean<TableBean>> {

    @Bind(R.id.right)
    Button right;
    @Bind(R.id.deny)
    Button deny;
    @Bind(R.id.lead_welcome)
    TextView leadWelcome;
    @Bind(R.id.lead_progress)
    ProgressBar leadProgress;
    @Bind(R.id.lead_isVip)
    TextView leadIsVip;
    @Bind(R.id.activity_lead)
    LinearLayout activityLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (MyApplication.mode == Constant.CUSTOMER) {
            initTableStatus(MyApplication.tableBean.getStatus(), MyApplication.tableBean.getRemark());
        }

        activityLead.setClickable(false);
    }

    @OnClick({R.id.right, R.id.deny})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                /**选择是，即先登录*/
                initButton(right, true);
                initButton(deny, false);
                intent = new Intent(LeadActivity.this, LoginActivity.class);
                intent.putExtra(Constant.INTENT_FLAG, Constant.LEAD_TO_LOGIN);
                startActivity(intent);
                break;
            case R.id.deny:
                /**暂时未登录*/
                initButton(deny, true);
                initButton(right, false);
                intent = new Intent(LeadActivity.this, MainActivity.class);
                intent.putExtra(Constant.INTENT_FLAG, Constant.TABLE_STATUS_OTHER);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initButton(deny, false);
        initButton(right, false);
        if (MyApplication.mode == Constant.PRODUCER) {
            initTableStatus(MyApplication.tableBean.getStatus(), MyApplication.tableBean.getRemark());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 按键状态
     *
     * @param btn
     * @param isSelect
     */
    private void initButton(Button btn, boolean isSelect) {
        btn.setSelected(isSelect);
        if (isSelect) {
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_color_main_select));
        } else {
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //监控/拦截/屏蔽返回键
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理台桌状态
     *
     * @param status
     * @param message
     */
    private void initTableStatus(Integer status, String message) {
        Intent intent = new Intent(LeadActivity.this, MainActivity.class);
        switch (status) {
            case Constant.TABLE_STATUS_DISABLE:
                /**不可用*/
            case Constant.TABLE_STATUS_NO_OPEN:
                /**未开台*/
            case Constant.TABLE_STATUS_SWEEP:
                /**打扫中*/
            case Constant.TABLE_STATUS_RESERVE:
                /**预定*/
                intentLock(status, message);
                break;
            case Constant.TABLE_STATUS_EMPTY:
                /**空桌*/
                break;
            case Constant.TABLE_STATUS_OPEN:
                /**已开桌*/
            case Constant.TABLE_STATUS_FOOD:
                /**已上菜*/
                intent.putExtra(Constant.INTENT_FLAG, Constant.TABLE_STATUS_OPEN);
                startActivity(intent);
                break;
            case Constant.TABLE_STATUS_BILL:
                /**结帐中*/
                intent.putExtra(Constant.INTENT_FLAG, Constant.TABLE_STATUS_BILL);
                intent.putExtra(Constant.INTENT_TABLE_REMARK, message);
                startActivity(intent);
                break;
            default:
                /**其他*/
                break;
        }
    }

    /**
     * 跳转锁定界面
     *
     * @param status  状态
     * @param message 显示消息
     */
    private void intentLock(Integer status, String message) {
        Intent intent = new Intent(LeadActivity.this, LockActivity.class);
        intent.putExtra(Constant.LOCK_TITLE, message);
        intent.putExtra(Constant.INTENT_TABLE_STATUS, status);
        startActivity(intent);
    }

    /**
     * 初始化台桌信息
     */
    private void initTable() {
        leadIsVip.setText("换桌中,请稍等...");
        right.setClickable(false);
        deny.setClickable(false);
        TableMode mTableMode = new TableMode();
        mTableMode.tableInit(LeadActivity.this, this);
    }

    /**
     * 通知事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        switch (event.getType()) {
            case WebSocketEvent.EXCHANGE_TABLE:
                /**换桌*/
                initTable();
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<TableBean>> call, Response<Bean<TableBean>> response) {
        Bean<TableBean> bean = response.body();

        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            L.d("数据初始化：Lead="+bean.getResult().toString());
            activityLead.setClickable(false);
            leadIsVip.setText(getString(R.string.lead_activity_is_vip));
            right.setClickable(true);
            deny.setClickable(true);
            MyApplication.tableBean = bean.getResult();
            MyApplication.mode = bean.getResult().getMode();
            if (bean.getResult().getMode() == Constant.CUSTOMER) {
                /**消费者模式*/
                initTableStatus(bean.getResult().getStatus(), bean.getResult().getRemark());
            }
        } else {
            right.setClickable(false);
            deny.setClickable(false);
            leadIsVip.setText("换桌失败，点击空白区域重新获取数据...");
            activityLead.setClickable(true);
            activityLead.setOnClickListener(listener);
        }
    }

    @Override
    public void onFailure(Call<Bean<TableBean>> call, Throwable t) {
        leadIsVip.setText("换桌失败，点击空白区域重新获取数据...");
        right.setClickable(false);
        deny.setClickable(false);
        activityLead.setClickable(true);
        activityLead.setOnClickListener(listener);
    }

    /**
     * 换桌失败，重新加载
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initTable();
        }
    };

}
