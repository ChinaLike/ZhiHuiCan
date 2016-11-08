package cn.sczhckg.order.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.MyApplication;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.ViewPagerAdapter;
import cn.sczhckg.order.data.bean.MainPagerShow;
import cn.sczhckg.order.data.bean.UserLoginBean;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.fragment.PotTypeFagment;
import cn.sczhckg.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ Describe:主界面,ceshi
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity implements Callback<MainPagerShow> {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.table_number)
    TextView tableNumber;
    @Bind(R.id.waitress)
    TextView waitress;
    @Bind(R.id.no_login)
    ImageView noLogin;
    @Bind(R.id.has_login)
    LinearLayout hasLogin;
    /**
     * 事物管理器
     */
    private FragmentManager mFm;
    /**
     * 适配Adapter
     */
    private ViewPagerAdapter adapter;
    /**
     * 设备ID
     */
    private String deviceId = "";
    /**
     * 用户ID
     */
    private String userId = "";
    /**
     * 锅底选择，推荐菜品
     */
    private PotTypeFagment mPotTypeFagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isLogin();
        init();
    }

    /**
     * 判断是否登录，登录刷新个人信息
     */
    private void isLogin() {
        deviceId = AppSystemUntil.getAndroidID(this);
        if (MyApplication.isLogin) {
            UserLoginBean bean = (UserLoginBean) getIntent().getExtras().getSerializable("userInfo");
            userId = bean.getId();
        }
        initNetData();
    }

    /**
     * 获取网络数据
     */
    @Override
    protected void initNetData() {
        Call<MainPagerShow> mainShow = RetrofitRequest.service(Config.HOST).potDataShow(userId, deviceId);
        mainShow.enqueue(this);
    }

    @Override
    protected void init() {
        mFm = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFm);
        adapter.setList(initFragment());
        viewPager.setAdapter(adapter);
    }


    /**
     * 初始化Fragment
     *
     * @return
     */
    private List<Fragment> initFragment() {
        List<Fragment> mList = new ArrayList<>();
        mPotTypeFagment = new PotTypeFagment();
        mList.add(mPotTypeFagment);
        return mList;
    }

    @Override
    public void onResponse(Call<MainPagerShow> call, Response<MainPagerShow> response) {
        MainPagerShow bean = response.body();
        /**获取数据成功*/
        if (bean.getStatus() == 0 && bean != null) {
            tableNumber.setText("桌号：" + bean.getTableNumber());
            waitress.setText("服务员：" + bean.getWaitress());
            if (mPotTypeFagment != null) {
                mPotTypeFagment.setData(bean);
            }
        }
    }

    @Override
    public void onFailure(Call<MainPagerShow> call, Throwable t) {
        Toast.makeText(this, getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }
}
