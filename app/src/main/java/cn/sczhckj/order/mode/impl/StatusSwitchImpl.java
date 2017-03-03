package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import org.greenrobot.eventbus.EventBus;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.overwrite.CheckSwitchButton;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ describe: 台桌状态切换
 * @ author: Like on 2017-02-23.
 * @ email: 572919350@qq.com
 */

public class StatusSwitchImpl implements Callback<Bean<ResponseCommonBean>>, CompoundButton.OnCheckedChangeListener {

    int MEGER_TO_ALONE = 0;//并桌转单桌
    int ALONE_TO_MEGER = 1;//单桌转并桌

    private Context mContext;

    private CheckSwitchButton buttonView;

    private CommonDialog mDialog;


    public StatusSwitchImpl(Context context) {
        this.mContext = context;
        mDialog = new CommonDialog(context, CommonDialog.Mode.TEXT);
    }

    public void setButtonView(CheckSwitchButton buttonView) {
        this.buttonView = buttonView;
        buttonView.setOnCheckedChangeListenner(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            dialog();
        }
    }

    private void dialog() {
        mDialog.setTitle(mContext.getString(R.string.dialog_title))
                .setTextContext(mContext.getString(R.string.dialog_context_switch))
                .setPositive(mContext.getString(R.string.dialog_context_switch_positive), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.setTextContext(mContext.getString(R.string.toast_content_hint));
                        switchStatus();
                    }
                })
                .setNegative(mContext.getString(R.string.dialog_context_switch_negative), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        buttonView.setChecked(false);
                    }
                })
                .show();
    }

    /**
     * 数据请求
     */
    public void switchStatus() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setRecordId(MyApplication.tableBean.getRecordId());
        bean.setStatus(MEGER_TO_ALONE);
        TableMode tableMode = new TableMode();
        tableMode.switchStatus(bean, this);
    }

    @Override
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
        Bean<ResponseCommonBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            //将合并点菜标志设置为否，同时刷新菜品列表
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.ALONE_ORDER));
            mDialog.dismiss();
        } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
            mDialog.setTextContext(bean.getMessage());
            buttonView.setChecked(false);
        } else {
            T.showShort(mContext, bean.getMessage());
            buttonView.setChecked(false);
        }
    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        buttonView.setChecked(false);
    }
}
