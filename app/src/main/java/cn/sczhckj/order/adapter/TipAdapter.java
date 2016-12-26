package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @ describe:  打赏
 * @ author: Like on 2016/12/26.
 * @ email: 572919350@qq.com
 */

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private Context mContext;

    private List<Integer> mList;

    public TipAdapter(Context mContext, List<Integer> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {

        public TipViewHolder(View itemView) {
            super(itemView);
        }
    }

}
