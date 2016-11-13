package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;

/**
 * @describe: 团购券适配器
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class GrouponAdapter extends RecyclerView.Adapter<GrouponAdapter.GrouponViewHolder> {

    private Context mContext;

    private List<String> mList;

    public GrouponAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public GrouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrouponViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_groupon, parent, false));
    }

    @Override
    public void onBindViewHolder(GrouponViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    static class GrouponViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.groupon_number)
        TextView grouponNumber;
        @Bind(R.id.group_input)
        EditText groupInput;
        @Bind(R.id.groupon_close)
        ImageView grouponClose;
        @Bind(R.id.item_groupon_parent)
        RelativeLayout itemGrouponParent;

        public GrouponViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
