package cn.sczhckj.order.overwrite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;

/**
 * @ describe: 通用Dialog ,包含一个按钮或两个的TextView、EditText、Progress弹窗
 * @ author: Like on 2017-02-28.
 * @ email: 572919350@qq.com
 */

public class CommonDialog extends Dialog {
    @Bind(R.id.title_img)
    ImageView titleImg;//标题图片
    @Bind(R.id.title)
    TextView title;//标题
    @Bind(R.id.text_context)
    TextView textContext;//文本内容
    @Bind(R.id.dialog_progress)
    ProgressBar dialogProgress;//进度条
    @Bind(R.id.progress_text)
    TextView progressText;//进度显示内容
    @Bind(R.id.progress_parent)
    LinearLayout progressParent;//进度父类
    @Bind(R.id.edit_hint)
    TextView editHint;//编辑框编辑文本
    @Bind(R.id.edit_context)
    EditText editContext;//编辑框
    @Bind(R.id.edit_clear)
    ImageView editClear;//清除输入内容
    @Bind(R.id.edit_hint_line)
    View editHintLine;//线条
    @Bind(R.id.edit_parent)
    LinearLayout editParent;//编辑框父类
    @Bind(R.id.left_btn)
    Button leftBtn;//左侧按钮
    @Bind(R.id.left_parent)
    RelativeLayout leftParent;//左侧父类
    @Bind(R.id.right_btn)
    Button rightBtn;//右侧按钮
    @Bind(R.id.right_parent)
    RelativeLayout rightParent;//右侧父类
    /**
     * 主布局
     */
    private View view;
    /**
     * 当前选择的类型
     */
    private int mode;
    /**
     * 状态监听
     */
    private OnDialogStatusListener onDialogStatusListener;

    public CommonDialog(Context context) {
        super(context, R.style.customDialog);
    }

    /**
     * @param context
     * @param mode    {@link Mode#TEXT}
     *                {@link Mode#EDIT}
     *                {@link Mode#PROGRESS}
     */
    public CommonDialog(Context context, int mode) {
        super(context, R.style.customDialog);
        init(context, mode);
    }

