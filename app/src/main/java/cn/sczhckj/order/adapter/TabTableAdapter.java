package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.mode.impl.StatusSwitchImpl;
import cn.sczhckj.order.overwrite.CheckSwitchButton;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @describe: 头部导航栏数据
 * @author: Like on 2016/11/11.
 * @Email: 572919350@qq.com
 */

public class TabTableAdapter extends RecyclerView.Adapter<TabTableAdapter.TabViewHolder> {

    private Context mContext;

    private List<InfoBean> mList;

    private Map<Integer, LinearLayout> layouts = new HashMap<>();

    private Map<Integer, View> views = new HashMap<>();

    private int current = 0;
    /**
     * 平分等分
     */
    private int ITEM_WIDTH = 2;
    /**
     * 状态切换
     */
    private StatusSwitchImpl mStatusSwitch;

    private OnItemClickListener onItemClickListener;

    private Integer currTableId;

    public TabTableAdapter(Context mContext, List<InfoBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mStatusSwitch = new StatusSwitchImpl(mContext);
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
        holder.tabParent.setLayoutParams(new LinearLayout.LayoutParams(getItemWidth(), LinearLayout.LayoutParams.MATCH_PARENT));
        /**设置主桌显示切换按钮*/
        if (mList.get(position).getId() == currTableId) {
            views.get(position).setSelected(true);
            holder.statusSwitch.setVisibility(View.VISIBLE);
            mStatusSwitch.setButtonView(holder.statusSwitch);
        } else {
            views.get(position).setSelected(false);
            holder.statusSwitch.setVisibility(View.GONE);
        }

        holder.tabName.setText(mList.get(position).getName());
        holder.tabParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views.get(position).setSelected(true);
                views.get(current).setSelected(false);
                current = position;
                onItemClickListener.onItemClick(holder.tabParent, holder.getLayoutPosition(), mList.get(position));
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
     *
     * @param mList
     */
    public void notifyDataSetChanged(List<InfoBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * 当前台桌ID
     *
     * @param currTableId
     */
    public void setCurrTableId(Integer currTableId) {
        this.currTableId = currTableId;
    }

    static class TabViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tab_name)
        TextView tabName;
        @Bind(R.id.tab_line)
        View tabLine;
        @Bind(R.id.tab_parent)
        LinearLayout tabParent;
        @Bind(R.id.statusSwitch)
        CheckSwitchButton statusSwitch;

        public TabViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 获取每一项的宽度
     *
     * @return
     */
    private int getItemWidth() {
        /**32.0 和 42.0 是父类布局文件在界面所占比例（1:3.2）所得*/
        return (int) ((MainActivity.rightWidth * 32.0) / 42.0) / ITEM_WIDTH;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
