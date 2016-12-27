package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.SwitchViewEvent;

/**
 * @describe: 这种会员优惠价格一览表显示，并实现办理卡片
 * @author: Like on 2016/11/21.
 * @Email: 572919350@qq.com
 */

public class VipFavorableAdapter extends RecyclerView.Adapter<VipFavorableAdapter.VipFavorableHolder> {

    private List<CardInfoBean.Card> mList;

    private Context mContext;

    public VipFavorableAdapter(List<CardInfoBean.Card> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public VipFavorableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VipFavorableHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pop_vip_favorable, parent, false));
    }

    @Override
    public void onBindViewHolder(final VipFavorableHolder holder, int position) {
        final CardInfoBean.Card bean = mList.get(position);
        holder.vipName.setText(bean.getName() + ":");
        holder.vipPrice.setText("" + bean.getPrice());
        holder.vipFavorablePrice.setText("" + (bean.getOriginPrice() - bean.getPrice()));
        if (bean.getIsLock() == Constant.LOCK) {
            holder.vipChoose.setVisibility(View.VISIBLE);
            holder.transactionVip.setVisibility(View.GONE);
        } else {
            holder.vipChoose.setVisibility(View.GONE);
            holder.transactionVip.setVisibility(View.VISIBLE);
        }
        if (bean.isSelect()) {
            holder.transactionVip.setSelected(true);
            holder.transactionVip.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.transactionVip.setSelected(false);
            holder.transactionVip.setTextColor(0xFFFC8E50);
        }
        /**办理会员卡*/
        holder.transactionVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CardInfoBean.Card item : mList) {
                    item.setSelect(false);
                }
                /**申请办卡*/
                bean.setSelect(true);
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE_CARD, bean));
                notifyDataSetChanged();
            }
        });

    }

    public void notifyDataSetChanged(List<CardInfoBean.Card> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    static class VipFavorableHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.vip_name)
        TextView vipName;
        @Bind(R.id.vip_price)
        TextView vipPrice;
        @Bind(R.id.vip_favorable_price)
        TextView vipFavorablePrice;
        @Bind(R.id.transaction_vip)
        Button transactionVip;
        @Bind(R.id.vip_choose)
        ImageView vipChoose;

        public VipFavorableHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
