package cn.sczhckj.order.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import cn.sczhckj.order.manage.VersionManager;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.overwrite.SettingPopupWindow;
import cn.sczhckj.order.permission.OnPermissionCallback;
import cn.sczhckj.order.permission.PermissionManager;
import cn.sczhckj.order.service.WebSocketService;
import cn.sczhckj.order.until.AndroidVersionUtil;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.FileUntils;
import cn.sczhckj.order.until.NetUtils;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 1.WebSocket连接
 * 2.初始化版本信息
 * 3.初始化台桌信息(获取是否是哪一个模式，服务员还是消费者模式)
 */
public class InitActivity extends Activity implements Callback<Bean<VersionBean>>,
        CommonDialog.OnDialogStatusListener, OnPermissionCallback, SettingPopupWindow.OnButtonListener {

    @Bind(R.id.init_text)
    TextView initText;
    @Bind(R.id.init_parent)
    RelativeLayout initParent;
    @Bind(R.id.deviceid)
    TextView deviceid;

    private String TAG = getClass().getName();

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
    public static boolean isAuto = false;
    /**
     * 需要申请的权限
     */
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 服务
     */
    private Intent intent;
    /**
     * 设置窗口
     */
    private SettingPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        /**显示设备ID*/
        deviceid.setText(getString(R.string.init_activity_device, AppSystemUntil.getAndroidID(this)));

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
        initText.setText(getString(R.string.init_activity_loading));
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
        initText.setText(getString(R.string.init_activity_loading));
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
            mTableMode.tableInit(InitActivity.this, openInfoCallback);
        }
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
        initText.setText(getString(R.string.init_activity_loading));
        init();
    }

    @Override
    public void onResponse(Call<Bean<VersionBean>> call, Response<Bean<VersionBean>> response) {
        Bean<VersionBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            mVersionManager.setOnDialogStatusListener(this);
            mVersionManager.updata(bean.getResult());
        } else {
            initTableInfo();
        }
    }

    @Override
    public void onFailure(Call<Bean<VersionBean>> call, Throwable t) {
        initTableInfo();
    }

    /**
     * 台桌初始化信息返回
     */
    Callback<Bean<TableBean>> openInfoCallback = new Callback<Bean<TableBean>>() {
        @Override
        public void onResponse(Call<Bean<TableBean>> call, Response<Bean<TableBean>> response) {
            Bean<TableBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                MyApplication.tableBean = bean.getResult();
                MyApplication.mode = bean.getResult().getMode();
                initText.setText(getString(R.string.init_activity_loading_success));
                if (bean.getResult().getMode() == Constant.CUSTOMER) {
                    /**消费者模式*/
                    intentLead();
                } else if (bean.getResult().getMode() == Constant.PRODUCER) {
                    /**服务员模式*/
                    intentWaitressLogin();
                }

            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                initParent.setClickable(true);
                initText.setText(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<TableBean>> call, Throwable t) {
            initParent.setClickable(true);
            initText.setText(getString(R.string.init_activity_loading_fail));
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
        if (WebSocketEvent.INIT_SUCCESS == event.getType() && isTopActivity()) {
            /**初始化成功，当WebSocket连接成功时在获取版本信息*/
            isAuto = false;
            getVersion();
        } else if (WebSocketEvent.INIT_FAIL == event.getType()) {
            /**初始化失败*/
            initParent.setClickable(true);
            initText.setText(getString(R.string.init_activity_loading_fail));
            if (NetUtils.isConnected(this)){
                //网络已经连接
                if (popupWindow == null) {
                    popupWindow = new SettingPopupWindow(InitActivity.this);
                }
                popupWindow.setOnButtonListener(this);
                popupWindow.show();
            }else {
                T.showShort(this,"请检查网络是否连接");
            }
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

    /**
     * 跳转引导页，消费者模式
     */
    private void intentLead() {
        Intent intent = new Intent(InitActivity.this, LeadActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转服务员登录，服务员模式
     */
    private void intentWaitressLogin() {
        Intent intent = new Intent(InitActivity.this, WaitressLoginActivity.class);
        startActivity(intent);
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
     * 判断是否顶层Activity
     *
     * @return
     */
    private boolean isTopActivity() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().contains(TAG);
    }

    @Override
    public void affirm() {
        popupWindow.dismiss();
        onClick();
    }
}
