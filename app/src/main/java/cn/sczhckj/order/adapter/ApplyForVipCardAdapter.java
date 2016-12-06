package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.VipFavorableBean;

/**
 * @describe: 会员卡办理会员类型
 * @author: Like on 2016/11/23.
 * @Email: 572919350@qq.com
 */

public class ApplyForVipCardAdapter extends RecyclerView.Adapter<ApplyForVipCardAdapter.ApplyForVipViewHolder> {


    private Context mContext;

    private List<VipFavorableBean> mList;

    public ApplyForVipCardAdapter(Context mContext, List<VipFavorableBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ApplyForVipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplyForVipViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_apply_for_vip, parent, false));
    }

    @Override
    public void onBindViewHolder(ApplyForVipViewHolder holder, int position) {
        VipFavorableBean bean = mList.get(position);
        holder.applyForVipName.setText(bean.getName());
        if (bean.isSelect()) {
            holder.applyForVipImage.setSelected(true);
            holder.applyForVipName.setTextColor(0xFF333333);
        } else {
            holder.applyForVipImage.setSelected(false);
            holder.applyForVipName.setTextColor(0xFF666666);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    static class ApplyForVipViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.apply_for_vip_image)
        ImageView applyForVipImage;
        @Bind(R.id.apply_for_vip_name)
        TextView applyForVipName;

        public ApplyForVipViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
