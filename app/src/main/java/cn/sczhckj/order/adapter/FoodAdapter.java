package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.impl.FavorImpl;
import cn.sczhckj.order.mode.impl.FoodControlImpl;
import cn.sczhckj.order.mode.impl.TagCloudImpl;

/**
 * @describe: 菜品适配
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.DishesHolder> {

    private Context mContext;

    private List<FoodBean> mList;
    /**
     * 标签云数据适配
     */
    private TagCloudImpl mTagCloudImpl;

    /**
     * 本分类最大数量
     */
    private int maximum = 0;
    /**
     * 本分类是否必选
     */
    private int required = 0;
    /**
     * 分类权限，默认可以选择
     */
    private int catePermiss = Constant.PERMISS_AGREE;

    /**
     * 点赞实现
     */
    private FavorImpl mFavorImpl;
    /**
     * 菜品数量控制实现
     */
    private FoodControlImpl mFoodControl;


    public FoodAdapter(Context mContext, List<FoodBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mTagCloudImpl = new TagCloudImpl(mContext);
        mFavorImpl = new FavorImpl(mContext);
        mFoodControl = new FoodControlImpl(mContext);
    }

    @Override
    public DishesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DishesHolder(LayoutInflater.from(mContext).inflate(R.layout.item_dishes, parent, false));
    }

    /**
     * 刷新数据
     *
     * @param mList
     */
    public void notifyDataSetChanged(List<FoodBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final DishesHolder holder, int position) {
        final FoodBean bean = mList.get(position);
        /**菜品缩略图*/
        GlideLoading.loadingDishes(mContext, bean.getImageUrl(), holder.dishesImage);
        /**菜品名字*/
        holder.dishesName.setText(bean.getName());
        /**菜品价格*/
        holder.dishesPrice.setText(bean.getOriginPrice() + "");
        /**菜品销量*/
        holder.dishesSales.setText(bean.getSales() + "");
        /**菜品点赞数*/
        holder.dishesCollect.setText(bean.getFavors() + "");
        /**默认菜品*/
        holder.dishesNumber.setText(bean.getCount() + "");
        /**菜品减少*/
        mFoodControl.minusFood(holder.dishesMinus, bean);
        /**菜品添加*/
        mFoodControl.addFood(holder.dishesAdd, holder.dishesNumber, bean, mList);
        /**点击菜品图片进入详情*/
        holder.dishesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.DISHES_DETAILS_IN,bean,mList));
            }
        });

        /**判断优惠类型,并显示*/
        mTagCloudImpl.setPrice(holder.dishesFavorableRecyclerView, bean.getPrices());

        /**判断是否喜欢，即收藏与否*/
        if (bean.isFavor()) {
            holder.dishesCollectIcon.setSelected(true);
            holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext, R.color.favor_sel));
        } else {
            holder.dishesCollectIcon.setSelected(false);
            holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext, R.color.favor_nor));
        }
        /**收藏标识点击事件*/
        holder.favorParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavorImpl.favor(holder.dishesCollectIcon, holder.dishesCollect, bean);
            }
        });

        /**权限判定,该桌是否可以点餐*/
        if (catePermiss == Constant.PERMISS_AGREE) {
            /**首先判断分类是否可以点菜，可以点菜*/
            if (bean.getPermiss() == Constant.PERMISS_AGREE) {
                /**可以点菜*/
                holder.dishesAdd.setClickable(true);
                holder.dishesMinus.setClickable(true);
            } else {
                /**不可以点菜*/
                mFoodControl.buttonClick(holder.dishesAdd);
                mFoodControl.buttonClick(holder.dishesMinus);
            }
        } else if (catePermiss == Constant.PERMISS_DISAGREE) {
            /**该分类都不可以点菜*/
            mFoodControl.buttonClick(holder.dishesAdd);
            mFoodControl.buttonClick(holder.dishesMinus);
        }


    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }


    /**
     * 设置最大数量
     */
    public void setMaximum(int maximum) {
        this.maximum = maximum;
        mFoodControl.setMaximum(maximum);
    }

    /**
     * 设置是否必选
     */
    public void setRequired(int required) {
        this.required = required;
        mFoodControl.setRequired(required);
    }

    /**
     * 设置分类权限
     *
     * @param catePermiss
     */
    public void setCatePermiss(int catePermiss) {
        this.catePermiss = catePermiss;
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
        @Bind(R.id.dishes_minus)
        ImageView dishesMinus;
        @Bind(R.id.dishes_number)
        TextView dishesNumber;
        @Bind(R.id.dishes_add)
        ImageView dishesAdd;
        @Bind(R.id.dishes_favorable_recyclerView)
        RecyclerView dishesFavorableRecyclerView;
        @Bind(R.id.dishes_parent)
        LinearLayout parent;
        @Bind(R.id.dishes_favor_parent)
        LinearLayout favorParent;


        public DishesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
