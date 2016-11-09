package cn.sczhckg.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.PriceTypeBean;
import cn.sczhckg.order.data.listener.OnShoppingCartListener;
import cn.sczhckg.order.data.listener.OnTotalNumberListener;
import cn.sczhckg.order.overwrite.SlantTextView;

/**
 * @describe: 购物车数据适配
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private List<DishesBean> mList;

    private Context mContext;

    private OnShoppingCartListener onShoppingCartListener;

    private OnTotalNumberListener onTotalNumberListener;


    public ShoppingCartAdapter(List<DishesBean> mList, Context mContext,OnShoppingCartListener onShoppingCartListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.onShoppingCartListener=onShoppingCartListener;
    }

    @Override
    public ShoppingCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingCartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_dishes, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingCartViewHolder holder, int position) {
        final DishesBean bean = mList.get(position);
        holder.cartName.setText(bean.getName());
        holder.cartNumber.setText("×" + bean.getNumber());
        if (bean.getPriceType()==null) {
            holder.cartPrice.setText("¥" + bean.getPrice());
        }else {
            holder.cartFavorablePrice.setText("¥" + bean.getPrice());
            countFavorable(bean.getPriceType(),holder.cartPrice,holder.cartFavorable);
        }
        bean.setTotalPrice(countPrice(bean.getNumber(),  holder.cartPrice));
        holder.cartTotalPrice.setText("¥" + bean.getTotalPrice());
        /**加菜*/
        holder.cartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=bean.getNumber();
                number++;
                bean.setNumber(number);
                notifyDataSetChanged();
                onShoppingCartListener.shoppingCart(bean);
                countTotal();
            }
        });
        /**减菜*/
        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=bean.getNumber();
                if (number>0){
                    number--;
                    bean.setNumber(number);
                    if (number==0){
                        mList.remove(bean);
                    }
                    notifyDataSetChanged();
                    onShoppingCartListener.shoppingCart(bean);
                    countTotal();
                }

            }
        });
        countTotal();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void notifyDataSetChanged(List<DishesBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * 计算锅底数量，菜品数量，总价
     */
    private void countTotal(){
        /**锅底数量*/
        int potNumber=0;
        /**菜品数量*/
        int dishesNumber=0;
        /**总价*/
        int totalPrice=0;
        for (int i = 0; i < mList.size(); i++) {
            DishesBean bean=mList.get(i);
            if (bean.getType()==0){
                potNumber=potNumber+bean.getNumber();
            }else {
                dishesNumber=dishesNumber+bean.getNumber();
            }
           totalPrice = totalPrice+bean.getTotalPrice();
        }
        onTotalNumberListener.totalNumber(totalPrice,potNumber,dishesNumber);
    }

    /**
     * 计算折扣
     */
    private void countFavorable(List<PriceTypeBean> priceTypeBeen,TextView textView,ImageView image) {
        int price = priceTypeBeen.get(0).getPrice();
        int type= priceTypeBeen.get(0).getType();
        for (int i = 0; i < priceTypeBeen.size(); i++) {
            if (priceTypeBeen.get(i).getPrice() < price) {
                price = priceTypeBeen.get(i).getPrice();
                type = priceTypeBeen.get(i).getType();
            }
        }
        textView.setText("¥"+price);
        if (type == 0){
            image.setImageResource(R.drawable.vip);
        }else if (type == 1){
            image.setImageResource(R.drawable.zhekou);
        }
    }

    /**
     * 计算总价
     *
     * @return
     */
    private int countPrice(int number, TextView text) {
        int price=Integer.parseInt(text.getText().toString().replace("¥",""));
        return number * price;
    }

    public void setOnTotalNumberListener(OnTotalNumberListener onTotalNumberListener) {
        this.onTotalNumberListener = onTotalNumberListener;
    }

    static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cart_favorable)
        ImageView cartFavorable;
        @Bind(R.id.cart_name)
        TextView cartName;
        @Bind(R.id.cart_number)
        TextView cartNumber;
        @Bind(R.id.cart_price)
        TextView cartPrice;
        @Bind(R.id.cart_total_price)
        TextView cartTotalPrice;
        @Bind(R.id.cart_add)
        ImageView cartAdd;
        @Bind(R.id.cart_minus)
        ImageView cartMinus;
        @Bind(R.id.cart_favorable_price)
        SlantTextView cartFavorablePrice;

        public ShoppingCartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
