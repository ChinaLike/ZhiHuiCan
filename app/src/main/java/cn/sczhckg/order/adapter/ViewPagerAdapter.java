package cn.sczhckg.order.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe: 主页面适配器
 * @author: Like on 2016/11/7.
 * @Email: 572919350@qq.com
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Fragment> mList) {
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        if (mList != null) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
