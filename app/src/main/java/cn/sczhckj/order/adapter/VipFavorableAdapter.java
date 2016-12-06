package cn.sczhckj.order.adapter;

import android.content.Context;
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
import cn.sczhckj.order.data.bean.VipFavorableBean;
import cn.sczhckj.order.data.event.ApplyForVipCardEvent;

/**
 * @describe: 这种会员优惠价格一览表显示，并实现办理卡片
 * @author: Like on 2016/11/21.
 * @Email: 572919350@qq.com
 */

public class VipFavorableAdapter extends RecyclerView.Adapter<VipFavorableAdapter.VipFavorableHolder> {

    private List<VipFavorableBean> mList;

    private Context mContext;

    public VipFavorableAdapter(List<VipFavorableBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public VipFavorableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VipFavorableHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pop_vip_favorable, parent, false));
    }

    @Override
    public void onBindViewHolder(final VipFavorableHolder holder, int position) {
        final VipFavorableBean bean=mList.get(position);
        holder.vipName.setText(bean.getName());
        holder.vipPrice.setText("¥ "+bean.getPrice());
        holder.vipFavorablePrice.setText("¥ "+bean.getFavorablePrice());
        if (bean.getIsThis()==0){
            holder.vipChoose.setVisibility(View.VISIBLE);
            holder.transactionVip.setVisibility(View.GONE);
        }else {
            holder.vipChoose.setVisibility(View.GONE);
            holder.transactionVip.setVisibility(View.VISIBLE);
        }
        if (bean.isSelect()){
            holder.transactionVip.setSelected(true);
            holder.transactionVip.setText(mContext.getResources().getString(R.string.button_vip_select));
        }else {
            holder.transactionVip.setSelected(false);
            holder.transactionVip.setText(mContext.getResources().getString(R.string.button_vip_no_select));
        }
        /**办理会员卡*/
        holder.transactionVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (VipFavorableBean item:mList) {
                    item.setSelect(false);
                }
                if (holder.transactionVip.isSelected()){
                    /**取消办卡*/
                    EventBus.getDefault().post(new ApplyForVipCardEvent(ApplyForVipCardEvent.CANCEL_APPLY,bean.getId()));
                }else {
                    /**申请办卡*/
                    bean.setSelect(true);
                    EventBus.getDefault().post(new ApplyForVipCardEvent(ApplyForVipCardEvent.APPLY,bean.getId(),mList));
                }
                notifyDataSetChanged();
            }
        });

    }

    public void notifyDataSetChanged(List<VipFavorableBean> mList) {
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
