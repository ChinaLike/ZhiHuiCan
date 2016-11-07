package cn.sczhckg.order.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.ViewPagerAdapter;
import cn.sczhckg.order.fragment.PotTypeFagment;
import cn.sczhckg.order.until.AppSystemUntil;

/**
 * @ Describe:主界面
 * Created by Like on 2016/11/2.
 * @ Email: 572919350@qq.com
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    /**
     * 事物管理器
     */
    private FragmentManager mFm;
    /**
     * 适配Adapter
     */
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX）

        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）

        Log.d("屏幕信息","width="+width+",height="+height+",density="+density+",densityDpi="+densityDpi+",IMEI="+ AppSystemUntil.getAndroidID(this));
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
     * @return
     */
    private List<Fragment> initFragment() {
        List<Fragment> mList = new ArrayList<>();
        mList.add(new PotTypeFagment());
        return mList;
    }

}
