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
import cn.sczhckg.order.data.bean.PayTypeBean;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;
import cn.sczhckg.order.data.listener.OnPayTypeListenner;

/**
 * @describe: 结账方式适配
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsPayAdapter extends RecyclerView.Adapter<SettleAountsPayAdapter.ContextViewHolder> {

    private Context mContext;

    private List<PayTypeBean> mList;

    private Map<Integer,Button> mMap=new HashMap<>();

    private OnPayTypeListenner onPayTypeListenner;

    public SettleAountsPayAdapter(Context mContext, List<PayTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public SettleAountsPayAdapter.ContextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SettleAountsPayAdapter.ContextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_context, parent, false));
    }

    @Override
    public void onBindViewHolder(final SettleAountsPayAdapter.ContextViewHolder holder, final int position) {
        if (!mMap.containsKey(holder.getLayoutPosition())){
            mMap.put(holder.getLayoutPosition(),holder.cartType);
        }
        holder.cartType.setText(mList.get(position).getName());
        holder.cartType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer btn:mMap.keySet()) {
                    mMap.get(btn).setSelected(false);
                }
                holder.cartType.setSelected(true);
                onPayTypeListenner.payType(mList.get(position));
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


    public void notifyDataSetChanged(List<PayTypeBean> mList) {
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

    public void setOnPayTypeListenner(OnPayTypeListenner onPayTypeListenner) {
        this.onPayTypeListenner = onPayTypeListenner;
    }
}
