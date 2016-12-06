package cn.sczhckj.order.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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
import cn.sczhckj.order.data.bean.DishesBean;
import cn.sczhckj.order.data.event.BottomChooseEvent;
import cn.sczhckj.order.data.event.RefreshCartEvent;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.fragment.OrderFragment;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.overwrite.MyDialog;

/**
 * @describe: 菜品适配
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishesHolder> {

    private Context mContext;

    private List<DishesBean> mList;

    private MyDialog dialog;


    public DishesAdapter(Context mContext, List<DishesBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        dialog=new MyDialog(mContext);
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
    public void notifyDataSetChanged(List<DishesBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final DishesHolder holder, int position) {
        final DishesBean bean = mList.get(position);
        GlideLoading.loadingDishes(mContext, bean.getUrl(), holder.dishesImage);
        holder.dishesName.setText(bean.getName());
        holder.dishesPrice.setText("¥  " + bean.getPrice() + "");
        holder.dishesSales.setText(bean.getSales() + "");
        holder.dishesCollect.setText(bean.getCollect() + "");
        if (bean.getStockout() == 0) {
            holder.dishesStockout.setVisibility(View.INVISIBLE);
            holder.dishesMinus.setClickable(false);
            holder.dishesAdd.setClickable(false);
        } else {
            holder.dishesStockout.setVisibility(View.INVISIBLE);
            holder.dishesMinus.setClickable(true);
            holder.dishesAdd.setClickable(true);
        }
        holder.dishesNumber.setText(bean.getNumber() + "");
        /**菜品减少*/
        holder.dishesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getPermiss() == Constant.PREMISS_AGREE) {
                    int number = bean.getNumber();
                    if (number > 0) {
                        number--;
                        bean.setNumber(number);
                        holder.dishesNumber.setText(number + "");
                        bean.setTableId(OrderFragment.tabOrderType);
                        EventBus.getDefault().post(new RefreshCartEvent(bean));
                    }
                }
            }
        });
        /**菜品添加*/
        holder.dishesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOverProof(bean,holder);
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
        if (bean.getPriceType()!=null&&bean.getPriceType().size()!=0){
            holder.dishesFavorableRecyclerView.setVisibility(View.VISIBLE);
            DetailsAdapter adapter=new DetailsAdapter(mContext,bean.getPriceType());
            holder.dishesFavorableRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
            holder.dishesFavorableRecyclerView.setAdapter(adapter);
        }else {
            holder.dishesFavorableRecyclerView.setVisibility(View.GONE);
        }

        /**判断是否喜欢，即收藏与否*/
        if (bean.isCollect()){
            holder.dishesCollectIcon.setSelected(true);
        }else {
            holder.dishesCollectIcon.setSelected(false);
        }
        /**收藏标识点击事件*/
        holder.dishesCollectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dishesCollectIcon.isSelected()){
                    holder.dishesCollectIcon.setSelected(false);
                    bean.setCollect(false);
                    holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_gray));
                }else {
                    holder.dishesCollect.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_main_select));
                    holder.dishesCollectIcon.setSelected(true);
                    bean.setCollect(true);
                }
            }
        });

        /**权限判定,该桌是否可以点餐*/
        if (bean.getPermiss()==Constant.PREMISS_AGREE){
            /**可以点菜*/
            holder.dishesAdd.setClickable(true);
            holder.dishesMinus.setClickable(true);
        }else {
            /**不可以点菜*/
            holder.dishesAdd.setClickable(false);
            holder.dishesMinus.setClickable(false);
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
        @Bind(R.id.dishes_parent)
        LinearLayout parent;


        public DishesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 判断锅底是否超过标准
     */
    private void isOverProof(final DishesBean bean, final DishesHolder holder){
        /**首先判断是否是锅底再次判断是否已经选择了一个锅底*/
        if(bean.getType()==0&& BaseFragment.totalPotNumber>0){
            dialog.setTitle("温馨提示");
            dialog.setContextText("尊敬的顾客您好！\n您已经选择了一份锅底，确认还\n需要再来一份吗？");
            dialog.setPositiveButton("确认选择", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAddDishes(bean,holder);
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("不选了", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else {
            setAddDishes(bean,holder);
        }
    }

    /**
     * 菜品添加
     * @param bean
     * @param holder
     */
    private void setAddDishes(DishesBean bean,DishesHolder holder){
        if (bean.getPermiss() == Constant.PREMISS_AGREE) {
            int number = bean.getNumber();
            number++;
            bean.setNumber(number);
            holder.dishesNumber.setText(number + "");
            bean.setTableId(OrderFragment.tabOrderType);
            EventBus.getDefault().post(new RefreshCartEvent(bean));
        }
    }

}
