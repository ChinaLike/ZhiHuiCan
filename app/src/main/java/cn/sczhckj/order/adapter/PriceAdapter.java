package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.PriceBean;
import cn.sczhckj.order.data.bean.PriceTypeBean;
import cn.sczhckj.order.overwrite.TextViewBorder;
import cn.sczhckj.order.until.ColorUntils;

/**
 * @describe:
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.DetailsViewHolder> {

    private Context mContext;

    private List<PriceBean> mList;

    public PriceAdapter(Context mContext, List<PriceBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_favorable, parent, false));
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        PriceBean bean=mList.get(position);
        /**设置内容*/
        holder.context.setText(bean.getTitle()+"：¥"+bean.getPrice());
        /**设置字体颜色*/
        holder.context.setTextColor(ColorUntils.stringToHex(bean.getColor()));
        /**设置边框颜色*/
        holder.context.setBorderColor(ColorUntils.stringToHex(bean.getColor()));
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }
        return 0;
    }

    static class DetailsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.favorable_context)
        TextViewBorder context;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
