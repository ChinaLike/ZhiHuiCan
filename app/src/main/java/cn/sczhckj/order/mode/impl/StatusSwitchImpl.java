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
import cn.sczhckj.order.overwrite.MyDialog;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ describe: 台桌状态切换
 * @ author: Like on 2017-02-23.
 * @ email: 572919350@qq.com
 */

public class StatusSwitchImpl implements Callback<Bean<ResponseCommonBean>> {

    int MEGER_TO_ALONE = 0;//并桌转单桌
    int ALONE_TO_MEGER = 1;//单桌转并桌

    private Context mContext;

    private MyDialog dialog;

    private CheckSwitchButton buttonView;

    public StatusSwitchImpl(Context context){
        this.mContext = context;
        dialog = new MyDialog(context);
    }

    public void switchStatus(final CheckSwitchButton button) {
        buttonView = button;
        button.setOnCheckedChangeListenner(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialog();
            }
        });
    }

    /**
     * 弹窗
     */
    private void dialog() {
        dialog.setTitle(mContext.getString(R.string.dialog_title));
        dialog.setContextText(mContext.getString(R.string.dialog_context_switch));
        dialog.setPositiveButton(mContext.getString(R.string.dialog_context_switch_negative), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                buttonView.setChecked(false);
            }
        });
        dialog.setNegativeButton(mContext.getString(R.string.dialog_context_switch_positive), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                T.showShort(mContext,mContext.getString(R.string.toast_content_hint));
                switchStatus(MyApplication.tableBean.getRecordId());
            }
        });
        dialog.show();
    }

    /**
     * 数据请求
     * @param recordId
     */
    public void switchStatus(Integer recordId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setRecordId(recordId);
        bean.setStatus(MEGER_TO_ALONE);
        TableMode tableMode = new TableMode();
        tableMode.switchStatus(bean, this);
    }

    @Override
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
        Bean<ResponseCommonBean> bean =response.body();
        if (bean!=null && bean.getCode() == ResponseCode.SUCCESS){
            buttonView.setChecked(true);
            //将合并点菜标志设置为否，同时刷新菜品列表
            EventBus.getDefault().post( new WebSocketEvent(WebSocketEvent.ALONE_ORDER));
        }else {
            T.showShort(mContext,bean.getMessage());
            buttonView.setChecked(false);
        }
    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
        buttonView.setChecked(false);
    }
}
