package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.eval.EvalItemBean;
import cn.sczhckj.order.data.listener.OnItemClickListener;

/**
 * @describe: 评价热词适配
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.ReviewViewHolder> {

    private Context mContext;

    private List<EvalItemBean> mList;

    public EvalAdapter(Context mContext, List<EvalItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_eval, parent, false));
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        final EvalItemBean bean = mList.get(position);
        holder.evalName.setText(bean.getName());
        /**设置总星星*/
        holder.evalRatingBar.setNumStars(bean.getMaximum());
        /**设置默认星星*/
        holder.evalRatingBar.setRating(bean.getNumber());
        holder.evalRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    bean.setNumber(rating);
                }
            }
        });
    }

    public void notifyDataSetChanged(List<EvalItemBean> mList) {
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

    public List<EvalItemBean> getmList() {
        return mList;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.eval_name)
        TextView evalName;
        @Bind(R.id.eval_ratingBar)
        RatingBar evalRatingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
