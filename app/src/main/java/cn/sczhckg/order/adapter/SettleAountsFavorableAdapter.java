package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;

/**
 * @describe: 结账优惠方式适配
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsFavorableAdapter extends RecyclerView.Adapter<SettleAountsFavorableAdapter.ContextViewHolder> {

    private Context mContext;

    private List<FavorableTypeBean> mList;

    private Map<Integer,Button> mMap=new HashMap<>();

    public SettleAountsFavorableAdapter(Context mContext, List<FavorableTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public SettleAountsFavorableAdapter.ContextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ContextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_context, parent, false));
    }

    @Override
    public void onBindViewHolder(final SettleAountsFavorableAdapter.ContextViewHolder holder, final int position) {
        if (!mMap.containsKey(holder.getLayoutPosition())){
            mMap.put(holder.getLayoutPosition(),holder.cartType);
        }
        holder.cartType.setText(mList.get(position).getName());
        holder.cartType.setSelected(false);

        holder.cartType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer btn:mMap.keySet()) {
                    mMap.get(btn).setSelected(false);
                }
                holder.cartType.setSelected(true);
                EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.FTYPE,mList.get(position)));
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


    public void notifyDataSetChanged(List<FavorableTypeBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class ContextViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cart_type)
        Button cartType;

        public ContextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
