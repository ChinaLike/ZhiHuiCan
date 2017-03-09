package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;

/**
 * @ describe: 太好做状态标识
 * @ author: Like on 2017-02-26.
 * @ email: 572919350@qq.com
 */

public class TableStatusAdapter extends RecyclerView.Adapter<TableStatusAdapter.TableStatusViewHolder> {

    private Context mContext;

    private List<String> mList;

    public TableStatusAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TableStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableStatusViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table_status, parent, false));
    }

    @Override
    public void onBindViewHolder(TableStatusViewHolder holder, int position) {
        holder.parent.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class TableStatusViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.status_parent)
        TextView parent;

        public TableStatusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
