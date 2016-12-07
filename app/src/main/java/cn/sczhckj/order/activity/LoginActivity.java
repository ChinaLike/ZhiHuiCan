package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.UserLoginBean;
import cn.sczhckj.order.data.event.LoginEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:用户登录界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class LoginActivity extends Activity implements Callback<Bean<UserLoginBean>> {

    /**
     * 账号登录
     */
    private static final int PASSWORD_LOGIN = 1;
    /**
     * 快捷登录
     */
    private static final int SHORTCUT_LOGIN = 2;
    /**
     * 短信倒计时时间
     */
    private static final long TIME = 60000;


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.login_numberLogin)
    Button loginNumberLogin;
    @Bind(R.id.login_shortcutLogin)
    Button loginShortcutLogin;
    @Bind(R.id.login_phone_input)
    EditText loginPhoneInput;
    @Bind(R.id.login_view1)
    View loginView1;
    @Bind(R.id.login_code)
    Button loginCode;
    @Bind(R.id.login_cancel_phone)
    ImageView loginCancelPhone;
    @Bind(R.id.login_password_input)
    EditText loginPasswordInput;
    @Bind(R.id.login)
    Button login;

    private Integer loginType;


    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        /**设置默认选中账号登录*/
        loginType(PASSWORD_LOGIN);
        initTime();
    }

    /**
     * 初始化倒计时时间
     */
    private void initTime() {
        mCountDownTimer = new CountDownTimer(TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                loginCode.setText((millisUntilFinished / 1000) + "s后重新发送");
            }

            @Override
            public void onFinish() {
                loginCode.setText("重新发送");
                loginCode.setClickable(true);
            }
        };
    }

    @OnClick({R.id.back, R.id.login_set, R.id.login_numberLogin, R.id.login_shortcutLogin, R.id.login_code, R.id.login_cancel_phone, R.id.login_cancel_password, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                /**返回上一级*/
                finish();
                break;
            case R.id.login_set:
                /**用户设置*/
                break;
            case R.id.login_numberLogin:
                /**账号登录*/
                loginType(PASSWORD_LOGIN);
                break;
            case R.id.login_shortcutLogin:
                /**密码登录*/
                loginType(SHORTCUT_LOGIN);
                break;
            case R.id.login_code:
                /**获取验证码*/
                if (!"".equals(loginPhoneInput.getText().toString())) {
                    mCountDownTimer.start();
                    loginCode.setClickable(false);
                    getSMSAuthCode(loginPhoneInput.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "请先输入电话号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_cancel_phone:
                /**清除登录号码*/
                loginPhoneInput.setText("");
                break;
            case R.id.login_cancel_password:
                /**清除密码*/
                loginPasswordInput.setText("");
                break;
            case R.id.login:
                login(loginPhoneInput.getText().toString(), loginPasswordInput.getText().toString());
                break;
        }
    }

    /**
     * 登录方式
     *
     * @param type
     */
    private void loginType(int type) {
        if (type == PASSWORD_LOGIN) {
            /**密码登录*/
            loginType = PASSWORD_LOGIN;
            select(loginNumberLogin, true);
            select(loginShortcutLogin, false);
            loginView1.setVisibility(View.GONE);
            loginCode.setVisibility(View.GONE);
            loginCancelPhone.setVisibility(View.VISIBLE);
        } else if (type == SHORTCUT_LOGIN) {
            /**快捷登录*/
            loginType = PASSWORD_LOGIN;
            select(loginNumberLogin, false);
            select(loginShortcutLogin, true);
            loginView1.setVisibility(View.VISIBLE);
            loginCode.setVisibility(View.VISIBLE);
            loginCancelPhone.setVisibility(View.GONE);
        }
    }

    /**
     * 按钮设置
     *
     * @param mButton
     */
    private void select(Button mButton, boolean isSelect) {
        mButton.setSelected(isSelect);
        if (isSelect) {
            mButton.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.button_text));
        } else {
            mButton.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
        }
    }

    /**
     * 获取短信验证码
     */
    private void getSMSAuthCode(String phone) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setPhone(phone);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.LOGIN_AUTH_CODE)
                .time()
                .bean(bean);
        /**进行数据校验*/
        Call<Bean<Integer>> vipCallBack = RetrofitRequest.service().smsAuthCode(restRequest.toRequestString());
        vipCallBack.enqueue(new Callback<Bean<Integer>>() {
            @Override
            public void onResponse(Call<Bean<Integer>> call, Response<Bean<Integer>> response) {
                if (response.body() != null) {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bean<Integer>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
            }
        });
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

        RequestCommonBean bean = new RequestCommonBean();
        bean.setPhone(userName);
        bean.setPassword(password);
        bean.setLoginType(loginType);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.LOGIN)
                .time()
                .bean(bean);
        /**进行数据校验*/
        Call<Bean<UserLoginBean>> vipCallBack = RetrofitRequest.service().login(restRequest.toRequestString());
        vipCallBack.enqueue(this);

    }

    /**
     * 根据标识类型判断登录方式，如果是后期进行登录应关闭
     *
     * @param bean
     */
    private void into(UserLoginBean bean) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Constant.USER_INFO, bean);
        if (getIntent().getExtras() != null && (getIntent().getExtras().getInt(Constant.INTENT_FLAG) == Constant.LEAD_TO_LOGIN)) {
            startActivity(intent);
        } else if (getIntent().getExtras() != null && (getIntent().getExtras().getInt(Constant.INTENT_FLAG) == Constant.MAIN_TO_LOGIN)) {
            setResult(Constant.LOGIN_RESULT_CODE, intent);
        }
        /**发布消息*/
        EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_SUCCESS, bean));
        finish();
    }

    @Override
    public void onResponse(Call<Bean<UserLoginBean>> call, Response<Bean<UserLoginBean>> response) {
        Bean<UserLoginBean> bean = response.body();
        if (bean.getCode() == 0) {
            Toast.makeText(this, bean.getMessage(), Toast.LENGTH_SHORT).show();
            MyApplication.isLogin = true;
            into(bean.getResult());
        } else {
            Toast.makeText(this, bean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Bean<UserLoginBean>> call, Throwable t) {
        Toast.makeText(this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }


}

