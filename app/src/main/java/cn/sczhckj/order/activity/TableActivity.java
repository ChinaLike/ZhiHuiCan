package cn.sczhckj.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.SpinnerAdapter;
import cn.sczhckj.order.adapter.TableAdapter;
import cn.sczhckj.order.adapter.TableCateAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.produce.TableAttrBean;
import cn.sczhckj.order.data.bean.produce.TableBean;
import cn.sczhckj.order.data.bean.produce.TableCateBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.ProduceMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.overwrite.LoadingPopupWindow;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 台桌显示
 */
public class TableActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.table_spinner)
    Spinner tableSpinner;
    @Bind(R.id.table_cate)
    RecyclerView tableCate;
    @Bind(R.id.table)
    RecyclerView table;
    @Bind(R.id.activity_table)
    LinearLayout activityTable;
    @Bind(R.id.table_parent)
    LinearLayout tableParent;
    @Bind(R.id.temp_gif)
    GifImageView tempGif;
    @Bind(R.id.temp_text)
    TextView tempText;
    @Bind(R.id.temp_parent)
    LinearLayout tempParent;
    @Bind(R.id.temp_fail_btn)
    Button tempFail;
    /**
     * 台桌属性
     */
    private List<TableAttrBean> attrBeen = new ArrayList<>();
    /**
     * 台桌
     */
    private List<TableBean> tableBeen = new ArrayList<>();
    /**
     * 筛选的台桌
     */
    private List<TableBean> filterTable = new ArrayList<>();

    /**
     * 当前属性
     */
    private TableAttrBean currAttrBean = new TableAttrBean();

    /**
     * 台桌分类Adapter
     */
    private TableCateAdapter cateAdapter;

    /**
     * 台桌
     */
    private TableAdapter tableAdapter;

    /**
     * 台桌Mode
     */
    private ProduceMode mProduceMode;
    /**
     * 一行显示台桌数量
     */
    private final int ITEM = 4;
    /**
     * 再次获取初始化信息
     */
    private TableMode mTableMode;
    /**加载弹窗*/
    private LoadingPopupWindow mLoadingPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);
        setToolbar();
        init();
    }

    /**
     * 设置ToolBar
     */
    private void setToolbar() {
        toolbar.setTitle("餐桌选择");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.open_btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, WaitressLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        mLoadingPopupWindow = new LoadingPopupWindow(this);
        mLoadingPopupWindow.setModal(false);
        tempParent.setClickable(false);
        mProduceMode = new ProduceMode();
        mTableMode = new TableMode();
        initCateAdapter();
        initCate();
        initAttr();
        initTableAdapter();
    }

    /**
     * 加载中
     */
    private void loading(String str) {
        tempParent.setVisibility(View.VISIBLE);
        tableParent.setVisibility(View.GONE);
        tempGif.setVisibility(View.VISIBLE);
        tempFail.setVisibility(View.GONE);
        tempGif.setImageResource(R.drawable.init_loading);
        tempParent.setClickable(false);
        tempText.setText(str);
    }

    /**
     * 加载失败
     */
    private void loadingFail(String str) {
        tempParent.setVisibility(View.VISIBLE);
        tableParent.setVisibility(View.GONE);
        tempGif.setVisibility(View.GONE);
        tempFail.setVisibility(View.VISIBLE);
        tempText.setText(str);
        tempParent.setClickable(true);
        tempFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCate();
                initAttr();
            }
        });
    }

    /**
     * 加载成功
     */
    private void loadingSuccess() {
        tempParent.setVisibility(View.GONE);
        tableParent.setVisibility(View.VISIBLE);
    }

    /**
     * 分类适配器
     */
    private void initCateAdapter() {
        cateAdapter = new TableCateAdapter(TableActivity.this, null);
        cateAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TableActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tableCate.setLayoutManager(linearLayoutManager);
        tableCate.setAdapter(cateAdapter);
    }

    /**
     * 下拉列表显示台桌属性
     *
     * @param mList
     */
    private void initAttrAdapter(List<TableAttrBean> mList) {
        SpinnerAdapter adapter = new SpinnerAdapter(TableActivity.this, mList);
        tableSpinner.setOnItemSelectedListener(this);
        tableSpinner.setAdapter(adapter);
    }

    /**
     * 台桌适配器
     */
    private void initTableAdapter() {
        tableAdapter = new TableAdapter(TableActivity.this, null);
        LinearLayoutManager manager = new GridLayoutManager(TableActivity.this, ITEM);
        tableAdapter.setOnItemClickListener(tableClickListener);
        table.setLayoutManager(manager);
        table.setAdapter(tableAdapter);
    }

    /**
     * 初始化分类
     */
    private void initCate() {
        loading(getString(R.string.table_activity_loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(TableActivity.this));
        mProduceMode.tableCate(bean, cateCallback);
    }

    /**
     * 初始化属性
     */
    private void initAttr() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(TableActivity.this));
        mProduceMode.tableAttr(bean, attrCallback);
    }

    /**
     * 初始化台桌
     */
    private void initTable(TableCateBean cateBean) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(TableActivity.this));
        bean.setId(cateBean.getId());
        bean.setCode(cateBean.getCode());
        mProduceMode.allTable(bean, tableCallback);
    }

    /**
     * 分类回调
     */
    Callback<Bean<List<TableCateBean>>> cateCallback = new Callback<Bean<List<TableCateBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<TableCateBean>>> call, Response<Bean<List<TableCateBean>>> response) {
            Bean<List<TableCateBean>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                TableCateBean cateBean = new TableCateBean();
                cateBean.setDistrict("全部台桌");
                cateBean.setSelect(true);
                List<TableCateBean> list = bean.getResult();
                list.add(0, cateBean);
                /**首先加载全部的*/
                initTable(cateBean);
                cateAdapter.notifyDataSetChanged(list);
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                loadingFail(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<List<TableCateBean>>> call, Throwable t) {
            loadingFail(getString(R.string.table_activity_loading_fail));
        }
    };
    /**
     * 台桌属性回调
     */
    Callback<Bean<List<TableAttrBean>>> attrCallback = new Callback<Bean<List<TableAttrBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<TableAttrBean>>> call, Response<Bean<List<TableAttrBean>>> response) {
            Bean<List<TableAttrBean>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                loadingSuccess();
                attrBeen = bean.getResult();
                TableAttrBean tableAttrBean = new TableAttrBean();
                tableAttrBean.setAttrId(-1);
                tableAttrBean.setAttrName("全部");
                attrBeen.add(0, tableAttrBean);
                initAttrAdapter(attrBeen);
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                loadingFail(getString(R.string.table_activity_loading_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<List<TableAttrBean>>> call, Throwable t) {
            loadingFail(getString(R.string.table_activity_loading_fail));
        }
    };
    /**
     * 台桌回调
     */
    Callback<Bean<List<TableBean>>> tableCallback = new Callback<Bean<List<TableBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<TableBean>>> call, Response<Bean<List<TableBean>>> response) {
            Bean<List<TableBean>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                loadingSuccess();
                tableBeen = bean.getResult();
                filterAttr(tableBeen);
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                loadingFail(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<List<TableBean>>> call, Throwable t) {
            loadingFail(getString(R.string.table_activity_loading_fail));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /**设置当前属性*/
        currAttrBean = attrBeen.get(position);
        filterAttr(tableBeen);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void filterAttr(List<TableBean> tableBeen) {
        filterTable.clear();
        int attrId = currAttrBean.getAttrId();
        if (attrId == -1) {
            /**是-1表示全部台桌*/
            filterTable.addAll(tableBeen);
            tableAdapter.notifyDataSetChanged(filterTable);
            return;
        }
        for (TableBean bean : tableBeen) {
            if (bean.getAttr() != null) {
                for (TableAttrBean arrtBean : bean.getAttr()) {
                    if (attrId == arrtBean.getAttrId()) {
                        filterTable.add(bean);
                        break;
                    }
                }
            }
        }
        tableAdapter.notifyDataSetChanged(filterTable);
    }

    /**
     * 台桌被选中
     */
    private OnItemClickListener tableClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object bean) {
            MyApplication.deviceID = ((TableBean) bean).getDeviceId();
            initTable();
        }
    };

    /**
     * 分类点击事件
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onItemClick(View view, int position, Object bean) {
        initTable((TableCateBean) bean);
    }

    /**
     * 跳转引导页
     */
    private void intentLead() {
        Intent intent = new Intent(TableActivity.this, LeadActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取该台桌信息
     */
    private void initTable() {
        mLoadingPopupWindow.setLoadingText("台桌信息初始化中，请稍等...");
        mLoadingPopupWindow.show();
        mTableMode.tableInit(TableActivity.this, openInfoCallback);
    }

    /**
     * 获取台桌初始化信息
     */
    Callback<Bean<cn.sczhckj.order.data.bean.table.TableBean>> openInfoCallback = new Callback<Bean<cn.sczhckj.order.data.bean.table.TableBean>>() {
        @Override
        public void onResponse(Call<Bean<cn.sczhckj.order.data.bean.table.TableBean>> call, Response<Bean<cn.sczhckj.order.data.bean.table.TableBean>> response) {
            Bean<cn.sczhckj.order.data.bean.table.TableBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                mLoadingPopupWindow.dismiss();
                MyApplication.tableBean = bean.getResult();
                intentLead();
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                mLoadingPopupWindow.setLoadingText(bean.getMessage());
            } else {
                mLoadingPopupWindow.setLoadingText(getString(R.string.table_activity_open_table_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<cn.sczhckj.order.data.bean.table.TableBean>> call, Throwable t) {
            mLoadingPopupWindow.setLoadingText(getString(R.string.table_activity_open_table_fail));
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //监控/拦截/屏蔽返回键
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
