package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;

/**
 * @ Describe:引导界面，提供用户VIP登录，用户可选择登录，也可不选择登录，不登录路直接跳转选菜界面，如果
 * 想要登录就跳转登录界面。
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */

public class LeadActivity extends Activity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);
        disposeIntent();

    }

    /**
     * 处理Intent状态
     */
    private void disposeIntent() {
        int status = getIntent().getExtras().getInt(Constant.INTENT_TABLE_STATUS, Constant.TABLE_STATUS_OTHER);
        String remark = getIntent().getExtras().getString(Constant.INTENT_TABLE_REMARK,"");
        Intent intent = new Intent(LeadActivity.this, MainActivity.class);
        switch (status) {
            case Constant.TABLE_STATUS_OPEN:
                /**已开桌*/
            case Constant.TABLE_STATUS_FOOD:
                /**已上菜和已开桌处理同级*/
                intent.putExtra(Constant.INTENT_FLAG, Constant.TABLE_STATUS_OPEN);
                startActivity(intent);
                break;
            case Constant.TABLE_STATUS_BILL:
                /**结帐中*/
                intent.putExtra(Constant.INTENT_FLAG, Constant.TABLE_STATUS_BILL);
                intent.putExtra(Constant.INTENT_TABLE_REMARK,remark);
                startActivity(intent);
                break;
            case Constant.TABLE_STATUS_EMPTY:
                /**空桌*/
            default:
                /**其他*/
                break;
        }
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

}
