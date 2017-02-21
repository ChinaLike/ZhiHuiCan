package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.ServiceAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.service.ServiceBean;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.ServiceMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 服务界面呼叫界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ServiceFragment extends BaseFragment implements Callback<Bean<List<ServiceBean>>>, OnItemClickListener {


    @Bind(R.id.service_list)
    RecyclerView serviceList;
    @Bind(R.id.service_list_parent)
    LinearLayout serviceListParent;
    @Bind(R.id.service_gif)
    GifImageView serviceGif;
    @Bind(R.id.service_title)
    TextView serviceTitle;
    @Bind(R.id.service_hint)
    TextView serviceHint;
    @Bind(R.id.service_call_parent)
    RelativeLayout serviceCallParent;
    @Bind(R.id.loading_title)
    TextView loadingTitle;
    @Bind(R.id.loading_parent)
    LinearLayout loadingItemParent;
    @Bind(R.id.loading_fail_title)
    TextView loadingFailTitle;
    @Bind(R.id.loading_fail)
    LinearLayout loadingFail;
    @Bind(R.id.loadingParent)
    LinearLayout loadingParent;
    @Bind(R.id.service_phone)
    ImageView servicePhone;
    @Bind(R.id.service_parent)
    LinearLayout serviceParent;
    @Bind(R.id.context_parent)
    RelativeLayout contextParent;

    /**
     * 初始化GIF图片
     */
    private GifDrawable mGifDrawable = null;

    /**
     * 列表适配器
     */
    private ServiceAdapter mServiceAdapter;
    /**
     * 一行显示列数
     */
    private final int ROW_COUNT = 2;
    /**
     * 数据请求
     */
    private ServiceMode mServiceMode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(true);
        init();
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        loading(loadingParent, contextParent, loadingItemParent, loadingFail, loadingTitle,
                getString(R.string.service_fragment_init));
        initAdapter();
        mServiceMode = new ServiceMode();
        initList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化界面
     *
     * @param isShow 是否显示列表页
     */
    private void initView(boolean isShow) {
        if (isShow) {
            /**显示列表*/
            serviceListParent.setVisibility(View.VISIBLE);
            serviceCallParent.setVisibility(View.GONE);
        } else {
            /**显示动态呼叫界面*/
            serviceListParent.setVisibility(View.GONE);
            serviceCallParent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化列表适配器
     */
    private void initAdapter() {
        mServiceAdapter = new ServiceAdapter(null, mContext);
        mServiceAdapter.setOnItemClickListener(this);
        serviceList.setLayoutManager(new GridLayoutManager(mContext, ROW_COUNT));
        serviceList.setAdapter(mServiceAdapter);
    }

    /**
     * 初始化网络数据
     */
    private void initList() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        mServiceMode.services(bean, this);
    }

    /**
     * 初始化Gif
     */
    private void initGif() {
        try {
            mGifDrawable = new GifDrawable(getResources(), R.drawable.service_bg);
            serviceGif.setImageDrawable(mGifDrawable);
            serviceHint.setText(getString(R.string.service_fragment_cancel));
            serviceTitle.setText(getString(R.string.service_fragment_service));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 呼叫服务
     *
     * @param serviceId 服务类型ID
     */
    private void callService(int serviceId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setServiceId(serviceId);
        mServiceMode.call(bean, requestCommonBeanCallback);

        initGif();

    }

    /**
     * 取消服务
     */
    private void cancelService() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        mServiceMode.abort(bean, requestCommonBeanCallback);
    }

    @OnClick({R.id.loadingParent, R.id.service_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadingParent:
                /**重新加载*/
                initList();
                break;
            case R.id.service_parent:
                /**关闭服务*/
                cancelService();
                initView(true);
                break;
        }

    }

    @Override
    public void onResponse(Call<Bean<List<ServiceBean>>> call, Response<Bean<List<ServiceBean>>> response) {
        Bean<List<ServiceBean>> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            loadingSuccess(loadingParent, contextParent, loadingItemParent, loadingFail);
            mServiceAdapter.notifyDataSetChanged(bean.getResult());
        } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
            loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                    getString(R.string.service_fragment_loading_fail));
        }
    }

    @Override
    public void onFailure(Call<Bean<List<ServiceBean>>> call, Throwable t) {
        loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                getString(R.string.service_fragment_loading_fail));
    }

    @Override
    public void onItemClick(View view, int position, Object bean) {
        initView(false);
        callService(((ServiceBean) bean).getId());
    }

    /**
     * 呼叫服务或取消服务回调
     */
    Callback<Bean<ResponseCommonBean>> requestCommonBeanCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                serviceTitle.setText(bean.getResult().getMessage());
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                serviceTitle.setText(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {

        }
    };

    /**
     * WebSocket推送
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (event.getType() == WebSocketEvent.TYPE_SERVICE_COMPLETE) {
            T.showShort(mContext, event.getBean().getMessage());
            cancelService();
            initView(true);
        }
    }

}
