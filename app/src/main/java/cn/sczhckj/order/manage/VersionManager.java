package cn.sczhckj.order.manage;

import android.content.Context;
import android.view.View;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.InitActivity;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.until.AndroidUtils;

/**
 * @describe: 版本管理
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionManager {

    private DownLoadManager downLoadManager;

    private Context mContext;

    private CommonDialog.OnDialogStatusListener onDialogStatusListener;

    private CommonDialog dialog;

    public VersionManager(Context mContext) {
        this.mContext = mContext;
        downLoadManager = new DownLoadManager(mContext);
    }

    /**
     * 监听
     *
     * @param onDialogStatusListener
     */
    public void setOnDialogStatusListener(CommonDialog.OnDialogStatusListener onDialogStatusListener) {
        this.onDialogStatusListener = onDialogStatusListener;
    }

    /**
     * APK更新
     *
     * @param bean
     */
    public void updata(VersionBean bean) {
        if (bean.getCode() > AndroidUtils.getVersionCode(mContext)) {
            /**当前版本高于本地版本，执行更新*/
            if (InitActivity.isAuto) {
                /**后台自动更新*/
                downLoadManager.downloadFile(Config.HOST, bean.getUrl(), judgeName(bean.getName()));
            } else {
                /**初始化用户操作更新*/
                dialog(bean);
            }
        } else {
            onDialogStatusListener.dismiss();
        }
    }

    /**
     * 弹窗显示更新信息
     *
     * @param bean
     */
    private void dialog(final VersionBean bean) {
        dialog = new CommonDialog(mContext, CommonDialog.Mode.TEXT);
        String context = mContext.getString(R.string.dialog_apk_update_content, AndroidUtils.getVersionName(mContext), bean.getVersion(), bean.getSize(), bean.getContent());
        dialog.setTitle(mContext.getString(R.string.dialog_apk_update_title)).setTextContext(context)
                .setOnDialogStatusListener(onDialogStatusListener)
                .setPositive(mContext.getString(R.string.dialog_apk_update_positive), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downLoadManager.setOnDialogStatusListener(onDialogStatusListener);
                        downLoadManager.downloadFile(Config.HOST, bean.getUrl(), judgeName(bean.getName()));
                        /**没有返回监听的关闭*/
                        dialog.onDismiss();
                    }
                })
                .setNegative(mContext.getString(R.string.dialog_apk_update_negative), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                })
                .show();
        /**点击外部不能关闭*/
        dialog.setCancelable(false);
    }

    /**
     * 判断名字是否符合规范
     *
     * @param name
     * @return
     */
    public static String judgeName(String name) {
        if (name != null && name.endsWith(".apk")) {
            return name;
        } else {
            return FileConstant.APK_NAME;
        }
    }

}
