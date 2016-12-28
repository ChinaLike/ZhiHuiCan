package cn.sczhckj.order.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
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
        String title = getIntent().getExtras().getString(Constant.LOCK_TITLE, "此桌已经锁定");
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
        if (event.getType() == WebSocketEvent.TYPE_UNLOCK) {
            finish();
        }
    }

}
