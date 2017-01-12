package cn.sczhckj.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.Config;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.bean.push.PushCommonBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.manage.VersionManager;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 初始化台桌信息，版本信息
 */
public class InitActivity extends Activity implements OnWebSocketListenner, Callback<Bean<VersionBean>>, VersionManager.OnDialogClickListener {

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
     * WebSocket实现
     */
    private WebSocketImpl mWebSocket;
    /**
     * 版本类型0-点菜端 1-后厨端
     */
    private final int VERSION_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);
        /**显示设备ID*/
        deviceid.setText("设备ID：" + AppSystemUntil.getAndroidID(this));
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

    /**
     * 获取当前网络上的版本信息
     */
    private void getVersion() {
        initText.setText("数据初始化中...");
        initParent.setClickable(false);
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        bean.setType(VERSION_TYPE);
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
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(this));
        mTableMode.openInfo(bean, openInfoCallback);
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
                intentLead(status);
                break;
            default:
                /**其他*/
                intentLead(Constant.TABLE_STATUS_OTHER);
                break;
        }
    }

    /**
     * 跳转引导页
     *
     * @param status 状态
     */
    private void intentLead(Integer status) {
        Intent intent = new Intent(InitActivity.this, LeadActivity.class);
        intent.putExtra(Constant.INTENT_TABLE_STATUS, status);
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
        mWebSocket = new WebSocketImpl();
        mWebSocket.push(Config.URL_LOCK_SERVICE + AppSystemUntil.getAndroidID(this), this);
    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onClose(int code, String reason) {
        initText.setText("初始化失败，点击重新初始化...");
    }

    @Override
    public void onOpen() {
        /**当WebSocket连接成功时在获取版本信息*/
        getVersion();
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onTextMessage(String payload) {
        RestRequest<PushCommonBean> restRequest
                = JSONRestRequest.Parser.parse(payload, PushCommonBean.class);
        if (OP.PUSH_LOCK.equals(restRequest.getOp())) {
            /**锁定界面*/
            Intent intent = new Intent(InitActivity.this, LockActivity.class);
            intent.putExtra(Constant.LOCK_TITLE, restRequest.getBean().getMessage());
            startActivity(intent);
        } else if (OP.PUSH_UNLOCK.equals(restRequest.getOp())) {
            /**解锁界面*/
            EventBus.getDefault().post(
                    new WebSocketEvent(WebSocketEvent.TYPE_UNLOCK, restRequest.getBean()));
        } else if (OP.PUSH_BILL_FINISH.equals(restRequest.getOp())) {
            /**结账完成*/
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.TYPE_BILL_FINISH));
        }
    }


    @OnClick(R.id.init_parent)
    public void onClick() {
        initText.setText("数据初始化中...");
        mWebSocket.reConnection();
    }

    @Override
    public void onResponse(Call<Bean<VersionBean>> call, Response<Bean<VersionBean>> response) {
        Bean<VersionBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            if (bean.getResult() != null) {
                if (bean.getResult().getCode() > mVersionManager.getVersionCode(InitActivity.this)) {
                    mVersionManager.version(InitActivity.this, bean.getResult());
                    mVersionManager.setOnDialogClickListener(this);
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
                MyApplication.setRecordId(bean.getResult().getRecordId());
                MyApplication.setStatus(bean.getResult().getStatus());
                MyApplication.setFoodCountHint(bean.getResult().getFoodCountHint() == null
                        ? 0 : bean.getResult().getFoodCountHint());
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

}
