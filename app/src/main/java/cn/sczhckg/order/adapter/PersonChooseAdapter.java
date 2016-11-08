package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.PersonBean;
import cn.sczhckg.order.fragment.PotTypeFagment;

/**
 * @describe:
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class PersonChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<PersonBean> mList = new ArrayList<>();

    public PersonChooseAdapter(Context mContext, List<PersonBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            PersonCommonViewHolder commonHolder = new PersonCommonViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_person_common, parent, false));
            return commonHolder;
        } else {
            PersonChooseViewHolder chooseHolder = new PersonChooseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_person_choose, parent, false));
            return chooseHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==0){
            ((PersonCommonViewHolder)holder).itemPerson.setText(mList.get(position).getNumber()+"");
        }else {
            if (mList.size()>PotTypeFagment.DEFAULT_PERSON+1){
                ((PersonChooseViewHolder)holder).table.setVisibility(View.VISIBLE);
                ((PersonChooseViewHolder)holder).table.setText(mList.get(position).getTableName());
            }
            ((PersonChooseViewHolder)holder).person.setText(mList.get(position).getNumber()+"");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < PotTypeFagment.DEFAULT_PERSON) {
            return 0;
        }
        return 1;
    }

    /**
     * 刷新数据
     *
     * @param mList
     */
    public void notifyDataSetChanged(List<PersonBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class PersonCommonViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_person)
        TextView itemPerson;

        public PersonCommonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class PersonChooseViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.person_table_text)
        TextView table;
        @Bind(R.id.person)
        TextView person;
        @Bind(R.id.person_minus)
        ImageView minus;
        @Bind(R.id.person_add)
        ImageView add;

        public PersonChooseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
