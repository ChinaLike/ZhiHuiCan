package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.TabBean;
import cn.sczhckg.order.fragment.OrderFragment;

/**
 * @describe: 头部导航栏数据
 * @author: Like on 2016/11/11.
 * @Email: 572919350@qq.com
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.TabViewHolder> {

    private Context mContext;

    private List<TabBean> mList;

    private Map<Integer, LinearLayout> layouts = new HashMap<>();

    private Map<Integer, View> views = new HashMap<>();

    private int current = 0;

    public TabAdapter(Context mContext, List<TabBean> mList) {
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

        views.get(current).setSelected(true);
        holder.tabName.setText(mList.get(position).getName());
        holder.tabParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views.get(position).setSelected(true);
                views.get(current).setSelected(false);
                current = position;
                OrderFragment.tabOrderType = mList.get(position).getId();
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

    public void notifyDataSetChanged(List<TabBean> mList) {
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
}
