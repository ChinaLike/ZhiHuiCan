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
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.data.bean.PriceTypeBean;
import cn.sczhckj.order.data.listener.OnTotalNumberListener;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.overwrite.RoundImageView;

/**
 * @describe: 购物车数据适配
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ShoppingCartViewHolder> {

    private List<FoodBean> mList;

    private Context mContext;

    private OnTotalNumberListener onTotalNumberListener;

    public CartAdapter(List<FoodBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ShoppingCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShoppingCartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_dishes, parent, false));
    }

    @Override
    public void onBindViewHolder(ShoppingCartViewHolder holder, int position) {
        final FoodBean bean = mList.get(position);
        /**设置菜品名字*/
        holder.cartDishesName.setText(bean.getName());
        /**设置菜品数量*/
        holder.cartDishesNumber.setText("×" + bean.getCount());
        /**设置菜品价格*/
        holder.cartDishesPrice.setText("" + bean.getPrice());
        /**设置价格，以及优惠图标*/
        holder.cartDishesTotalPrice.setText(bean.getCount() * bean.getPrice() + "");
        setPrice(holder.cartFavorableIamge, holder.cartDishesPrice, holder.cartDishesTotalPrice, bean);
        /**设置已完成数量*/
        setFinishFood(holder.cartDishesFlag, holder.cartDishesRefund,bean);
//        if (bean.getPrices() == null || bean.getPrices().size() == 0) {
//            holder.cartPrice.setText("¥" + bean.getPrice());
//        } else {
//            holder.cartFavorablePrice.setText("¥" + bean.getPrice());
//            countFavorable(bean.getPriceType(), holder.cartPrice, holder.cartFavorable);
//        }
//        bean.setTotalPrice(countPrice(bean.getCount(), holder.cartPrice));
//        holder.cartTotalPrice.setText("¥" + bean.getTotalPrice());
//        /**加菜*/
//        holder.cartAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**首先判断权限*/
//                if (bean.getPermiss() == Constant.PREMISS_AGREE) {
//                    int number = bean.getCount();
//                    number++;
//                    bean.setCount(number);
//                    notifyDataSetChanged();
//                    EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 0, bean));
//                    countTotal();
//                }
//            }
//        });
//        /**减菜*/
//        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**首先判断权限*/
//                if (bean.getPermiss() == Constant.PREMISS_AGREE) {
//                    int number = bean.getCount();
//                    if (number > 0) {
//                        number--;
//                        bean.setCount(number);
//                        if (number == 0) {
//                            mList.remove(bean);
//                        }
//                        notifyDataSetChanged();
//                        EventBus.getDefault().post(new CartNumberEvent(Constant.CART_NUMBER_EVENT, 1, bean));
//                        countTotal();
//                    }
//                }
//            }
//        });
//        countTotal();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void notifyDataSetChanged(List<FoodBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * 设置小计价格，优惠图标，单价
     *
     * @param imageView  优惠图标
     * @param price      单价
     * @param totalPrice 小计
     * @param bean       参数
     */
    private void setPrice(ImageView imageView, TextView price, TextView totalPrice, FoodBean bean) {
        if (bean.getPrices() != null && bean.getPrices().size() != 0) {
            for (PriceBean item : bean.getPrices()) {
                if (item.getActive() == Constant.PRICE_ACTIVE) {
                    price.setText(item.getPrice() + "");
                    totalPrice.setText(bean.getCount() * item.getPrice() + "");
                    GlideLoading.loadingDishes(mContext, item.getImageUrl(), imageView);
                }
            }
        }
    }

    /**
     * 设置已经完成菜品显示
     *
     * @param layout
     * @param bean
     */
    private void setFinishFood(LinearLayout layout, ImageView retIm,FoodBean bean) {
        layout.removeAllViews();
        List<ImageView> mList=new ArrayList<>();
        for (int i = 0; i < bean.getCount(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.selector_cart_food_finish);
            imageView.setPadding(0,0,5,0);
            layout.addView(imageView);
            mList.add(imageView);
        }
        if (bean.getFinishCount() != null && bean.getFinishCount() != 0) {
            for (int i = 0; i < bean.getFinishCount(); i++) {
                mList.get(i).setSelected(true);
            }
        }
        /**设置可退图标*/
        if (bean.getCount()==bean.getFinishCount()){
            retIm.setImageResource(R.drawable.order_btn_foodback_dis);
        }else {
            retIm.setImageResource(R.drawable.order_btn_foodback_nor);
        }
    }

//    /**
//     * 计算锅底数量，菜品数量，总价
//     */
//    public void countTotal() {
//        /**锅底数量*/
//        int potNumber = 0;
//        /**菜品数量*/
//        int dishesNumber = 0;
//        /**总价*/
//        int totalPrice = 0;
//        for (int i = 0; i < mList.size(); i++) {
//            FoodBean bean = mList.get(i);
//            if (bean.getType() == 0) {
//                potNumber = potNumber + bean.getCount();
//            } else {
//                dishesNumber = dishesNumber + bean.getCount();
//            }
//            if (bean.getPriceType() != null && bean.getPriceType().size() != 0) {
//                totalPrice = totalPrice + bean.getCount() * getFavortablePrice(bean.getPriceType());
//            } else {
//                totalPrice = totalPrice + bean.getCount() * bean.getPrice();
//            }
//        }
//        onTotalNumberListener.totalNumber(totalPrice, potNumber, dishesNumber);
//    }

    /**
     * 计算折扣
     */
    private void countFavorable(List<PriceTypeBean> priceTypeBeen, TextView textView, ImageView image) {
        int price = getFavortablePrice(priceTypeBeen);
        int type = getFavorableType(priceTypeBeen);
        textView.setText("¥" + price);
        for (int i = 0; i < priceTypeBeen.size(); i++) {
            if (priceTypeBeen.get(i).getType() == type) {
                GlideLoading.loadingDishes(mContext, priceTypeBeen.get(i).getImageUrl(), image);
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

        @Bind(R.id.cart_favorable_iamge)
        RoundImageView cartFavorableIamge;
        @Bind(R.id.cart_dishes_name)
        TextView cartDishesName;
        @Bind(R.id.cart_dishes_number)
        TextView cartDishesNumber;
        @Bind(R.id.cart_dishes_refund)
        ImageView cartDishesRefund;
        @Bind(R.id.cart_dishes_price)
        TextView cartDishesPrice;
        @Bind(R.id.cart_dishes_total_price)
        TextView cartDishesTotalPrice;
        @Bind(R.id.cart_dishes_flag)
        LinearLayout cartDishesFlag;

        public ShoppingCartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
