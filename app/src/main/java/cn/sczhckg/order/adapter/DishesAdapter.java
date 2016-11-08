package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 菜品适配
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishesHolder> {

    private Context mContext;

    private List<DishesBean> mList;

    public DishesAdapter(Context mContext, List<DishesBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public DishesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DishesHolder(LayoutInflater.from(mContext).inflate(R.layout.item_dishes, parent, false));
    }

    /**
     * 刷新数据
     * @param mList
     */
    public void notifyDataSetChanged(List<DishesBean> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final DishesHolder holder, int position) {
        final DishesBean bean = mList.get(position);
        Picasso.with(mContext).load(bean.getUrl()).into(holder.dishesImage);
        holder.dishesName.setText(bean.getName());
        holder.dishesPrice.setText("¥  " + bean.getPrice() + "");
        holder.dishesSales.setText(bean.getSales() + "");
        holder.dishesCollect.setText(bean.getCollect() + "");
        if (bean.getStockout() == 0) {
            holder.dishesStockout.setVisibility(View.VISIBLE);
            holder.dishesMinus.setClickable(false);
            holder.dishesAdd.setClickable(false);
        } else {
            holder.dishesStockout.setVisibility(View.INVISIBLE);
            holder.dishesMinus.setClickable(true);
            holder.dishesAdd.setClickable(true);
        }
        holder.dishesNumber.setText(bean.getNumber() + "");
        holder.dishesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = bean.getNumber();
                if (number > 0) {
                    number--;
                    bean.setNumber(number);
                    holder.dishesNumber.setText(number + "");
                }
            }
        });
        holder.dishesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = bean.getNumber();
                number++;
                bean.setNumber(number);
                holder.dishesNumber.setText(number + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class DishesHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.dishes_image)
        ImageView dishesImage;
        @Bind(R.id.dishes_name)
        TextView dishesName;
        @Bind(R.id.dishes_price)
        TextView dishesPrice;
        @Bind(R.id.dishes_sales)
        TextView dishesSales;
        @Bind(R.id.dishes_collect_icon)
        ImageView dishesCollectIcon;
        @Bind(R.id.dishes_collect)
        TextView dishesCollect;
        @Bind(R.id.dishes_stockout)
        ImageView dishesStockout;
        @Bind(R.id.dishes_minus)
        ImageView dishesMinus;
        @Bind(R.id.dishes_number)
        TextView dishesNumber;
        @Bind(R.id.dishes_add)
        ImageView dishesAdd;
        @Bind(R.id.dishes_favorable_recyclerView)
        RecyclerView dishesFavorableRecyclerView;

        public DishesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