    private void init(Context context, int mode) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_common, null, false);
        ButterKnife.bind(this, view);
        setMode(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    /**
     * 设置类型
     *
     * @param mode
     */
    public CommonDialog setMode(int mode) {
        this.mode = mode;
        if (mode == Mode.TEXT) {
            textContext.setVisibility(View.VISIBLE);
            progressParent.setVisibility(View.GONE);
            editParent.setVisibility(View.GONE);
        } else if (mode == Mode.EDIT) {
            textContext.setVisibility(View.GONE);
            progressParent.setVisibility(View.GONE);
            editParent.setVisibility(View.VISIBLE);
        } else if (mode == Mode.PROGRESS) {
            textContext.setVisibility(View.GONE);
            progressParent.setVisibility(View.VISIBLE);
            editParent.setVisibility(View.GONE);
        }
        leftParent.setVisibility(View.GONE);
        rightParent.setVisibility(View.GONE);
        editHint.setVisibility(View.GONE);
        editClear.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        return this;
    }

    public CommonDialog setOnDialogStatusListener(OnDialogStatusListener onDialogStatusListener) {
        this.onDialogStatusListener = onDialogStatusListener;
        return this;
    }

    /**
     * 获取编辑框控件
     *
     * @return
     */
    public EditText getEditText() {
        return editContext;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public CommonDialog setTitle(String title) {
        if (title == null) {
            title = "";
        }
        this.title.setText(title);
        return this;
    }

    /**
     * 设置标题
     *
     * @param character
     */
    public CommonDialog setTitle(Character character) {
        if (character != null) {
            this.title.setText(character);
        }
        return this;
    }

    /**
     * 设置标题图片
     *
     * @param resId 资源文件
     */
    public CommonDialog setTitleImg(int resId) {
        titleImg.setImageResource(resId);
        return this;
    }

    /**
     * 设置标题图片
     *
     * @param drawable
     */
    public CommonDialog setTitleImg(Drawable drawable) {
        if (drawable != null) {
            titleImg.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 设置标题图片
     *
     * @param bitmap
     */
    public CommonDialog setTitleImg(Bitmap bitmap) {
        if (bitmap != null) {
            titleImg.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * 积极地按钮
     *
     * @param title    标题
     * @param listener 监听
     */
    public CommonDialog setPositive(String title, View.OnClickListener listener) {
        rightParent.setVisibility(View.VISIBLE);
        if (title != null) {
            rightBtn.setText(title);
        }
        rightBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 消极的按钮
     *
     * @param title    标题
     * @param listener 监听
     */
    public CommonDialog setNegative(String title, View.OnClickListener listener) {
        leftParent.setVisibility(View.VISIBLE);
        if (title != null) {
            leftBtn.setText(title);
        }
        leftBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置文本显示
     *
     * @param context 文本内容
     */
    public CommonDialog setTextContext(String context) {
        if (mode != Mode.TEXT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            textContext.setText(context);
        }
        return this;
    }

    /**
     * 设置文本显示
     *
     * @param context   文本内容
     * @param textColor 文本颜色
     */
    public CommonDialog setTextContext(String context, int textColor) {
        if (mode != Mode.TEXT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            textContext.setTextColor(textColor);
            textContext.setText(context);
        }
        return this;
    }

    /**
     * 设置输入框上方的提醒文字
     *
     * @param str
     */
    public CommonDialog setEditTextHint(String str) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            if (str != null) {
                editHint.setVisibility(View.VISIBLE);
                editHint.setText(str);
            }
        }
        return this;
    }

    /**
     * 设置Edit输入类型
     *
     * @param type {@link android.text.InputType}
     * @return
     */
    public CommonDialog setEditInputType(int type) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            editContext.setInputType(type);
        }
        return this;
    }

    /**
     * 设置输入框上方的提醒文字
     *
     * @param str
     * @param color 颜色
     */
    public CommonDialog setEditTextHint(String str, int color) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            if (str != null) {
                editHint.setVisibility(View.VISIBLE);
                editHint.setText(str);
                editHint.setTextColor(color);
            }
        }
        return this;
    }

    /**
     * 设置输入框内提示文字
     *
     * @param hintText
     */
    public CommonDialog setEditHint(String hintText) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            if (hintText != null) {
                editContext.setHint(hintText);
            }
        }
        return this;
    }

    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getInputText() {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            return editContext.getText().toString();
        }
    }

    /**
     * 设置进度框下面的文字提醒
     *
     * @param text
     */
    public CommonDialog setProgressText(String text) {
        if (mode != Mode.PROGRESS) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            if (progressText.getVisibility() == View.GONE) {
                progressText.setVisibility(View.VISIBLE);
            }
            progressText.setText(text);
        }
        return this;
    }

    /**
     * 设置进度条的最大值
     *
     * @param max
     */
    public CommonDialog setProgressMax(int max) {
        if (mode != Mode.PROGRESS) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            dialogProgress.setMax(max);
        }
        return this;
    }

    /**
     * 设置进度条的值
     *
     * @param progress
     */
    public CommonDialog setProgress(int progress) {
        if (mode != Mode.PROGRESS) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            dialogProgress.setProgress(progress);
        }
        return this;
    }


    /**
     * 清除Edit的内容
     */
    public void clearEdit() {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            editContext.setText("");
        }
    }

    /**
     * 设置清空Edit的按钮
     *
     * @param resId
     */
    public CommonDialog setClearButton(Integer resId) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            editClear.setVisibility(View.VISIBLE);
            if (resId != null) {
                editClear.setImageResource(resId);
            }
            editClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editContext.setText("");
                }
            });
        }
        return this;
    }

    /**
     * 设置底部线条颜色
     *
     * @param colorId
     */
    public CommonDialog setEditLineColor(int colorId) {
        if (mode != Mode.EDIT) {
            throw new NullPointerException("对应的Mode不对应");
        } else {
            editHintLine.setBackgroundColor(colorId);
        }
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (onDialogStatusListener != null) {
            onDialogStatusListener.show();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDialogStatusListener != null) {
            onDialogStatusListener.dismiss();
        }
    }

    /**
     * 没有回调的关闭
     */
    public void onDismiss() {
        super.dismiss();
    }

    /**
     * 没有回调的打开
     */
    public void onShow() {
        super.show();
    }

    public interface Mode {
        int TEXT = 0;//纯文本
        int EDIT = 1;//编辑框
        int PROGRESS = 2;//进度框
    }

    /**
     * Dialog的状态
     */
    public interface OnDialogStatusListener {
        void show();

        void dismiss();
    }

}
