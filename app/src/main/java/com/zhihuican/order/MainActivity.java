package com.zhihuican.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhihuican.order.activity.PotTypeActivity;
import com.zhihuican.order.until.AppSystemUntil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    @Bind(R.id.right)
    Button right;
    @Bind(R.id.deny)
    Button deny;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    @Bind(R.id.vip_login)
    RelativeLayout vipLogin;
    @Bind(R.id.welcome)
    LinearLayout welcome;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.login)
    ImageView login;
    @Bind(R.id.inputCard)
    EditText inputCard;
    @Bind(R.id.vip_card_close)
    ImageView vipCardClose;
    @Bind(R.id.inputPassword)
    EditText inputPassword;
    @Bind(R.id.vip_password_close)
    ImageView vipPasswordClose;
    @Bind(R.id.confir)
    Button confir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.right, R.id.deny, R.id.back, R.id.login, R.id.vip_card_close, R.id.vip_password_close,R.id.confir})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                /**会员进入*/
                welcome.setVisibility(View.GONE);
                vipLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.deny:
                /**非会员进入*/
                intent = new Intent(MainActivity.this, PotTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                /**返回*/
                welcome.setVisibility(View.VISIBLE);
                vipLogin.setVisibility(View.GONE);
                break;
            case R.id.login:
                /**登录*/
                break;
            case R.id.vip_card_close:
                /**清除卡号*/
                inputCard.setText("");
                break;
            case R.id.vip_password_close:
                /**清除密码*/
                inputPassword.setText("");
                break;
            case R.id.confir:
                /**确认*/
                if (AppSystemUntil.isWifi(this)){

                }else {
                    Toast.makeText(this,getString(R.string.toast_net),Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
