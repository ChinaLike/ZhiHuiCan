package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Constant;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.right, R.id.deny})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                /**选择是，即先登录*/
                right.setSelected(true);
                right.setTextColor(ContextCompat.getColor(this,R.color.text_color_main_select));
                deny.setSelected(false);
                deny.setTextColor(ContextCompat.getColor(this,R.color.white));
                intent = new Intent(LeadActivity.this, LoginActivity.class);
                intent.putExtra(Constant.INTENT_FLAG,Constant.LEAD_TO_LOGIN);
                startActivity(intent);
                break;
            case R.id.deny:
                /**暂时未登录*/
                right.setSelected(false);
                right.setTextColor(ContextCompat.getColor(this,R.color.white));
                deny.setSelected(true);
                deny.setTextColor(ContextCompat.getColor(this,R.color.text_color_main_select));
                intent = new Intent(LeadActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        right.setSelected(false);
        right.setTextColor(ContextCompat.getColor(this,R.color.white));
        deny.setSelected(false);
        deny.setTextColor(ContextCompat.getColor(this,R.color.white));
    }
}
