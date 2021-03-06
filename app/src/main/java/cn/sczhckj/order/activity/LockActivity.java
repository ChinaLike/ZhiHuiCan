package cn.sczhckj.order.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.mode.impl.FloatButtonImpl;

/**
 * 锁屏界面
 */
public class LockActivity extends Activity {

    @Bind(R.id.lock_title)
    TextView lockTitle;
    @Bind(R.id.float_btn)
    FloatingActionButton floatBtn;

    private FloatButtonImpl mFloatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mFloatButton = new FloatButtonImpl(this);
        setTitle();
        initFloatBtn();
    }

    private void setTitle() {
        String title = getIntent().getExtras().getString(Constant.LOCK_TITLE,
                getString(R.string.lock_activity_hint, MyApplication.tableBean.getTableName()));
        lockTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 关闭界面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        switch (event.getType()) {
            case WebSocketEvent.TYPE_UNLOCK:
                /**普通直接解锁*/
                finish();
                break;
            case WebSocketEvent.TYPE_BILL_FINISH:
                /**结账完成*/
                finish();
                break;
        }
    }

    @OnClick(R.id.float_btn)
    public void onClick() {
        mFloatButton.show();
    }

    /**
     * 初始化浮动按钮
     */
    private void initFloatBtn() {
        if (MyApplication.mode == Constant.PRODUCER) {
            floatBtn.setVisibility(View.VISIBLE);
        } else {
            floatBtn.setVisibility(View.GONE);
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
}
