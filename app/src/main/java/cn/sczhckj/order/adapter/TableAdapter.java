package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.produce.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.listener.OnItemClickListener;

/**
 * @ describe: 台桌适配
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private Context mContext;

    private List<TableBean> mList;

    private OnItemClickListener onItemClickListener;

    public TableAdapter(Context mContext, List<TableBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, final int position) {
        final TableBean bean = mList.get(position);
        holder.tableName.setText(bean.getTableName());
        holder.tableTime.setText(bean.getTime());
        initStatusAdapter(holder.tableFlag, bean, holder.tableTime, holder.tableStatus);
        if (bean.isSelect()) {
            holder.itemTableParent.setSelected(true);
        } else {
            holder.itemTableParent.setSelected(false);
        }
        holder.itemTableParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(bean);
                onItemClickListener.onItemClick(v, position, bean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void notifyDataSetChanged(List<TableBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * 设置选中背景
     *
     * @param item
     */
    private void setBackground(TableBean item) {
        for (TableBean bean : mList) {
            bean.setSelect(false);
            if (item.equals(bean)) {
                bean.setSelect(true);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 状态适配器
     *
     * @param mRecyclerView
     * @param bean
     */
    private void initStatusAdapter(RecyclerView mRecyclerView, TableBean bean, TextView tableTime, ImageView tableStatus) {
        TableStatusAdapter adapter = new TableStatusAdapter(mContext, status(bean, tableTime, tableStatus));
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 台桌状态
     *
     * @param bean
     * @param tableTime
     * @param tableStatus
     * @return
     */
    private List<String> status(TableBean bean, TextView tableTime, ImageView tableStatus) {
        List<String> list = new ArrayList<>();
        tableStatus.setVisibility(View.GONE);
        boolean isYY = false;//是否预约
        if (bean.getStatus() == Constant.TABLE_STATUS_RESERVE) {
            list.add("预");
            isYY = true;
        } else if (bean.getStatus() == Constant.TABLE_STATUS_EMPTY) {
            list.add("空");
            return list;
        } else if (bean.getStatus() == Constant.TABLE_STATUS_OPEN || bean.getStatus() == Constant.TABLE_STATUS_FOOD) {
            list.add("餐");
            tableTime.setText("" + bean.getTime());
        } else if (bean.getStatus() == Constant.TABLE_STATUS_BILL) {
            list.add("帐");
            tableTime.setText("" + bean.getTime());
        } else if (bean.getStatus() == Constant.TABLE_STATUS_SWEEP) {
            list.add("扫");
        }
        if (bean.getConsumeType() == Constant.TABLE_TYPE_ALONE) {
            list.add("单");
            if (isYY) {
                openTable(tableStatus);
            }

        } else if (bean.getConsumeType() == Constant.TABLE_TYPE_MAIN) {
            list.add("主");
            if (isYY) {
                openTable(tableStatus);
            }
        } else if (bean.getConsumeType() == Constant.TABLE_TYPE_AUX) {
            list.add("辅");
        }

        return list;
    }

    /**
     * 到点操作
     *
     * @param view
     */
    private void openTable(final ImageView view) {
        view.setVisibility(View.VISIBLE);
        view.setSelected(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-02-26 做到点操作
                view.setSelected(true);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.table_name)
        TextView tableName;
        @Bind(R.id.table_flag)
        RecyclerView tableFlag;
        @Bind(R.id.item_table_parent)
        LinearLayout itemTableParent;
        @Bind(R.id.table_time)
        TextView tableTime;
        @Bind(R.id.table_status)
        ImageView tableStatus;

        public TableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
