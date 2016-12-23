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
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.overwrite.TextViewBorder;
import cn.sczhckj.order.until.ColorUntils;

/**
 * @ describe: 标签云适配
 * @ author: Like on 2016/12/23.
 * @ email: 572919350@qq.com
 */

public class TagCloudAdapter extends RecyclerView.Adapter<TagCloudAdapter.TagCloudViewHolder> {

    private List<PriceBean> mList;

    private Context mContext;

    public TagCloudAdapter(List<PriceBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public TagCloudViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagCloudViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_favorable, parent, false));
    }

    @Override
    public void onBindViewHolder(TagCloudViewHolder holder, int position) {
        PriceBean bean = mList.get(position);
        /**设置内容*/
        holder.favorableContext.setText(bean.getTitle() + "：¥" + bean.getPrice());
        /**设置字体颜色*/
        holder.favorableContext.setTextColor(ColorUntils.stringToHex(bean.getColor()));
        /**设置边框颜色*/
        holder.favorableContext.setBorderColor(ColorUntils.stringToHex(bean.getColor()));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class TagCloudViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.favorable_context)
        TextViewBorder favorableContext;

        public TagCloudViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
