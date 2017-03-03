package cn.sczhckj.order.manage;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.overwrite.MyDialog;

/**
 * @describe: 版本管理
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionManager {

    private DownLoadManager downLoadManager;

    private Context mContext;

    public VersionManager(Context mContext) {
        this.mContext = mContext;
        downLoadManager = new DownLoadManager(mContext);
    }

    /**
     * 获取软件版本号
     */
    public int getVersionCode(Context mContext) {
        int verCode = -1;
        try {
            verCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param mContext
     * @return
     */
    public String getVersionName(Context mContext) {
        String versionName = null;
        try {
            versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 版本验证
     *
     * @param mContext
     * @param bean
     */
    public void version(Context mContext, VersionBean bean, CommonDialog.OnDialogStatusListener listener) {
        if (getVersionCode(mContext) < bean.getCode()) {
            /**服务器有更新*/
            updataVersion(mContext, bean, listener);
        }
    }

    /**
     * 提示当前有新版本
     */
    public void updataVersion(final Context mContext, final VersionBean bean, CommonDialog.OnDialogStatusListener listener) {
        String context = mContext.getString(R.string.dialog_apk_update_content, getVersionName(mContext), bean.getVersion(), bean.getSize(), bean.getContent());
        final CommonDialog dialog = new CommonDialog(mContext, CommonDialog.Mode.TEXT);
        dialog.setTitle(mContext.getString(R.string.dialog_apk_update_title)).setTextContext(context)
                .setOnDialogStatusListener(listener)
                .setPositive(mContext.getString(R.string.dialog_apk_update_positive), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downLoadDialog();
                        downLoadManager.retrofitDownload(Config.HOST, bean.getUrl(), judgeName(bean.getName()));
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
     * 下载弹窗
     */
    private void downLoadDialog() {
        final CommonDialog dialog = new CommonDialog(mContext, CommonDialog.Mode.TEXT);
        dialog.setTitle(mContext.getString(R.string.dialog_apk_updating_title))
                .setTextContext(mContext.getString(R.string.dialog_apk_updating_content))
                .setPositive(mContext.getString(R.string.dialog_apk_updating_negative), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).show();
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
