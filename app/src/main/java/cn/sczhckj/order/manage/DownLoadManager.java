package cn.sczhckj.order.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.sczhckj.order.activity.InitActivity;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.data.network.DownloadApi;
import cn.sczhckj.order.helper.DownloadProgressHandler;
import cn.sczhckj.order.helper.ProgressHelper;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.until.FileUntils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @describe: 下载管理
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class DownLoadManager {

    private CommonDialog dialog;

    private Context mContext;

    private String apkName;

    private CommonDialog.OnDialogStatusListener onDialogStatusListener;
    /**
     * 是否弹出安装界面
     */
    private boolean isInstall = false;

    public DownLoadManager(Context context) {
        mContext = context;
    }

    public void setOnDialogStatusListener(CommonDialog.OnDialogStatusListener onDialogStatusListener) {
        this.onDialogStatusListener = onDialogStatusListener;
    }

    /**
     * 弹窗初始化
     */
    private void initDialog() {
        dialog = new CommonDialog(mContext, CommonDialog.Mode.PROGRESS);
        dialog.setTitle("版本更新")
                .setOnDialogStatusListener(onDialogStatusListener)
                .setProgressMax(0)
                .setProgress(0)
                .setProgressText(String.format("%1s Kb/%2s Kb", (int) (0 / 1024), (int) (0 / 1024)))
                .setNegative("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).show();
        dialog.setCancelable(false);
    }

    /**
     * 文件下载
     *
     * @param host    主机地址
     * @param url     链接地址
     * @param apkName APK名称
     */
    public void downloadFile(String host, String url, final String apkName) {
        this.apkName = apkName;
        if (!InitActivity.isAuto) {
            initDialog();
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(host);
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        DownloadApi retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);
        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                if (!InitActivity.isAuto) {
                    dialog.setProgressMax((int) (contentLength / 1024));
                    dialog.setProgress((int) (bytesRead / 1024));
                    dialog.setProgressText(String.format("%1s Kb/%2s Kb", (int) (bytesRead / 1024), (int) (contentLength / 1024)));
                }
                if (done && !isInstall) {
                    isInstall = true;
                    install();
                }
            }
        });

        Call<ResponseBody> call = retrofit.retrofitDownload(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(FileUntils.getSdPath() + FileConstant.PATH, apkName);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    is.close();
                    fos.close();
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 安装应用
     */
    private void install() {
        dialog.onDismiss();
        final File file = new File(FileUntils.getSdPath() + FileConstant.PATH, apkName);
        final CommonDialog mDialog = new CommonDialog(mContext, CommonDialog.Mode.TEXT);
        mDialog.setTitle("应用安装")
                .setTextContext("下载已完成，请确定安装应用！")
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.onDismiss();
                        autoInstall(file);
                    }
                })
                .show();
        mDialog.setCancelable(false);
    }

    /**
     * 自动安装
     */
    private void autoInstall(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
