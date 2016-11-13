package cn.sczhckg.order.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;

/**
 * @describe: 结账界面清单数据适配
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<SettleAccountsDishesBean> mList;

    public SettleAccountsAdapter(Context mContext, List<SettleAccountsDishesBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * 获得父项的数量
     *
     * @return
     */
    @Override
    public int getGroupCount() {
//        Log.d("mList.size()=",mList.size()+"");
//        if (mList != null) {
//            return mList.size();
//        } else {
            return 2;
//        }
    }

    /**
     * 获得某个父项的子项数目
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
//        Log.d("子项=",mList.get(groupPosition).getItemDishes().size()+"");
//        if (mList.get(groupPosition).getItemDishes() == null) {
            return 2;
//        } else {
//            return mList.get(groupPosition).getItemDishes().size();
//        }
    }

    /**
     * 获得某个父项
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getItemDishes().get(childPosition);
    }

    /**
     * 获得某个父项的id
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获得某个父项的某个子项的id
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 获得父项显示的view
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_expandable_list_one_classify,null,false);
        return view;
    }

    /**
     * 获得子项显示的view
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_expandable_list_two_classify,null,false);
        return view;
    }

    /**
     * 子项是否可选中，如果需要设置子项的点击事件，需要返回true
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void notifyDataSetChanged(List<SettleAccountsDishesBean> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

}
