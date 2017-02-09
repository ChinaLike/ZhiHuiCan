package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.until.show.L;

/**
 * @ describe:  打赏
 * @ author: Like on 2016/12/26.
 * @ email: 572919350@qq.com
 */

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private Context mContext;

    private List<Integer> mList;

    private OnItemClickListener onItemClickListener;

    private Map<Integer,LinearLayout> item=new HashMap<>();

    private Map<Integer,TextView> rmb=new HashMap<>();

    private Map<Integer,TextView> money=new HashMap<>();

    public TipAdapter(Context mContext, List<Integer> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TipViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_awards, parent, false));
    }

    @Override
    public void onBindViewHolder(final TipViewHolder holder, final int position) {
        holder.itemMoney.setText(mList.get(position)+"");
        item.put(position, holder.itemParent);
        rmb.put(position, holder.itemRmb);
        money.put(position, holder.itemMoney);
        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.itemParent.isSelected()) {
                    onItemClickListener.onItemClick(holder.itemParent, -1, mList.get(position));
                    holder.itemParent.setSelected(false);
                    holder.itemRmb.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));
                    holder.itemMoney.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));
                } else {
                    onItemClickListener.onItemClick(holder.itemParent, position, mList.get(position));
                    for (Integer itemC:item.keySet()) {
                        item.get(itemC).setSelected(false);
                        rmb.get(itemC).setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));
                        money.get(itemC).setTextColor(ContextCompat.getColor(mContext, R.color.text_color_red));
                    }
                    holder.itemParent.setSelected(true);
                    holder.itemRmb.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    holder.itemMoney.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }
        });
    }

    public void notifyDataSetChanged(List<Integer> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_rmb)
        TextView itemRmb;
        @Bind(R.id.item_money)
        TextView itemMoney;
        @Bind(R.id.item_parent)
        LinearLayout itemParent;

        public TipViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
