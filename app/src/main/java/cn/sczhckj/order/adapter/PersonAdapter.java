package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.fragment.RequiredFagment;

/**
 * @describe: 锅底必选和推荐菜品界面人数适配
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<Integer> mList = new ArrayList<>();

    private OnTableListenner onTableListenner;

    public PersonAdapter(Context mContext, List<Integer> mList) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            ((PersonCommonViewHolder) holder).itemPerson.setText(mList.get(position) + "");
            /**设置点击默认人数*/
            ((PersonCommonViewHolder) holder).parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(mList.size()-1);
                    mList.add(mList.get(position));
                    onTableListenner.person(mList.get(position));
                    notifyDataSetChanged();
                }
            });
        } else if(getItemViewType(position) == 1){
            ((PersonChooseViewHolder) holder).person.setText(mList.get(position)+ "");
            /**人数减*/
            ((PersonChooseViewHolder) holder).minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(((PersonChooseViewHolder) holder).person.getText().toString());
                    if (number > 1) {
                        number--;
                        ((PersonChooseViewHolder) holder).person.setText(number + "");
                        onTableListenner.person(number);
                    }
                }
            });
            /**人数加*/
            ((PersonChooseViewHolder) holder).add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(((PersonChooseViewHolder) holder).person.getText().toString());
                    number++;
                    ((PersonChooseViewHolder) holder).person.setText(number + "");
                    onTableListenner.person(number);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < RequiredFagment.DEFAULT_PERSON) {
            return 0;
        }
        return 1;
    }

    public void setOnTableListenner(OnTableListenner onTableListenner) {
        this.onTableListenner = onTableListenner;
    }

    /**
     * 刷新数据
     *
     * @param mList
     */
    public void notifyDataSetChanged(List<Integer> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class PersonCommonViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_person)
        TextView itemPerson;
        @Bind(R.id.item_default_person_parent)
        LinearLayout parent;

        public PersonCommonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class PersonChooseViewHolder extends RecyclerView.ViewHolder {

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
