package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.ArrayMap;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.overwrite.CommonDialog;
import cn.sczhckj.order.until.show.L;

/**
 * @ describe:  菜品数量控制实现
 * @ author: Like on 2016/12/20.
 * @ email: 572919350@qq.com
 */

public class FoodControlImpl {

    private Context mContext;

    /**
     * 本分类最大数量
     */
    private static int maximum = 0;
    /**
     * 本分类是否必选
     */
    private static int required = 0;
    /**
     * 分类ID
     */
    private static Integer cateId;

    private CommonDialog mDialog;

    public FoodControlImpl(Context mContext) {
        this.mContext = mContext;
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
                cateFoodControler(countText, bean, type);
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

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    private void cateFoodControler(TextView countText, FoodBean bean, int type) {
        /**判断是否限制点菜*/
        if (maximum == Constant.FOOD_DISASTRICT) {
            foodControler(countText, bean, type);
            return;
        }
        int cateNumber = 0;
        /**获取已经下单中对应分类下的数量*/
        for (FoodBean orderBean : BaseFragment.orderList) {
            int cateId = orderBean.getCateId();
            if (cateId == this.cateId) {
                cateNumber++;
            }
        }
        /**获取未下单中对应分类下的数量*/
        for (FoodBean disOrderBean : BaseFragment.disOrderList) {
            int cateId = disOrderBean.getCateAlias();
            if (cateId == this.cateId) {
                cateNumber++;
            }
        }
        /**获取当前对应分类的数量，每一次加一份*/
        int currCateNumber = cateNumber + 1;
        /**判断是否超过对应数量*/
        if (currCateNumber > maximum) {
            dialog(mContext.getString(R.string.dialog_title), mContext.getString(R.string.dialog_context_over_number), "我懂了");
        } else {
            foodControler(countText, bean, type);
        }

    }

    private void foodControler(TextView countText, FoodBean bean, int type) {
        /**判断该菜品有数量控制*/
        if (bean.getMaximum() == null || bean.getMaximum() == Constant.FOOD_DISASTRICT) {
            setAddDishes(bean, countText, type);
            return;
        }
        int foodNumber = 0;
        /**获取已经下单中对应菜品的数量*/
        for (FoodBean orderBean : BaseFragment.orderList) {
            /**获取菜品ID和价格类型ID ， 判断是否是同一个菜品规则：只需要判断菜品ID与价格类型ID是否一样*/
            int foodId = orderBean.getId();
            int priceTypeId = orderBean.getType();
            if (foodId == bean.getId() && priceTypeId == bean.getType()) {
                foodNumber++;
            }
        }
        /**获取未下单中对应菜品的数量*/
        for (FoodBean disOrderBean : BaseFragment.disOrderList) {
            /**获取菜品ID和价格类型ID ， 判断是否是同一个菜品规则：只需要判断菜品ID与价格类型ID是否一样*/
            int foodId = disOrderBean.getId();
            int priceTypeId = disOrderBean.getType();
            if (foodId == bean.getId() && priceTypeId == bean.getType()) {
                foodNumber++;
            }
        }
        /**获取当前对应菜品的数量,每一次加一份*/
        int currFoodNumber = foodNumber + 1;
        if (currFoodNumber > bean.getMaximum()) {
            dialog(mContext.getString(R.string.dialog_title), mContext.getString(R.string.dialog_context_number), "我懂了");
        } else {
            setAddDishes(bean, countText, type);
        }
    }


    /**
     * 一个按钮弹窗
     *
     * @param title
     * @param content
     * @param btn
     */
    private void dialog(String title, String content, String btn) {
        mDialog = new CommonDialog(mContext, CommonDialog.Mode.TEXT);
        mDialog.setTitle(title)
                .setTextContext(content)
                .setPositive(btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                })
                .show();
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
        /**设置分类ID(此处用别名)*/
        bean.setCateAlias(cateId);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(mContext.getResources().getString(R.string.dialog_title), mContext.getResources().getString(R.string.dialog_context_permiss), mContext.getResources().getString(R.string.dialog_cancel));
            }
        });
    }

    /**
     * 验证所有分类下必选的菜品是否必选
     *
     * @param mList    已经选择的菜品集合
     * @param cateBean 分类集合
     * @return 是否已经满足必选要求
     */
    public static String requiredFood(List<FoodBean> mList, List<CateBean.CateItemBean> cateBean) {
        StringBuffer cateName = new StringBuffer();
        /**必选的ID*/
        List<Integer> required = new ArrayList<>();
        for (CateBean.CateItemBean item : cateBean) {
            if (item.getRequired() == Constant.REQUIRED) {
                required.add(item.getId());
            }
        }
        /**获取必选分类的数量*/
        Map<Integer, Integer> requiredNum = new HashMap<>();
        for (Integer id : required) {
            for (FoodBean bean : mList) {
                if (bean.getCateAlias() == id) {
                    if (requiredNum.containsKey(id)) {
                        requiredNum.put(id, requiredNum.get(id) + bean.getCount());
                    } else {
                        requiredNum.put(id, bean.getCount());
                    }
                }
            }
        }
        /**判断是否对应的必选已经全部包含*/
        for (Integer id : required) {
            if (!requiredNum.containsKey(id)) {
                cateName.append(getCateName(id, cateBean));
            }
        }
        return cateName.toString();
    }

    /**
     * 获取指定分类的名字
     */
    private static String getCateName(Integer cateId, List<CateBean.CateItemBean> cateBean) {
        for (CateBean.CateItemBean bean : cateBean) {
            if (bean.getId() == cateId) {
                return bean.getName() + " ";
            }
        }
        return "";
    }
}
