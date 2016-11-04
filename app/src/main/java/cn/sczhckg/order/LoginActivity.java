package cn.sczhckg.order;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:首页欢迎界面和VIP登录
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class LoginActivity extends Activity implements Callback<CommonBean>{


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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.right, R.id.deny, R.id.back, R.id.login, R.id.vip_card_close, R.id.vip_password_close, R.id.confir})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right:
                /**会员进入*/
                welcome.setVisibility(View.GONE);
                vipLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.deny:
                /**非会员进入*/
                into();
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
                if (AppSystemUntil.isWifi(this)) {
                    login(inputCard.getText().toString(), inputPassword.getText().toString());
                } else {
                    Toast.makeText(this, getString(R.string.toast_net), Toast.LENGTH_SHORT).show();
                }
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
        Map<String,String> vipMap=new HashMap<>();
        vipMap.put(Constant.USERNAME,userName);
        vipMap.put(Constant.PASSWORD,password);
        Call<CommonBean> vipCallBack= RetrofitRequest.GET(Config.HOST).vip(vipMap);
        vipCallBack.enqueue(this);

    }

    /**
     * 进入主界面
     */
    private void into(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
        final CommonBean bean= response.body();
        if (bean.getCode()==0){
            Toast.makeText(this, bean.getMsg(), Toast.LENGTH_SHORT).show();
            MyApplication.isLogin=true;
            into();
        }else {
            Toast.makeText(this, bean.getMsg(), Toast.LENGTH_SHORT).show();
            confir.setText(bean.getMsg());
        }
    }

    @Override
    public void onFailure(Call<CommonBean> call, Throwable t) {
        Toast.makeText(this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }
}

