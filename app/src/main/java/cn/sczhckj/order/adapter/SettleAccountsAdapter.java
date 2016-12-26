package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.bill.BillBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.image.GlideLoading;

/**
 * @describe: 结账界面清单数据适配
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<BillBean> mList;

    public SettleAccountsAdapter(Context mContext, List<BillBean> mList) {
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
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    /**
     * 获得某个父项的子项数目
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (mList.get(groupPosition).getFoods() == null) {
            return 0;
        } else {
            return mList.get(groupPosition).getFoods().size();
        }
    }

    /**
     * 获得某个父项
     *
     * @param groupPosition
     * @return
     */
    @Override
    public BillBean getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public FoodBean getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getFoods().get(childPosition);
    }

    /**
     * 获得某个父项的id
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获得某个父项的某个子项的id
     *
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
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 获得父项显示的view
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expandable_list_one_classify, null, false);
        ParentViewHolder holder = new ParentViewHolder(convertView);
        holder.name.setText(getGroup(groupPosition).getName().toString());
        holder.number.setText("×" + getGroup(groupPosition).getCount() + "");
        holder.totalPrice.setText("" + getGroup(groupPosition).getSum());
        holder.favorablePrice.setVisibility(View.INVISIBLE);
        holder.price.setVisibility(View.INVISIBLE);
        holder.image.setVisibility(View.GONE);
        if (isExpanded) {
            holder.tag.setImageResource(R.drawable.accounts_btn_details_sel);
        }
        return convertView;
    }

    /**
     * 获得子项显示的view
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expandable_list_two_classify, null, false);
        ChildViewHolder holder = new ChildViewHolder(convertView);
        holder.name.setText(getChild(groupPosition, childPosition).getName().toString());
        holder.number.setText("×" + getChild(groupPosition, childPosition).getCount());
        /**优惠后价格,没有优惠价原价与总价相等*/
        holder.favorablePrice.setText("" + getChild(groupPosition, childPosition).getPrice());
        if (getChild(groupPosition, childPosition).getPriceImageUrl() != null &&
                getChild(groupPosition, childPosition).getOriginPrice() > getChild(groupPosition, childPosition).getPrice()) {
            /**设置原价*/
            holder.price.setText("¥ " + getChild(groupPosition, childPosition).getOriginPrice());
            holder.image.setVisibility(View.VISIBLE);
            GlideLoading.loadingDishes(mContext, getChild(groupPosition, childPosition).getPriceImageUrl(), holder.image);
        } else {
            holder.price.setVisibility(View.GONE);
            holder.image.setVisibility(View.INVISIBLE);
        }
        /**设置Item总价*/
        holder.totalPrice.setText("" + getChild(groupPosition, childPosition).getCount() * getChild(groupPosition, childPosition).getPrice());
        return convertView;
    }

    /**
     * 子项是否可选中，如果需要设置子项的点击事件，需要返回true
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void notifyDataSetChanged(List<BillBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class ParentViewHolder {
        @Nullable
        @Bind(R.id.el1_favorable_image)
        ImageView image;
        @Nullable
        @Bind(R.id.el1_dishes_name)
        TextView name;
        @Nullable
        @Bind(R.id.el1_dishes_number)
        TextView number;
        @Nullable
        @Bind(R.id.el1_favorable_price)
        TextView favorablePrice;
        @Nullable
        @Bind(R.id.el1_price)
        TextView price;
        @Nullable
        @Bind(R.id.el1_total_price)
        TextView totalPrice;
        @Nullable
        @Bind(R.id.tag)
        ImageView tag;
        @Nullable
        @Bind(R.id.parent)
        LinearLayout parent;

        ParentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Nullable
        @Bind(R.id.el2_favorable_image)
        ImageView image;
        @Nullable
        @Bind(R.id.el2_dishes_name)
        TextView name;
        @Nullable
        @Bind(R.id.el2_dishes_number)
        TextView number;
        @Nullable
        @Bind(R.id.el2_favorable_price)
        TextView favorablePrice;
        @Nullable
        @Bind(R.id.el2_price)
        TextView price;
        @Nullable
        @Bind(R.id.el2_total_price)
        TextView totalPrice;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
