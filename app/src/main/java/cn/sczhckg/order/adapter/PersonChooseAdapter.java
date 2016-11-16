package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.data.bean.PersonBean;
import cn.sczhckg.order.fragment.PotTypeFagment;

/**
 * @describe: 锅底必选和推荐菜品界面人数适配
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            ((PersonCommonViewHolder) holder).itemPerson.setText(mList.get(position).getNumber() + "");
            /**设置点击默认人数*/
            ((PersonCommonViewHolder) holder).default_person_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.get(PotTypeFagment.DEFAULT_PERSON).setNumber(mList.get(position).getNumber());
                    notifyDataSetChanged();
                }
            });
        } else {
            if (mList.size() > PotTypeFagment.DEFAULT_PERSON + 1) {
                ((PersonChooseViewHolder) holder).table.setVisibility(View.VISIBLE);
                ((PersonChooseViewHolder) holder).table.setText(mList.get(position).getTableName());
            }
            ((PersonChooseViewHolder) holder).person.setText(mList.get(position).getNumber() + "");
            /**人数减*/
            ((PersonChooseViewHolder) holder).minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(((PersonChooseViewHolder) holder).person.getText().toString());
                    if (number > 1) {
                        number--;
                        ((PersonChooseViewHolder) holder).person.setText(number + "");
                        MainActivity.person=number;
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
                    MainActivity.person=number;
                }
            });
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
        @Bind(R.id.item_default_person_parent)
        LinearLayout default_person_parent;

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
