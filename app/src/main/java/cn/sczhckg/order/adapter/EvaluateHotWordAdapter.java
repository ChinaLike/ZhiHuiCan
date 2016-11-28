package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.EvaluateBean;

/**
 * @describe: 评价热词适配
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class EvaluateHotWordAdapter extends RecyclerView.Adapter<EvaluateHotWordAdapter.EvaluateHotWordViewHolder> {

    private Context mContext;

    private List<EvaluateBean> mList;

    public EvaluateHotWordAdapter(Context mContext, List<EvaluateBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public EvaluateHotWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvaluateHotWordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_evaluate_btn, parent, false));
    }

    @Override
    public void onBindViewHolder(final EvaluateHotWordViewHolder holder, int position) {
        holder.evaluateHotWord.setText(mList.get(position).getText());
        holder.evaluateHotWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.evaluateHotWord.isSelected()){
                    holder.evaluateHotWord.setSelected(false);
                    holder.evaluateHotWord.setTextColor(0xFF333333);
                }else {
                    holder.evaluateHotWord.setSelected(true);
                    holder.evaluateHotWord.setTextColor(mContext.getResources().getColor(R.color.button_text));
                }
            }
        });
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

    static class EvaluateHotWordViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.evaluate_hot_word)
        Button evaluateHotWord;

        public EvaluateHotWordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
