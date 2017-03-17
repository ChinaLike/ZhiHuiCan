package cn.sczhckj.order.overwrite;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.Config;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.until.AppSystemUntil;

/**
 * @ describe: 设置弹窗
 * @ author: Like on 2017-03-15.
 * @ email: 572919350@qq.com
 */

public class SettingPopupWindow extends PopupWindow {

    @Bind(R.id.setting_ip)
    EditText ip;
    @Bind(R.id.setting_port)
    EditText port;
    @Bind(R.id.setting)
    Button setting;

    private View view;

    private OnButtonListener onButtonListener;

    private Context mContext;

    public SettingPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pop_host_setting, null);
        int w = AppSystemUntil.width(context) / 2;
        int h = AppSystemUntil.height(context) / 2;
        ButterKnife.bind(this, view);
        setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setWidth(w);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
        settingDefault();
    }

    /**
     * 设置默认参数
     */
    private void settingDefault() {
        ip.setText("" + MyApplication.share.getData("ip", "192.168.0.25"));
        port.setText("" + MyApplication.share.getData("port", "8080"));
    }

    @OnClick(R.id.setting)
    public void onClick() {
        if (ip.getText().toString() != null || !ip.getText().toString().equals("")) {
            MyApplication.share.setData("ip", ip.getText().toString().trim());
        }
        if (port.getText().toString() != null || !port.getText().toString().equals("")) {
            MyApplication.share.setData("port", port.getText().toString().trim());
        }
        Config.setHOST();
        Config.setUrlHeartService();
        Config.setUrlNotificationService();
        if (onButtonListener != null) {
            onButtonListener.affirm();
        }
        dismiss();
    }

    /**
     * 显示位置
     */
    public void show() {
        showAtLocation(view, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.4F);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1F);
    }

    public void setOnButtonListener(OnButtonListener onButtonListener) {
        this.onButtonListener = onButtonListener;
    }

    public interface OnButtonListener {
        void affirm();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
