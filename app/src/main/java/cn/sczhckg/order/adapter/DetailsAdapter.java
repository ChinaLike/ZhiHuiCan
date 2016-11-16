package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PriceTypeBean;

/**
 * @describe:
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private Context mContext;

    private List<PriceTypeBean> mList;

    public DetailsAdapter(Context mContext, List<PriceTypeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_favorable, parent, false));
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        PriceTypeBean bean=mList.get(position);
        Picasso.with(mContext).load(bean.getUrl()).into(holder.image);
        holder.title.setText(bean.getTitle());
        holder.price.setText("¥  "+bean.getPrice());
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }
        return 0;
    }

    static class DetailsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
