package cn.sczhckj.order.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.WebSocketEvent;

/**
 * 锁屏界面
 */
public class LockActivity extends Activity {

    @Bind(R.id.lock_title)
    TextView lockTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setTitle();

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

}
