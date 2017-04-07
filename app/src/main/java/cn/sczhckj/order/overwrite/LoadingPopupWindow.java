package cn.sczhckj.order.overwrite;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import pl.droidsonroids.gif.GifImageView;

/**
 * @ describe: 加载提示框弹窗
 * @ author: Like on 2017-04-07.
 * @ email: 572919350@qq.com
 */

public class LoadingPopupWindow extends PopupWindow {

    @Bind(R.id.loading_gif)
    GifImageView loadingGif;
    @Bind(R.id.loading_text)
    TextView loadingText;
    @Bind(R.id.loading_parent)
    LinearLayout loadingParent;
    private Context mContext;
    private LayoutInflater inflater;
    private View view;
    /**
     * 使用模式，消费者还是服务员
     */
    private int mode = Constant.PRODUCER;
    /**
     * 是否模态，即空白阴影
     */
    private boolean isModal = true;

    public LoadingPopupWindow(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        view = inflater.inflate(R.layout.pop_loading, null, false);
        ButterKnife.bind(this, view);
        setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mode == Constant.PRODUCER) {
            setBackgroundDrawable(new ColorDrawable(0));
            setOutsideTouchable(true);
        }
        defaultSetting();
    }

    /**
     * 默认设置
     */
    private void defaultSetting() {
        loadingGif.setImageResource(R.drawable.loading);
        loadingText.setText("数据正在加载中，请稍等...");
    }

    /**
     * 显示
     */
    public void show() {
        showAtLocation(view, Gravity.CENTER, 0, 0);
        if (isModal) {
            backgroundAlpha(0.6F);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (isModal) {
            backgroundAlpha(1F);
        }
    }

    /**
     * 设置加载时的文本
     *
     * @param text
     */
    public void setLoadingText(CharSequence text) {
        if (text != null) {
            loadingText.setText(text);
        }
    }

    /**
     * 设置加载图片
     *
     * @param resId
     */
    public void setLoadingGif(int resId) {
        loadingGif.setImageResource(resId);
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

    /**
     * 设置点击弹窗
     */
    public void setOnClickListener(View.OnClickListener listener) {
        loadingParent.setOnClickListener(listener);
    }

    /**
     * 设置是否可以点击
     * @return
     */
    public void setClickable(boolean clickable){
        loadingParent.setClickable(clickable);
    }

    /**
     * 设置消费模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 设置模态
     *
     * @param modal
     */
    public void setModal(boolean modal) {
        isModal = modal;
    }
}
