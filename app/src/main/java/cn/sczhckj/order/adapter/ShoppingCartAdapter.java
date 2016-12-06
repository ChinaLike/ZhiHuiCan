package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.DishesBean;
import cn.sczhckj.order.data.bean.PriceTypeBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.listener.OnTotalNumberListener;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.overwrite.SlantTextView;

/**
 * @describe: 购物车数据适配
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private List<DishesBean> mList;

    private Context mContext;

    private OnTotalNumberListener onTotalNumberListener;

    public ShoppingCartAdapter(List<DishesBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
        /**设置优惠之前的价格，以及优惠图标*/
        if (bean.getPriceType() == null || bean.getPriceType().size() == 0) {
            holder.cartPrice.setText("¥" + bean.getPrice());
        } else {
            holder.cartFavorablePrice.setText("¥" + bean.getPrice());
            countFavorable(bean.getPriceType(), holder.cartPrice, holder.cartFavorable);
        }
        bean.setTotalPrice(countPrice(bean.getNumber(), holder.cartPrice));
        holder.cartTotalPrice.setText("¥" + bean.getTotalPrice());
        /**加菜*/
        holder.cartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**首先判断权限*/
                if (bean.getPermiss() == Constant.PREMISS_AGREE) {
                    int number = bean.getNumber();
                    number++;
                    bean.setNumber(number);
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 0, bean));
                    countTotal();
                }
            }
        });
        /**减菜*/
        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**首先判断权限*/
                if (bean.getPermiss() == Constant.PREMISS_AGREE) {
                    int number = bean.getNumber();
                    if (number > 0) {
                        number--;
                        bean.setNumber(number);
                        if (number == 0) {
                            mList.remove(bean);
                        }
                        notifyDataSetChanged();
                        EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 1, bean));
                        countTotal();
                    }
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
    public void countTotal() {
        /**锅底数量*/
        int potNumber = 0;
        /**菜品数量*/
        int dishesNumber = 0;
        /**总价*/
        int totalPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            DishesBean bean = mList.get(i);
            if (bean.getType() == 0) {
                potNumber = potNumber + bean.getNumber();
            } else {
                dishesNumber = dishesNumber + bean.getNumber();
            }
            if (bean.getPriceType() != null && bean.getPriceType().size() != 0) {
                totalPrice = totalPrice + bean.getNumber() * getFavortablePrice(bean.getPriceType());
            } else {
                totalPrice = totalPrice + bean.getNumber() * bean.getPrice();
            }
        }
        onTotalNumberListener.totalNumber(totalPrice, potNumber, dishesNumber);
    }

    /**
     * 计算折扣
     */
    private void countFavorable(List<PriceTypeBean> priceTypeBeen, TextView textView, ImageView image) {
        int price = getFavortablePrice(priceTypeBeen);
        int type = getFavorableType(priceTypeBeen);
        textView.setText("¥" + price);
        for (int i = 0; i < priceTypeBeen.size(); i++) {
            if (priceTypeBeen.get(i).getType() == type) {
                GlideLoading.loadingDishes(mContext, priceTypeBeen.get(i).getUrl(), image);
            }
        }
    }

    /**
     * 获取优惠的ID
     *
     * @param priceTypeBeen
     * @return
     */
    private int getFavorableType(List<PriceTypeBean> priceTypeBeen) {
        int price = priceTypeBeen.get(0).getPrice();
        int type = priceTypeBeen.get(0).getType();
        for (int i = 0; i < priceTypeBeen.size(); i++) {
            if (priceTypeBeen.get(i).getPrice() < price) {
                price = priceTypeBeen.get(i).getPrice();
                type = priceTypeBeen.get(i).getType();
            }
        }

        return type;
    }

    /**
     * 获取优惠价格
     *
     * @param priceTypeBeen
     * @return
     */
    private int getFavortablePrice(List<PriceTypeBean> priceTypeBeen) {
        int price = priceTypeBeen.get(0).getPrice();
        for (int i = 0; i < priceTypeBeen.size(); i++) {
            if (priceTypeBeen.get(i).getPrice() < price) {
                price = priceTypeBeen.get(i).getPrice();
            }
        }
        return price;
    }

    /**
     * 计算总价
     *
     * @return
     */
    private int countPrice(int number, TextView text) {
        int price = Integer.parseInt(text.getText().toString().replace("¥", ""));
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
