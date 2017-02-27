package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.produce.WaitressBean;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.ProduceMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 服务员登录
 */
public class WaitressLoginActivity extends Activity implements Callback<Bean<WaitressBean>>, TextWatcher {


    @Bind(R.id.login_phone_input)
    EditText loginPhoneInput;
    @Bind(R.id.login_cancel_phone)
    ImageView loginCancelPhone;
    @Bind(R.id.login_password_input)
    EditText loginPasswordInput;
    @Bind(R.id.login_cancel_password)
    ImageView loginCancelPassword;
    @Bind(R.id.login)
    Button login;

    private ProduceMode mProduceMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitress_login);
        ButterKnife.bind(this);
        init();

        /**测试*/
        // TODO: 2017-02-27 测试后删掉
        loginPhoneInput.setText("G0000");
        loginPasswordInput.setText("123456");
    }

    private void init() {
        mProduceMode = new ProduceMode();
        login.setClickable(false);
        login.setSelected(false);
        loginPhoneInput.addTextChangedListener(this);
    }

    @OnClick({R.id.login_cancel_phone, R.id.login_cancel_password, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_cancel_phone:
                loginPhoneInput.setText("");
                break;
            case R.id.login_cancel_password:
                loginPasswordInput.setText("");
                break;
            case R.id.login:
                verify();
                break;
        }
    }

    private void verify() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        bean.setWaitressId(loginPhoneInput.getText().toString());
        bean.setPassword(loginPasswordInput.getText().toString());
        mProduceMode.waitressLogin(bean, this);
    }

    @Override
    public void onResponse(Call<Bean<WaitressBean>> call, Response<Bean<WaitressBean>> response) {
        Bean<WaitressBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            Intent intent = new Intent(WaitressLoginActivity.this, TableActivity.class);
            startActivity(intent);
            finish();
        } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
            T.showShort(WaitressLoginActivity.this, bean.getMessage());
        } else {
            T.showShort(WaitressLoginActivity.this, "登录失败，请重新登陆");
        }
    }

    @Override
    public void onFailure(Call<Bean<WaitressBean>> call, Throwable t) {
        T.showShort(WaitressLoginActivity.this, "登录失败，请重新登陆");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals("")) {
            login.setClickable(false);
            login.setSelected(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        login.setClickable(true);
        login.setSelected(true);
    }
}
