package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.ListBean;
import cn.sczhckg.order.data.bean.QRCodeBean;

/**
 * @describe: 结账清单
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;

    private List<ListBean> mList;

    public ListAdapter(Context mContext, List<ListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.listTitle.setText(mList.get(position).getTitle());
        holder.listContext.setText(mList.get(position).getContext());
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_title)
        TextView listTitle;
        @Bind(R.id.list_context)
        TextView listContext;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
