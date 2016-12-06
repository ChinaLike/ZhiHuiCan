package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;

/**
 * @describe: 团购券适配器
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class GrouponAdapter extends RecyclerView.Adapter<GrouponAdapter.GrouponViewHolder> {

    private Context mContext;

    private List<String> mList;

    public GrouponAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public GrouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrouponViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_groupon, parent, false));
    }

    @Override
    public void onBindViewHolder(GrouponViewHolder holder, final int position) {
        holder.inputParent.setVisibility(View.GONE);
        holder.grouponNumber.setVisibility(View.VISIBLE);
        holder.grouponNumber.setText(mList.get(position));
        holder.grouponClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyDataSetChanged();
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

    public void notifyDataSetChanged(List<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class GrouponViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.groupon_number)
        TextView grouponNumber;
        @Bind(R.id.input_parent)
        LinearLayout inputParent;
        @Bind(R.id.groupon_close)
        ImageView grouponClose;

        public GrouponViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
