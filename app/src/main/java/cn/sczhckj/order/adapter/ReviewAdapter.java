package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.EvaluateBean;

/**
 * @describe: 评价热词适配
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;

    private List<EvaluateBean> mList;

    public ReviewAdapter(Context mContext, List<EvaluateBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_evaluate_btn, parent, false));
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {

    }

    public void notifyDataSetChanged(List<EvaluateBean> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
