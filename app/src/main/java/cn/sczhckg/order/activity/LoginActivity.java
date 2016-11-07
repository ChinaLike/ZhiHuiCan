package cn.sczhckg.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.MyApplication;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.UserLoginBean;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:首页欢迎界面和VIP登录
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class LoginActivity extends Activity implements Callback<String> {

    @Bind(R.id.inputCard)
    EditText inputCard;
    @Bind(R.id.inputPassword)
    EditText inputPassword;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.login)
    ImageView login;
    @Bind(R.id.vip_card_close)
    ImageView vipCardClose;
    @Bind(R.id.vip_password_close)
    ImageView vipPasswordClose;
    @Bind(R.id.confir)
    Button confir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.login, R.id.vip_card_close, R.id.vip_password_close, R.id.confir})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                break;
            case R.id.vip_card_close:
                inputCard.setText("");
                break;
            case R.id.vip_password_close:
                inputPassword.setText("");
                break;
            case R.id.confir:
                login(inputCard.getText().toString(), inputPassword.getText().toString());
                break;
        }
    }

    /**
     * 确认按钮
     */
    private void login(String userName, String password) {
        if (userName.equals("") || userName == null) {
            Toast.makeText(this, getString(R.string.userNameHint), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("") || password == null) {
            Toast.makeText(this, getString(R.string.passwordHint), Toast.LENGTH_SHORT).show();
            return;
        }
        /**进行数据校验*/
        Map<String, String> vipMap = new HashMap<>();
        vipMap.put(Constant.USERNAME, userName);
        vipMap.put(Constant.PASSWORD, password);
        Call<String> vipCallBack = RetrofitRequest.GET(Config.HOST).vip(vipMap);
        vipCallBack.enqueue(this);

    }

    /**
     * 进入主界面
     */
    private void into() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
//        final UserLoginBean bean = response.body();
//        if (bean.getCode() == 0) {
//            Toast.makeText(this, bean.getResult().getMsg(), Toast.LENGTH_SHORT).show();
//            MyApplication.isLogin = true;
//            into();
//        } else {
//            Toast.makeText(this,  bean.getResult().getMsg(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }
}

