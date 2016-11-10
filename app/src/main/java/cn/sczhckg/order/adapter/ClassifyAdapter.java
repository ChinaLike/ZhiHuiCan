package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.ClassifyItemBean;

/**
 * @describe:
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ClassifyViewHolder> {

    private Context mContext;

    private List<ClassifyItemBean> mList;

    private LinearLayoutManager manager;


    public ClassifyAdapter(Context mContext, List<ClassifyItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ClassifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(ClassifyViewHolder holder, final int position) {
        final ClassifyItemBean bean = mList.get(position);
        holder.classifyText.setText(bean.getName());
        if (bean.isSelect()) {
            holder.classifyLine.setVisibility(View.VISIBLE);
            holder.classifyText.setTextColor(mContext.getResources().getColor(R.color.text_color_m));
            holder.classiftItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.classifyLine.setVisibility(View.INVISIBLE);
            holder.classifyText.setTextColor(mContext.getResources().getColor(R.color.text_color_person));
            holder.classiftItem.setBackgroundColor(mContext.getResources().getColor(R.color.background_gray_m));
        }

        holder.classiftItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upData(bean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ClassifyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.classify_text)
        TextView classifyText;
        @Bind(R.id.classify_line)
        View classifyLine;
        @Bind(R.id.classift_item)
        LinearLayout classiftItem;

        public ClassifyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void setManager(LinearLayoutManager manager) {
        this.manager = manager;
    }

    private void upData(ClassifyItemBean bean) {
        int postion = mList.indexOf(bean);
        for (ClassifyItemBean item : mList) {
            if (item.isSelect()){
            item.setSelect(false);
        }
        bean.setSelect(true);
        notifyDataSetChanged();

    }

}
