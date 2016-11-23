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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.MyApplication;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.Bean;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.OP;
import cn.sczhckg.order.data.bean.UserLoginBean;
import cn.sczhckg.order.data.event.LoginEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.RestResponse;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:用户登录界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class LoginActivity extends Activity implements Callback<Bean<UserLoginBean>> {

    @Bind(R.id.inputCard)
    EditText inputCard;
    @Bind(R.id.inputPassword)
    EditText inputPassword;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.login_set)
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

    @OnClick({R.id.back, R.id.login_set, R.id.vip_card_close, R.id.vip_password_close, R.id.confir})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login_set:
                break;
            case R.id.vip_card_close:
                /**清空卡号*/
                inputCard.setText("");
                break;
            case R.id.vip_password_close:
                /**清空密码*/
                inputPassword.setText("");
                break;
            case R.id.confir:
                /**验证*/
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

        UserLoginBean bean=new UserLoginBean();
        bean.setName(userName);
        bean.setPassword(password);
        RestRequest<UserLoginBean> restRequest= JSONRestRequest.Builder.build(UserLoginBean.class)
                .op(OP.LOGIN)
                .time()
                .bean(bean);
        /**进行数据校验*/
        Call<Bean<UserLoginBean>> vipCallBack = RetrofitRequest.service().login(restRequest.toRequestString());
        vipCallBack.enqueue(this);

    }

    /**
     * 根据标识类型判断登录方式，如果是后期进行登录应关闭
     * @param bean
     */
    private void into(UserLoginBean bean) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Constant.USER_INFO, bean);
        if (getIntent().getFlags() == Constant.LEAD_TO_LOGIN) {
            startActivity(intent);
        } else if (getIntent().getFlags() == Constant.MAIN_TO_LOGIN) {
            setResult(Constant.LOGIN_RESULT_CODE,intent);
        }
        /**发布消息*/
        EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_SUCCESS,bean));

        finish();
    }

    @Override
    public void onResponse(Call<Bean<UserLoginBean>> call, Response<Bean<UserLoginBean>> response) {
//        RestResponse<UserLoginBean> resp = JSONRestResponse.Parser.parse(body, UserLoginBean.class);
//        UserLoginBean user = resp.getBean();
        Log.d("登录回调：",response.body().toString());
        Bean<UserLoginBean> bean=response.body();
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

