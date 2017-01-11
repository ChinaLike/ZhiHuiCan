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
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.impl.FoodControlImpl;
import cn.sczhckj.order.overwrite.RoundImageView;
import cn.sczhckj.order.until.show.L;

/**
 * @describe: 购物车数据适配
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ShoppingCartViewHolder> {

    private List<FoodBean> mList;

    private Context mContext;
    /**
     * 已下单
     */
    public static final int ORDER_TYPE = 0;
    /**
     * 未下单
     */
    public static final int DIS_ORDER_TYPE = 1;

    /**
     * 显示类型，购物车不需要显示菜品进度，已下单需要显示
     */
    private int type = DIS_ORDER_TYPE;
    /**
     * 数量控制实现
     */
    private FoodControlImpl mFoodControl;


    public CartAdapter(List<FoodBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mFoodControl = new FoodControlImpl(mContext);
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
        if (type == ORDER_TYPE) {
            setFinishFood(holder.cartDishesFlag, holder.cartDishesRefund, bean);
        } else if (type == DIS_ORDER_TYPE) {
            holder.cartDishesRefund.setImageResource(R.drawable.open_btn_dishes_reduce);
            mFoodControl.minusFood(holder.cartDishesRefund, bean, RefreshFoodEvent.FROM_CART);
        }
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
        price.setText(bean.getPrice() + "");
        totalPrice.setText(bean.getCount() * bean.getPrice() + "");
        if (bean.getPriceImageUrl()!=null) {
            imageView.setVisibility(View.VISIBLE);
            GlideLoading.loadingDishes(mContext, bean.getPriceImageUrl(), imageView);
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置已经完成菜品显示
     *
     * @param layout
     * @param bean
     */
    private void setFinishFood(LinearLayout layout, ImageView retIm, FoodBean bean) {
        layout.removeAllViews();
        List<ImageView> mList = new ArrayList<>();
        for (int i = 0; i < bean.getCount(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.selector_cart_food_finish);
            imageView.setPadding(0, 0, 5, 0);
            layout.addView(imageView);
            mList.add(imageView);
        }
        if (bean.getFinishCount() != null && bean.getFinishCount() != 0) {
            for (int i = 0; i < bean.getFinishCount(); i++) {
                mList.get(i).setSelected(true);
            }
        }
        /**设置可退图标*/
        if (bean.getCount() == bean.getFinishCount()) {
            retIm.setImageResource(R.drawable.order_btn_foodback_dis);
            retIm.setClickable(false);
        } else {
            retIm.setClickable(true);
            retIm.setImageResource(R.drawable.order_btn_foodback_nor);
            mFoodControl.refund(retIm, bean);
        }
    }

    /**
     * 设置显示类型
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
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
