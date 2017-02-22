package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.event.LoginEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.UserMode;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:用户登录界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
    public class LoginActivity extends Activity implements Callback<Bean<MemberBean>> {

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
    /**
     * 登录类型
     */
    private Integer loginType;
    /**
     * 计时器
     */
    private CountDownTimer mCountDownTimer;
    /**
     * 用户数据请求
     */
    private UserMode mUserMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUserMode = new UserMode();
        /**设置默认选中账号登录*/
        loginType(PASSWORD_LOGIN);
        loginBtnStatus(false);
        initTime();
    }

    /**
     * 初始化登录
     *
     * @param number 卡号或手机号
     * @param code   密码或验证码
     */
    private void initLogin(String number, String code) {
        if (number.equals("") || number == null) {
            T.showShort(this, getString(R.string.login_activity_user_name_hint));
            return;
        }
        if (code.equals("") || code == null) {
            T.showShort(this, getString(R.string.login_activity_password_hint));
            return;
        }
        loginBtnStatus(true);
        RequestCommonBean bean = new RequestCommonBean();
        if (loginType == PASSWORD_LOGIN) {
            bean.setMemberCode(number);
            bean.setPassword(code);
            mUserMode.login(bean, this);
        } else if (loginType == SHORTCUT_LOGIN) {
            bean.setPhone(number);
            bean.setIdentity(code);
            mUserMode.smsLogin(bean, this);
        }
    }

    /**
     * 初始化获取验证码
     *
     * @param phone 手机号
     */
    private void initSms(String phone) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setPhone(phone);
        mUserMode.sms(bean, smsCallback);
    }

    /**
     * 获取验证码回调
     */
    Callback<Bean<ResponseCommonBean>> smsCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                T.showShort(LoginActivity.this, bean.getMessage());
                mCountDownTimer.start();
            } else {
                T.showShort(LoginActivity.this, bean.getMessage());
                loginCode.setClickable(true);
                loginCode.setText(getString(R.string.login_activity_again_send));
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
            T.showShort(LoginActivity.this, getString(R.string.login_activity_over_time));
            loginCode.setClickable(true);
            loginCode.setText(getString(R.string.login_activity_again_send));
        }
    };

    /**
     * 初始化倒计时时间
     */
    private void initTime() {
        mCountDownTimer = new CountDownTimer(TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                loginCode.setText(getString(R.string.login_activity_again_send_later,millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                loginCode.setText(getString(R.string.login_activity_again_send));
                loginCode.setClickable(true);
            }
        };
    }

    @OnClick({R.id.back, R.id.login_set, R.id.login_numberLogin, R.id.login_shortcutLogin,
            R.id.login_code, R.id.login_cancel_phone, R.id.login_cancel_password, R.id.login})
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
                    loginCode.setClickable(false);
                    initSms(loginPhoneInput.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_activity_input_phone), Toast.LENGTH_SHORT).show();
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

                initLogin(loginPhoneInput.getText().toString(), loginPasswordInput.getText().toString());
                break;
        }
    }

    /**
     * 根据登录方式显示界面
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
            loginPhoneInput.setHint(getString(R.string.login_activity_input_phone_hint));
            loginPhoneInput.setText("");
            loginPasswordInput.setText("");
            loginPasswordInput.setHint(getString(R.string.login_activity_input_password_hint));
            loginPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (type == SHORTCUT_LOGIN) {
            /**快捷登录*/
            loginType = PASSWORD_LOGIN;
            select(loginNumberLogin, false);
            select(loginShortcutLogin, true);
            loginView1.setVisibility(View.VISIBLE);
            loginCode.setVisibility(View.VISIBLE);
            loginCancelPhone.setVisibility(View.GONE);
            loginPhoneInput.setHint(getString(R.string.login_activity_phone_hint));
            loginPhoneInput.setText("");
            loginPasswordInput.setText("");
            loginPasswordInput.setHint(getString(R.string.login_activity_code_hint));
            loginPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT);
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
     * 登录按钮状态
     *
     * @param isSelect
     */
    private void loginBtnStatus(boolean isSelect) {
        if (isSelect) {
            /**按下后，没有数据返回不可点击*/
            login.setClickable(false);
            login.setSelected(false);
            login.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            /**数据返回，可点击*/
            login.setClickable(true);
            login.setSelected(true);
            login.setTextColor(ContextCompat.getColor(this, R.color.button_text));
        }
    }

    /**
     * 根据标识类型判断登录方式，如果是后期进行登录应关闭
     *
     * @param bean
     */
    private void into(MemberBean bean) {
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
    public void onResponse(Call<Bean<MemberBean>> call, Response<Bean<MemberBean>> response) {
        Bean<MemberBean> bean = response.body();
        T.showShort(this, bean.getMessage());
        loginBtnStatus(false);
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
//            MyApplication.setIsLogin(true);
//            MyApplication.setMemberCode(bean.getResult().getMemberCode());
            MyApplication.tableBean.setUser(bean.getResult());
            into(bean.getResult());
        }
    }

    @Override
    public void onFailure(Call<Bean<MemberBean>> call, Throwable t) {
        loginBtnStatus(false);
        T.showShort(this, getString(R.string.login_activity_over_time));
    }
}

