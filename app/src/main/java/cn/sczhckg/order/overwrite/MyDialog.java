package cn.sczhckg.order.overwrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;

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

}
