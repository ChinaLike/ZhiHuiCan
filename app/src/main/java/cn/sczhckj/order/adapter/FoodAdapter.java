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
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.event.BottomChooseEvent;
import cn.sczhckj.order.data.event.RefreshCartEvent;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.mode.impl.DialogImpl;
import cn.sczhckj.order.mode.impl.TagCloudImpl;
import cn.sczhckj.order.overwrite.TagCloudLayout;

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

    private DialogImpl dialog;


    public FoodAdapter(Context mContext, List<FoodBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mTagCloudImpl = new TagCloudImpl(mContext);
        dialog = new DialogImpl(mContext);
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
        holder.dishesPrice.setText(bean.getPrice() + "");
        /**菜品销量*/
        holder.dishesSales.setText(bean.getSales() + "");
        /**菜品点赞数*/
        holder.dishesCollect.setText(bean.getFavors() + "");
        /**默认菜品*/
        holder.dishesNumber.setText(bean.getNumber() + "");
        /**菜品减少*/
        holder.dishesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = bean.getNumber();
                if (number > 0) {
                    number--;
                    bean.setNumber(number);
                    holder.dishesNumber.setText(number + "");
                    EventBus.getDefault().post(new RefreshCartEvent(bean));
                }
            }
        });
        /**菜品添加*/
        holder.dishesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOverProof(bean, holder);
            }
        });
        /**点击菜品图片进入详情*/
        holder.dishesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BottomChooseEvent(Constant.DISHES_DETAILS_IN, bean));
            }
        });

        /**判断优惠类型,并显示*/
        mTagCloudImpl.setPrice(holder.dishesFavorableRecyclerView, bean.getPrices());
        // TODO: 2016/12/14 显示原价还是优惠价？
//        mTagCloudImpl.getPrice(holder.dishesPrice, bean.getPrices());
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
                int favorNum = Integer.parseInt(holder.dishesCollect.getText().toString());
                if (holder.dishesCollectIcon.isSelected()) {
                    holder.dishesCollect.setText(favorNum - 1 + "");
                    holder.dishesCollectIcon.setSelected(false);
                    bean.setFavor(false);
                    holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext, R.color.favor_nor));
                } else {
                    holder.dishesCollect.setText(favorNum + 1 + "");
                    holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext, R.color.favor_sel));
                    holder.dishesCollectIcon.setSelected(true);
                    bean.setFavor(true);
                }
            }
        });

        /**权限判定,该桌是否可以点餐*/
        if (bean.getPermiss() == Constant.PREMISS_AGREE) {
            /**可以点菜*/
            holder.dishesAdd.setClickable(true);
            holder.dishesMinus.setClickable(true);
        } else {
            /**不可以点菜*/
            buttonClick(holder.dishesAdd);
            buttonClick(holder.dishesMinus);
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
        TagCloudLayout dishesFavorableRecyclerView;
        @Bind(R.id.dishes_parent)
        LinearLayout parent;
        @Bind(R.id.dishes_favor_parent)
        LinearLayout favorParent;


        public DishesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 判断锅底是否超过标准
     */
    private void isOverProof(final FoodBean bean, final DishesHolder holder) {
        /**首先判断是否是锅底再次判断是否已经选择规定锅底*/
        if (bean.getMaximum() == null || bean.getMaximum() == Constant.FOOD_DISASTRICT) {
            /**不限制数量*/
            setAddDishes(bean, holder);
        } else {
            if (bean.getNumber() >= bean.getMaximum()) {
                /**限制数量*/
                dialog.aloneDialog(mContext.getResources().getString(R.string.dialog_title),
                        mContext.getResources().getString(R.string.dialog_context),
                        mContext.getResources().getString(R.string.dialog_cacel)).show();
            } else {
                setAddDishes(bean, holder);
            }
        }
    }

    /**
     * 菜品添加
     *
     * @param bean
     * @param holder
     */
    private void setAddDishes(FoodBean bean, DishesHolder holder) {
        int number = bean.getNumber();
        number++;
        bean.setNumber(number);
        holder.dishesNumber.setText(number + "");
        EventBus.getDefault().post(new RefreshCartEvent(bean));
    }

    /**
     * 加菜按钮，减菜按钮
     *
     * @param button
     */
    private void buttonClick(final ImageView button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                dialog.aloneDialog(mContext.getResources().getString(R.string.dialog_title),
                        mContext.getResources().getString(R.string.dialog_context1),
                        mContext.getResources().getString(R.string.dialog_cacel)).show();
            }
        });
    }

}
