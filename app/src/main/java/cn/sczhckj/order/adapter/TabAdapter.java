package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.CateBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.fragment.OrderFragment;
import cn.sczhckj.order.until.AppSystemUntil;

/**
 * @describe: 头部导航栏数据
 * @author: Like on 2016/11/11.
 * @Email: 572919350@qq.com
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.TabViewHolder> {

    private Context mContext;

    private List<CateBean.CateItemBean> mList;

    private Map<Integer, LinearLayout> layouts = new HashMap<>();

    private Map<Integer, View> views = new HashMap<>();

    private int current = 0;
    /**
     * 平分等分
     */
    private int ITEM_WIDTH=2;
    /**
     * 默认显示第几项
     */
    private int defaultItem=0;

    private OnItemClickListener onItemClickListener;

    public TabAdapter(Context mContext, List<CateBean.CateItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tab, parent, false));
    }

    @Override
    public void onBindViewHolder(final TabViewHolder holder, final int position) {

        layouts.put(position, holder.tabParent);
        views.put(position, holder.tabLine);
        /**设置每一项的宽度*/
        holder.tabParent.setLayoutParams(new LinearLayout.LayoutParams(getItemWidth(),LinearLayout.LayoutParams.MATCH_PARENT));
        /**设置默认选择项*/
        if (defaultItem==position) {
            views.get(position).setSelected(true);
            onItemClickListener.onItemClick(holder.tabParent,holder.getLayoutPosition(),mList.get(position));
        }else {
            views.get(position).setSelected(false);
        }
        holder.tabName.setText(mList.get(position).getName());
        holder.tabParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views.get(position).setSelected(true);
                views.get(current).setSelected(false);
                current = position;
                onItemClickListener.onItemClick(holder.tabParent,holder.getLayoutPosition(),mList.get(position));
//                OrderFragment.tabOrderType = mList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    /**
     * 刷新数据
     * @param mList
     */
    public void notifyDataSetChanged(List<CateBean.CateItemBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class TabViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tab_name)
        TextView tabName;
        @Bind(R.id.tab_line)
        View tabLine;
        @Bind(R.id.tab_parent)
        LinearLayout tabParent;

        public TabViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDefaultItem(int defaultItem) {
        this.defaultItem = defaultItem;
    }

    /**
     * 获取每一项的宽度
     * @return
     */
    private int getItemWidth(){
        int screen= AppSystemUntil.width(mContext);
        return screen/2/ITEM_WIDTH;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}