package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.VersionBean;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.manage.VersionManager;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:引导界面，提供用户VIP登录，用户可选择登录，也可不选择登录，不登录路直接跳转选菜界面，如果
 * 想要登录就跳转登录界面。
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */

public class LeadActivity extends Activity implements Callback<Bean<VersionBean>>, VersionManager.OnDialogClickListener {

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

    private VersionManager mVersionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);
        mVersionManager = new VersionManager(LeadActivity.this);
        buttonIsClick(false);
        getVersion();
    }

    @OnClick({R.id.right, R.id.deny})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                /**选择是，即先登录*/
                right.setSelected(true);
                right.setTextColor(ContextCompat.getColor(this, R.color.text_color_main_select));
                deny.setSelected(false);
                deny.setTextColor(ContextCompat.getColor(this, R.color.white));
                intent = new Intent(LeadActivity.this, LoginActivity.class);
                intent.putExtra(Constant.INTENT_FLAG, Constant.LEAD_TO_LOGIN);
                startActivity(intent);
                break;
            case R.id.deny:
                /**暂时未登录*/
                right.setSelected(false);
                right.setTextColor(ContextCompat.getColor(this, R.color.white));
                deny.setSelected(true);
                deny.setTextColor(ContextCompat.getColor(this, R.color.text_color_main_select));
                intent = new Intent(LeadActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取当前网络上的版本信息
     */
    private void getVersion() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        bean.setVersionCode(mVersionManager.getVersionCode(LeadActivity.this));
        bean.setVersionName(mVersionManager.getVersionName(LeadActivity.this));
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.VERSION)
                .time()
                .bean(bean);
        /**进行数据校验*/
        Call<Bean<VersionBean>> vipCallBack = RetrofitRequest.service().version(restRequest.toRequestString());
        vipCallBack.enqueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        right.setSelected(false);
        right.setTextColor(ContextCompat.getColor(this, R.color.white));
        deny.setSelected(false);
        deny.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void onResponse(Call<Bean<VersionBean>> call, Response<Bean<VersionBean>> response) {
        Bean<VersionBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            if (bean.getResult() != null) {
                if (bean.getResult().getVersionCode()>mVersionManager.getVersionCode(LeadActivity.this)) {
                    mVersionManager.version(LeadActivity.this, bean.getResult());
                    mVersionManager.setOnDialogClickListener(this);
                }else {
                    buttonIsClick(true);
                }
            } else {
                buttonIsClick(true);
            }
        }
    }

    @Override
    public void onFailure(Call<Bean<VersionBean>> call, Throwable t) {
        buttonIsClick(true);
    }


    /**
     * 设置按钮是否可以点击
     *
     * @param isClick
     */
    private void buttonIsClick(boolean isClick) {
        right.setClickable(isClick);
        deny.setClickable(isClick);
        if (isClick) {
            leadWelcome.setVisibility(View.VISIBLE);
            leadProgress.setVisibility(View.GONE);
            leadIsVip.setText("是否会员？");
        } else {
            leadWelcome.setVisibility(View.GONE);
            leadProgress.setVisibility(View.VISIBLE);
            leadIsVip.setText("数据初始化中...");
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {
        buttonIsClick(true);
    }
}
