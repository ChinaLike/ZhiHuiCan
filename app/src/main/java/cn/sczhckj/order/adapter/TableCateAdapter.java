package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.produce.TableCateBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;

/**
 * @ describe: 台桌分类,用于服务员模式
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class TableCateAdapter extends RecyclerView.Adapter<TableCateAdapter.TableCateViewHolder> {

    private Context mContext;

    private List<TableCateBean> mList;

    private OnItemClickListener onItemClickListener;

    public TableCateAdapter(Context mContext, List<TableCateBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TableCateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableCateViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table_cate, parent, false));
    }

    @Override
    public void onBindViewHolder(TableCateViewHolder holder, final int position) {
        final TableCateBean bean = mList.get(position);
        holder.tableCate.setText(bean.getDistrict());
        if (bean.isSelect()) {
            holder.tableCate.setSelected(true);
        } else {
            holder.tableCate.setSelected(false);
        }
        holder.tableCate.setOnClickListener(new View.OnClickListener() {
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

    public void notifyDataSetChanged(List<TableCateBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void setBackground(TableCateBean item) {
        for (TableCateBean bean : mList) {
            bean.setSelect(false);
            if (item.equals(bean)) {
                bean.setSelect(true);
                notifyDataSetChanged();
            }
        }
    }

    static class TableCateViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.table_cate)
        TextView tableCate;

        public TableCateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
