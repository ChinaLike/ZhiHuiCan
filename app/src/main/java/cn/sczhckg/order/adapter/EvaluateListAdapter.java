package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckg.order.data.event.EvaluateListEvent;

/**
 * @describe: 评价菜品清单
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class EvaluateListAdapter extends RecyclerView.Adapter<EvaluateListAdapter.ListViewHolder> {

    private Context mContext;

    private List<SettleAccountsDishesItemBean> mList;

    private Map<Integer,TextView> mMap=new HashMap<>();

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
        if (!mMap.containsKey(holder.getLayoutPosition())) {
            mMap.put(holder.getLayoutPosition(), holder.title);
        }
        holder.title.setText(mList.get(position).getName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer index:mMap.keySet()) {
                    mMap.get(index).setTextColor(mContext.getResources().getColor(R.color.text_color_person));
                }
                mMap.get(holder.getLayoutPosition()).setTextColor(mContext.getResources().getColor(R.color.text_color_red));
                EventBus.getDefault().post(new EvaluateListEvent(EvaluateListEvent.ITEM,mList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        TextView title;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
