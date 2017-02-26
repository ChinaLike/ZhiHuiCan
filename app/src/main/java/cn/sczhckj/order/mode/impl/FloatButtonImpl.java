package cn.sczhckj.order.mode.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.TableActivity;
import cn.sczhckj.order.activity.WaitressLoginActivity;

/**
 * @ describe: 浮动按钮的实现
 * @ author: Like on 2017-02-26.
 * @ email: 572919350@qq.com
 */

public class FloatButtonImpl {

    private Context mContext;

    private PopupWindow mPopupWindow;

    private View view;

    private TextView logout;

    private TextView switchTable;

    public FloatButtonImpl(Context context) {
        mContext = context;
        initPop();
    }

    private void initPop() {
        view = LayoutInflater.from(mContext).inflate(R.layout.pop_status_switch, null, false);
        logout = (TextView) view.findViewById(R.id.logout);
        logout();
        switchTable = (TextView) view.findViewById(R.id.switch_table);
        switchStatus();
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 显示
     */
    public void show() {
        if (!mPopupWindow.isShowing()) {
            setBackgroundAlpha(0.4F);
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 关闭
     */
    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 退出当前账号
     */
    private void logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, WaitressLoginActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                clear();
            }
        });
    }

    /**
     * 切换台桌
     */
    private void switchStatus() {
        switchTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, TableActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                clear();
            }
        });
    }

    /**
     * 清空记录
     */
    private void clear() {
        MyApplication.deviceID = "";
    }

}
