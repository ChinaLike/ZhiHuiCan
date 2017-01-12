package cn.sczhckj.order.manage;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.overwrite.MyDialog;

/**
 * @describe: 版本管理
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionManager {

    private DownLoadManager downLoadManager;

    private MyDialog downLoadDialog;

    private Context mContext;

    private int maxs = 0;

    public VersionManager(Context mContext) {
        this.mContext = mContext;
        downLoadManager = new DownLoadManager();
    }

    public interface OnDialogClickListener {
        void show();

        void dismiss();
    }

    private OnDialogClickListener onDialogClickListener;

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
    public void version(Context mContext, VersionBean bean) {
        if (getVersionCode(mContext) < bean.getCode()) {
            /**服务器有更新*/
            updataVersion(mContext, bean);
        }
    }

    /**
     * 提示当前有新版本
     */
    public void updataVersion(final Context mContext, final VersionBean bean) {
        String context = "当前版本：" + getVersionName(mContext)
                + "\n最新版本：" + bean.getVersion()
                + "\n更新大小：" + bean.getSize()
                + "\n更新内容:" + bean.getContent();
        final MyDialog dialog = new MyDialog(mContext);
        dialog.setTitle("版本更新");
        dialog.setContextText(context);
        dialog.setNegativeButton("马上更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadDialog(mContext);
                downLoadManager.isMkdir();
                if (bean.getName() == null) {
                    downLoadManager.retrofitDownload(Config.HOST, bean.getUrl(), FileConstant.APK_NAME, downLoadDialog, mContext);
                } else {
                    downLoadManager.retrofitDownload(Config.HOST, bean.getUrl(), bean.getName(), downLoadDialog, mContext);
                }
                onDialogClickListener.show();
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("暂不更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogClickListener.dismiss();
            }
        });
        /**点击外部不能关闭*/
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 当前没有新版本
     */
    public void notUpdata(Context mContext) {

    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    /**
     * 下载弹窗
     *
     * @param mContext
     */
    private void downLoadDialog(Context mContext) {
        downLoadDialog = new MyDialog(mContext);
        downLoadDialog.setTitle("软件更新");
        downLoadDialog.setContextText("正在更新中...");
        downLoadDialog.setProgressVisibility();
        downLoadDialog.setAloneButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadDialog.dismiss();
                onDialogClickListener.dismiss();
            }
        });
        downLoadDialog.show();
    }

}
