package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.sczhckj.order.R;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.until.show.L;

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
     * 添加菜品,菜品列表的加号
     *
     * @param addImg    加菜按钮
     * @param countText 数量显示文本
     * @param bean      菜品数据
     * @param mList     该分类所有菜目
     */
    public void addFood(ImageView addImg, final TextView countText, final FoodBean bean, final List<FoodBean> mList, final int type) {
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyMaximum(mList)) {
                    /**总数已经超过本菜品分类设定数量*/
                    maxDialog();
                } else {
                    /**总数未超标，验证单个菜品数量是否超标*/
                    isOverProof(bean, countText, type);
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
    public void minusFood(ImageView minusImg, final TextView countText, final FoodBean bean, final int type) {
        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**总数量*/
                int number = bean.getCount();
                if (number > 0) {
                    BaseFragment.isAddFood = false;
                    number--;
                    bean.setCount(number);
                    countText.setText(number + "");
                    EventBus.getDefault().post(new RefreshFoodEvent(type, bean));
                }
            }
        });
    }

    /**
     * 减少菜品
     *
     * @param minusImg 减少按钮
     * @param bean     参数对象
     */
    public void minusFood(final ImageView minusImg, final FoodBean bean, final int type) {
        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = bean.getCount();
                if (number > 0) {
                    BaseFragment.isAddFood = false;
                    number--;
                    bean.setCount(number);
                    EventBus.getDefault().post(new RefreshFoodEvent(type, bean));
                }
            }
        });
    }

    /**
     * 退菜
     *
     * @param refundImg
     * @param bean
     */
    public void refund(ImageView refundImg, final FoodBean bean) {
        refundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RefreshFoodEvent(RefreshFoodEvent.CART_REFUND, bean));
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
    private void isOverProof(FoodBean bean, TextView countText, int type) {
        /**判断最大数量，如果是0，则不限制点菜*/
        if (bean.getMaximum() == null || bean.getMaximum() == Constant.FOOD_DISASTRICT) {
            /**不限制数量*/
            setAddDishes(bean, countText, type);
        } else {
            if (bean.getCount() >= bean.getMaximum()) {
                /**限制数量*/
                dialog.aloneDialog(mContext.getResources().getString(R.string.dialog_title),
                        mContext.getResources().getString(R.string.dialog_context),
                        mContext.getResources().getString(R.string.dialog_cancel)).show();
            } else {
                setAddDishes(bean, countText, type);
            }
        }
    }

    /**
     * 菜品添加
     *
     * @param bean
     * @param countText
     */
    private void setAddDishes(FoodBean bean, TextView countText, int type) {
        BaseFragment.isAddFood = true;
        int number = bean.getCount();
        number++;
        bean.setCount(number);
        countText.setText(number + "");
        /**发送广播，让购物车、必选菜品、下单点餐界面处理数据*/
        EventBus.getDefault().post(new RefreshFoodEvent(type, bean));
    }

    /**
     * 加菜按钮，减菜按钮权限管理
     *
     * @param button
     */
    public void buttonClick(final ImageView button) {
        if (dialog != null && dialog.getDialog().isShowing()) {
            dialog.getDialog().dismiss();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.aloneDialog(mContext.getResources().getString(R.string.dialog_title),
                        mContext.getResources().getString(R.string.dialog_context1),
                        mContext.getResources().getString(R.string.dialog_cancel)).show();
            }
        });
    }

}
