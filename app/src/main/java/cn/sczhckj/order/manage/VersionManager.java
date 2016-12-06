package cn.sczhckj.order.manage;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import java.io.FileOutputStream;
import java.io.InputStream;

import cn.sczhckj.order.data.bean.VersionBean;
import cn.sczhckj.order.overwrite.MyDialog;
import cn.sczhckj.platform.util.HttpClient;

/**
 * @describe: 版本管理
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionManager {

    public interface OnDialogClickListener{
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
        if (getVersionCode(mContext) < bean.getVersionCode()) {
            /**服务器有更新*/
            updataVersion(mContext,bean);
        }
    }

    /**
     * 提示当前有新版本
     */
    public void updataVersion(Context mContext,VersionBean bean) {
        String context="当前版本："+getVersionName(mContext)+"\n最新版本："+bean.getVersionName()+"\n更新大小："+bean.getVersionSize()+"M";
        final MyDialog dialog=new MyDialog(mContext);
        dialog.setTitle("版本更新");
        dialog.setContextText(context);
        dialog.setNegativeButton("暂不更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogClickListener.dismiss();
            }
        });

        dialog.setPositiveButton("马上更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/12/6 进入下载
                onDialogClickListener.show();
                dialog.dismiss();
            }
        });
        /**点击外部不能关闭*/
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 当前没有新版本
     */
    public void notUpdata(Context mContext){

    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

}
