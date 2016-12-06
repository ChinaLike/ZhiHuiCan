package cn.sczhckj.order.overwrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;

/**
 * @describe: 输入框弹窗
 * @author: Like on 2016/12/2.
 * @Email: 572919350@qq.com
 */

public class MyEditTextDialog extends Dialog {

    @Bind(R.id.dialog_title)
    TextView dialogTitle;
    @Bind(R.id.dialog_context)
    EditText dialogContext;
    @Bind(R.id.dialog_left)
    Button dialogLeft;
    @Bind(R.id.dialog_right)
    Button dialogRight;
    private Context mContext;

    private View view;

    public MyEditTextDialog(Context context) {
        this(context, R.style.customDialog);
    }

    public MyEditTextDialog(Context context, int themeResId) {
        super(context, R.style.customDialog);
        this.mContext = context;
        initView();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_text_password, null, false);
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
    public void setLeftButton(String title, View.OnClickListener onClickListener) {
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
    public void setRightButton(String title, View.OnClickListener onClickListener) {
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
     * 获取输入内容
     */
    public String getEditText() {
        return dialogContext.getText().toString();
    }

    /**
     * 设置提示语
     * @param hintText
     */
    public void setEditTextHint(String hintText){
        dialogContext.setHint(hintText);
    }

    @OnClick(R.id.dialog_cancel)
    public void onClick() {
        dialogContext.setText("");
    }
}
