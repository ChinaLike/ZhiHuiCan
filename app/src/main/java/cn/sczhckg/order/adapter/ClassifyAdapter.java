package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.ClassifyItemBean;

/**
 * @describe: 一级标题分类
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ClassifyViewHolder> {

    private Context mContext;

    private List<ClassifyItemBean> mList;

    private OnItemClickListener mOnItemClickListener;


    private Map<Integer, View> viewMap = new HashMap<>();
    private Map<Integer, TextView> textMap = new HashMap<>();
    private Map<Integer, LinearLayout> layoutMap = new HashMap<>();

    private int current = 0;

    public ClassifyAdapter(Context mContext, List<ClassifyItemBean> mList, int current) {
        this.mContext = mContext;
        this.mList = mList;
        this.current = current;
    }

    @Override
    public ClassifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(final ClassifyViewHolder holder, final int position) {
        final ClassifyItemBean bean = mList.get(position);
        holder.classifyText.setText(bean.getName());
        viewMap.put(position, holder.classifyLine);
        textMap.put(position, holder.classifyText);
        layoutMap.put(position, holder.classiftItem);

        if (position == current) {
            holder.classifyLine.setSelected(true);
            holder.classifyText.setSelected(true);
            holder.classiftItem.setSelected(true);
        }
        holder.classiftItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                upData(n);
                mOnItemClickListener.onItemClick(v, n);
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

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void upData(int n) {
        viewMap.get(current).setSelected(false);
        textMap.get(current).setSelected(false);
        layoutMap.get(current).setSelected(false);
        viewMap.get(n).setSelected(true);
        textMap.get(n).setSelected(true);
        layoutMap.get(n).setSelected(true);
        current = n;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
