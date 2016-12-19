package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.service.ServiceBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.image.GlideLoading;

/**
 * @ describe: 服务类型列表适配器
 * @ author: Like on 2016/12/19.
 * @ email: 572919350@qq.com
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<ServiceBean> mList;

    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public ServiceAdapter(List<ServiceBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_service_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, final int position) {
        final ServiceBean bean = mList.get(position);
        GlideLoading.loadingService(mContext, bean.getImageUrl(), holder.serviceItemImage);
        holder.serviceItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position, bean);
            }
        });
    }

    public void notifyDataSetChanged(List<ServiceBean> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.service_item_image)
        ImageView serviceItemImage;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
