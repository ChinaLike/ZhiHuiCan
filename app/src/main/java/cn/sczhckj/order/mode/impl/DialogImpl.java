package cn.sczhckj.order.mode.impl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import cn.sczhckj.order.overwrite.MyDialog;
import cn.sczhckj.order.overwrite.MyEditTextDialog;

/**
 * @describe:
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class DialogImpl {

    private MyDialog dialog;

    private MyEditTextDialog myEditTextDialog;

    public DialogImpl(Context mContext) {
        dialog = new MyDialog(mContext);
        myEditTextDialog = new MyEditTextDialog(mContext);
    }

    /**
     * 一个按钮，不处理逻辑，只关闭
     *
     * @param title
     * @param context
     * @param cacel
     */
    public MyDialog aloneDialog(String title, String context, String cacel) {
        dialog.setTitle(title);
        dialog.setContextText(context);
        dialog.setAloneButton(cacel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 设置取消按钮
     *
     * @param title
     * @param context
     * @param cacel
     * @return
     */
    public MyDialog cancelDialog(String title, String context, String cacel) {
        dialog.setTitle(title);
        dialog.setContextText(context);
        dialog.setNegativeButton(cacel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 自定义输入框
     * @param title
     * @param context
     * @param hint
     * @return
     */
    public MyEditTextDialog setEditDialog(String title, String context, String hint) {
        myEditTextDialog.setTitle(title);
        myEditTextDialog.setEditTextHint(hint);
        if (context != null && !"".equals(context)) {
            myEditTextDialog.setContext(context);
        }
        return myEditTextDialog;
    }


    public MyDialog getDialog() {
        return dialog;
    }

    public MyEditTextDialog editTextDialog() {
        return myEditTextDialog;
    }
}
