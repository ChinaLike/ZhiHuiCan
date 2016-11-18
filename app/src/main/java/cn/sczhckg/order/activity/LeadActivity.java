package cn.sczhckg.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.until.ConvertUtils;

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

        Log.d("单位转换：","px2dip="+ ConvertUtils.px2dip(this,245));
        Log.d("单位转换：","dip2px="+ ConvertUtils.dip2px(this,100));
        Log.d("单位转换：","sp2px="+ ConvertUtils.sp2px(this,100));
        Log.d("单位转换：","px2sp="+ ConvertUtils.px2sp(this,100));

    }

    @OnClick({R.id.right, R.id.deny})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                intent = new Intent(LeadActivity.this, LoginActivity.class);
                intent.setFlags(Constant.LEAD_TO_LOGIN);
                startActivity(intent);
                break;
            case R.id.deny:
                intent = new Intent(LeadActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
