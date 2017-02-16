package cn.sczhckj.order.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.Config;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.manage.DownLoadManager;
import cn.sczhckj.order.manage.VersionManager;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.permission.OnPermissionCallback;
import cn.sczhckj.order.permission.PermissionManager;
import cn.sczhckj.order.service.WebSocketService;
import cn.sczhckj.order.until.AndroidVersionUtil;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.FileUntils;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 初始化台桌信息，版本信息
 */
public class InitActivity extends Activity implements Callback<Bean<VersionBean>>,
        VersionManager.OnDialogClickListener, OnPermissionCallback {

    @Bind(R.id.init_text)
    TextView initText;
    @Bind(R.id.init_parent)
    RelativeLayout initParent;
    @Bind(R.id.deviceid)
    TextView deviceid;

    /**
     * 版本管理
     */
    private VersionManager mVersionManager;
    /**
     * 台桌信息获取
     */
    private TableMode mTableMode;

    /**
     * 手动更新或者是后台自动更新
     */
    private boolean isAuto = false;
    /**
     * 需要申请的权限
     */
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 服务
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        System.out.println(5/0+"");
        /**显示设备ID*/
        deviceid.setText("设备ID：" + AppSystemUntil.getAndroidID(this));
        if (AndroidVersionUtil.hasM()) {
            PermissionManager.getInstance().requestPermission(InitActivity.this, this, PERMISSIONS_STORAGE);
        } else {
            /**创建下载文件夹*/
            FileUntils.createFileDir(FileConstant.PATH);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        initText.setText("数据初始化中...");
        mTableMode = new TableMode();
        mVersionManager = new VersionManager(InitActivity.this);
        regsWebSocket();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(intent);
    }

    /**
     * 获取当前网络上的版本信息
     */
    private void getVersion() {
        initText.setText("数据初始化中...");
        initParent.setClickable(false);
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        bean.setType(Constant.VERSION_TYPE);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.DEVICE_UPDATE)
                .time()
                .bean(bean);
        /**进行数据校验*/
        Call<Bean<VersionBean>> vipCallBack = RetrofitRequest.service().version(restRequest.toRequestString());
        vipCallBack.enqueue(this);
    }

    /**
     * 获取台桌信息
     */
    private void initTableInfo() {
        if (!isAuto) {
            RequestCommonBean bean = new RequestCommonBean();
            bean.setDeviceId(AppSystemUntil.getAndroidID(this));
            mTableMode.tableInit(bean, openInfoCallback);
        }
    }

    /**
     * 处理台桌状态
     *
     * @param status
     * @param message
     */
    private void initTableStatus(Integer status, String message) {
        switch (status) {
            case Constant.TABLE_STATUS_DISABLE:
                /**不可用*/
            case Constant.TABLE_STATUS_NO_OPEN:
                /**未开台*/
            case Constant.TABLE_STATUS_SWEEP:
                /**打扫中*/
            case Constant.TABLE_STATUS_RESERVE:
                /**预定*/
                intentLock(status, message);
                break;
            case Constant.TABLE_STATUS_EMPTY:
                /**空桌*/
            case Constant.TABLE_STATUS_OPEN:
                /**已开桌*/
            case Constant.TABLE_STATUS_FOOD:
                /**已上菜*/
            case Constant.TABLE_STATUS_BILL:
                /**结帐中*/
                intentLead(status, message);
                break;
            default:
                /**其他*/
                intentLead(Constant.TABLE_STATUS_OTHER, null);
                break;
        }
    }

    /**
     * 跳转引导页
     *
     * @param status 状态
     */
    private void intentLead(Integer status, String remark) {
        Intent intent = new Intent(InitActivity.this, LeadActivity.class);
        intent.putExtra(Constant.INTENT_TABLE_STATUS, status);
        intent.putExtra(Constant.INTENT_TABLE_REMARK, remark);
        startActivity(intent);
    }

    /**
     * 跳转锁定界面
     *
     * @param status  状态
     * @param message 显示消息
     */
    private void intentLock(Integer status, String message) {
        Intent intent = new Intent(InitActivity.this, LockActivity.class);
        intent.putExtra(Constant.LOCK_TITLE, message);
        intent.putExtra(Constant.INTENT_TABLE_STATUS, status);
        startActivity(intent);
    }


    /**
     * 注册WebSocket
     */
    private void regsWebSocket() {
        intent = new Intent(InitActivity.this, WebSocketService.class);
        startService(intent);
    }


    @OnClick(R.id.init_parent)
    public void onClick() {
        initText.setText("数据初始化中...");
        init();
    }

    @Override
    public void onResponse(Call<Bean<VersionBean>> call, Response<Bean<VersionBean>> response) {
        Bean<VersionBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            if (bean.getResult() != null) {
                if (bean.getResult().getCode() > mVersionManager.getVersionCode(InitActivity.this)) {
                    if (!isAuto) {
                        /**手动更新*/
                        mVersionManager.version(InitActivity.this, bean.getResult());
                        mVersionManager.setOnDialogClickListener(this);
                    } else {
                        /**后台更新*/
                        DownLoadManager downLoadManager = new DownLoadManager();
                        downLoadManager.retrofitDownload(Config.HOST, bean.getResult().getUrl(),
                                VersionManager.judgeName(bean.getResult().getName()), null, InitActivity.this);
                    }
                } else {
                    initTableInfo();
                }
            } else {
                initTableInfo();
            }
        } else {
            initTableInfo();
        }
    }

    @Override
    public void onFailure(Call<Bean<VersionBean>> call, Throwable t) {
        initTableInfo();
    }

    /**
     * 台桌信息返回
     */
    Callback<Bean<TableBean>> openInfoCallback = new Callback<Bean<TableBean>>() {
        @Override
        public void onResponse(Call<Bean<TableBean>> call, Response<Bean<TableBean>> response) {
            initParent.setClickable(true);
            Bean<TableBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                L.d("初始化台桌信息：" + bean.toString());
//                MyApplication.setRecordId(bean.getResult().getRecordId());
//                MyApplication.setStatus(bean.getResult().getStatus());
//                MyApplication.setFoodCountHint(bean.getResult().getFoodCountHint() == null
//                        ? 0 : bean.getResult().getFoodCountHint());
                MyApplication.tableBean = bean.getResult();
                initTableStatus(bean.getResult().getStatus(), bean.getResult().getRemark());
                initText.setText("初始化成功");
            } else {
                initText.setText("初始化失败，点击重新初始化...");
            }
        }

        @Override
        public void onFailure(Call<Bean<TableBean>> call, Throwable t) {

            initParent.setClickable(true);
            initText.setText("初始化失败，点击重新初始化...");
        }
    };

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {
        initTableInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (WebSocketEvent.INIT_SUCCESS == event.getType()) {
            /**初始化成功，当WebSocket连接成功时在获取版本信息*/
            isAuto = false;
            getVersion();
        } else if (WebSocketEvent.INIT_FAIL == event.getType()) {
            /**初始化失败*/
            initParent.setClickable(true);
            initText.setText("初始化失败，点击重新初始化...");
        } else if (WebSocketEvent.TYPE_LOCK == event.getType()) {
            /**锁定界面有关*/
            Intent intent = new Intent(InitActivity.this, LockActivity.class);
            intent.putExtra(Constant.LOCK_TITLE, event.getMessage());
            startActivity(intent);
        } else if (WebSocketEvent.CHECK_VERSION == event.getType()) {
            /**检查版本*/
            isAuto = true;
            getVersion();
        }

    }


    @Override
    public void onSuccess(String... permissions) {
        /**创建下载文件夹*/
        FileUntils.createFileDir(FileConstant.PATH);
    }

    @Override
    public void onFail(String... permissions) {
        finish();
    }

    /**
     * 判断指定类是否运行
     *
     * @param className 包名+类名
     * @return
     */
    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) InitActivity.this
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }
}
