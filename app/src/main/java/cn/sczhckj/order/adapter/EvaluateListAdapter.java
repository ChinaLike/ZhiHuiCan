package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckj.order.data.event.EvaluateListEvent;

/**
 * @describe: 评价菜品清单
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class EvaluateListAdapter extends RecyclerView.Adapter<EvaluateListAdapter.ListViewHolder> {

    private Context mContext;

    private List<SettleAccountsDishesItemBean> mList;

    private Map<Integer, Button> mMap = new HashMap<>();

    private int current=0;

    public EvaluateListAdapter(Context mContext, List<SettleAccountsDishesItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_adapter_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        mMap.put(holder.getLayoutPosition(), holder.evaluateBtn);
        holder.evaluateBtn.setText(mList.get(position).getName());
        if (current==position){
            holder.evaluateBtn.setSelected(true);
            holder.evaluateBtn.setTextColor(mContext.getResources().getColor(R.color.button_text));
        }else {
            holder.evaluateBtn.setSelected(false);
            holder.evaluateBtn.setTextColor(0xFF333333);
        }
        holder.evaluateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.get(position).setSelected(true);
                mMap.get(position).setTextColor(mContext.getResources().getColor(R.color.button_text));
                mMap.get(current).setSelected(false);
                mMap.get(current).setTextColor(0xFF333333);
                current=position;
                EventBus.getDefault().post(new EvaluateListEvent(EvaluateListEvent.ITEM, mList.get(position)));
            }
        });
    }

    public void notifyDataSetChanged(List<SettleAccountsDishesItemBean> mList){
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

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.dishes_evaluate)
        Button evaluateBtn;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
