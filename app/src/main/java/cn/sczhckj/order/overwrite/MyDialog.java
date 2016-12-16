package cn.sczhckj.order.overwrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;

/**
 * @describe: 普通视图弹窗
 * @author: Like on 2016/12/2.
 * @Email: 572919350@qq.com
 */

public class MyDialog extends Dialog {

    @Bind(R.id.dialog_context)
    TextView dialogContext;
    @Bind(R.id.dialog_left)
    Button dialogLeft;
    @Bind(R.id.dialog_right)
    Button dialogRight;
    @Bind(R.id.dialog_title)
    TextView dialogTitle;
    @Bind(R.id.left_parent)
    RelativeLayout leftParent;
    @Bind(R.id.right_parent)
    RelativeLayout rightParent;
    @Bind(R.id.dialog_progress)
    ProgressBar dialogProgress;
    @Bind(R.id.progress_text)
    TextView progressText;
    @Bind(R.id.progress_parent)
    LinearLayout progressParent;

    private Context mContext;

    private View view;

    public MyDialog(Context context) {
        this(context, R.style.customDialog);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, R.style.customDialog);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_more_pot, null, false);
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    /**
     * 设置确定事件响应
     *
     * @param title
     * @param onClickListener
     */
    public void setPositiveButton(String title, View.OnClickListener onClickListener) {
        if (title != null) {
            dialogLeft.setText(title);
        }
        dialogLeft.setOnClickListener(onClickListener);
    }

    /**
     * 设置取消响应
     *
     * @param title
     * @param onClickListener
     */
    public void setNegativeButton(String title, View.OnClickListener onClickListener) {
        if (title != null) {
            dialogRight.setText(title);
        }
        dialogRight.setOnClickListener(onClickListener);
    }

    /**
     * 设置只有一个按键的Buton
     *
     * @param title
     * @param onClickListener
     */
    public void setAloneButton(String title, View.OnClickListener onClickListener) {
        leftParent.setVisibility(View.GONE);
        if (title != null) {
            dialogRight.setText(title);
        }
        dialogRight.setOnClickListener(onClickListener);
    }

    @Override
    public void setTitle(CharSequence title) {
        dialogTitle.setText(title);
    }

    /**
     * 设置内容
     *
     * @param context
     */
    public void setContextText(String context) {
        if (context != null) {
            dialogContext.setText(context);
        }
    }

    /**
     * 设置进度条的最大进度
     *
     * @param max
     */
    public void setProgressMax(int max) {

        dialogProgress.setMax(max);
    }

    /**
     * 设置进度条的进度
     *
     * @param progress
     */
    public void setProgress(int progress) {

        dialogProgress.setProgress(progress);
    }

    /**
     * 设置进度条进度文字描述
     *
     * @param text
     */
    public void setProgressText(String text) {

        progressText.setText(text);
    }

    /**
     * 设置进度条显示
     */
    public void setProgressVisibility() {
        if (progressParent.getVisibility() == View.GONE) {
            progressParent.setVisibility(View.VISIBLE);
        }
    }

}
