package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.event.RefreshFoodEvent;

/**
 * @ describe:  菜品数量控制实现
 * @ author: Like on 2016/12/20.
 * @ email: 572919350@qq.com
 */

public class FoodControlImpl {

    private Context mContext;
    /**
     * 弹窗
     */
    private DialogImpl dialog;

    /**
     * 本分类最大数量
     */
    private static int maximum = 0;
    /**
     * 本分类是否必选
     */
    private static int required = 0;

    public FoodControlImpl(Context mContext) {
        this.mContext = mContext;
        dialog = new DialogImpl(mContext);
    }

    /**
     * 添加菜品
     *
     * @param addImg    加菜按钮
     * @param countText 数量显示文本
     * @param bean      菜品数据
     * @param mList     该分类所有菜目
     */
    public void addFood(ImageView addImg, final TextView countText, final FoodBean bean, final List<FoodBean> mList) {
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyMaximum(mList)) {
                    /**验证总数是否超标*/
                    maxDialog();
                } else {
                    /**验证单个数量是否超标*/
                    isOverProof(bean, countText);
                }
            }
        });
    }

    /**
     * 减少菜品
     *
     * @param minusImg  减菜按钮
     * @param countText 数量显示文本
     * @param bean      菜品数据
     */
    public void minusFood(ImageView minusImg, final TextView countText, final FoodBean bean) {
        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**总数量*/
                int number = bean.getCount();
                /**已下单数量*/
                int orderCount = bean.getOrderCount()!=null?bean.getOrderCount():0;
                /**未下单数量*/
                int disOrder = bean.getDisOrderCount()!=null?bean.getDisOrderCount():0;
                if (number > 0) {
                    if (disOrder > 0) {
                        /**可以直接减菜*/
                        number--;
                        disOrder--;
                        bean.setDisOrderCount(disOrder);
                        bean.setCount(number);
                        countText.setText(number + "");
                        EventBus.getDefault().post(new RefreshFoodEvent(bean));
                    } else {
                        /**退菜*/
                        // TODO: 2016/12/20 做退菜处理
                    }


                }
            }
        });
    }

    /**
     * 验证总数是否已经超过最大数量
     */
    private boolean verifyMaximum(List<FoodBean> mList) {
        int count = 0;
        for (FoodBean bean : mList) {
            count = count + bean.getCount();
        }
        if (maximum == 0) {
            return false;
        } else if (count >= maximum) {
            return true;
        }
        return false;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    /**
     * 总数超标弹窗
     */
    private void maxDialog() {
        dialog.aloneDialog(mContext.getString(R.string.dialog_title),
                mContext.getString(R.string.dialog_context2),
                "好的").show();
    }

    /**
     * 判断锅底是否超过标准
     */
    private void isOverProof(FoodBean bean, TextView countText) {
        /**首先判断是否是锅底再次判断是否已经选择规定锅底*/
        if (bean.getMaximum() == null || bean.getMaximum() == Constant.FOOD_DISASTRICT) {
            /**不限制数量*/
            setAddDishes(bean, countText);
        } else {
            if (bean.getCount() >= bean.getMaximum()) {
                /**限制数量*/
                dialog.aloneDialog(mContext.getResources().getString(R.string.dialog_title),
                        mContext.getResources().getString(R.string.dialog_context),
                        mContext.getResources().getString(R.string.dialog_cacel)).show();
            } else {
                setAddDishes(bean, countText);
            }
        }
    }

    /**
     * 菜品添加
     *
     * @param bean
     * @param countText
     */
    private void setAddDishes(FoodBean bean, TextView countText) {
        int number = bean.getCount();
        number++;
        bean.setCount(number);
        countText.setText(number + "");
        EventBus.getDefault().post(new RefreshFoodEvent(bean));
    }

    /**
     * 加菜按钮，减菜按钮权限管理
     *
     * @param button
     */
    public void buttonClick(final ImageView button) {

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
