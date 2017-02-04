package cn.sczhckj.order.permission;

/**
 * @ describe: 权限回调
 * @ author: Like on 2017-02-04.
 * @ email: 572919350@qq.com
 */
public interface OnPermissionCallback {
    int PERMISSION_ALERT_WINDOW = 0xad1;
    int PERMISSION_WRITE_SETTING = 0xad2;

    void onSuccess(String... permissions);

    void onFail(String... permissions);
}
