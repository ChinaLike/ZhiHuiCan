package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.fragment.BaseFragment;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ describe: 点赞逻辑实现
 * @ author: Like on 2016/12/19.
 * @ email: 572919350@qq.com
 */

public class FavorImpl {

    private Context mContext;
    /**
     * 点赞数据请求
     */
    private FoodMode mFoodMode;

    public FavorImpl(Context mContext) {
        this.mContext = mContext;
        mFoodMode = new FoodMode();
    }

    /**
     * 点赞实现
     *
     * @param favorImg  点赞控件
     * @param favorText 点赞数量显示控件
     * @param bean      原有点赞数量
     */
    public void favor(ImageView favorImg, TextView favorText, FoodBean bean) {
        if (bean.isFavor()) {
            /**已点赞*/
//            favorImg.setSelected(false);
//            favorText.setText((bean.getFavors() - 1) + "");
//            bean.setFavors(bean.getFavors() - 1);
//            favorText.setTextColor(ContextCompat.getColor(mContext, R.color.favor_nor));
//            bean.setFavor(false);
        } else {
            /**未点赞*/
            BaseFragment.favorFood.add(bean);
            favorImg.setSelected(true);
            favorText.setText((bean.getFavors() + 1) + "");
            bean.setFavors(bean.getFavors() + 1);
            favorText.setTextColor(ContextCompat.getColor(mContext, R.color.favor_sel));
            bean.setFavor(true);
            initFavor(bean);
        }
    }

    /**
     * 点赞视图处理
     *
     * @param imageView 点赞图标
     * @param textView  点赞数
     * @param bean      参数
     */
    public void favorView(ImageView imageView, TextView textView, FoodBean bean) {
        int foodId = bean.getId();
        int catesId = bean.getCateId();
        imageView.setSelected(false);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.favor_nor));
        for (FoodBean item : BaseFragment.favorFood) {
            if (foodId == item.getId() && catesId == item.getCateId()) {
                imageView.setSelected(true);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.favor_sel));
            }
        }
    }

    /**
     * 点赞属性处理
     *
     * @param list  获取到的集合
     * @param mList 已点赞的集合
     */
    public static List<FoodBean> favorAttr(List<FoodBean> list, List<FoodBean> mList) {
        if (mList == null || mList.size() == 0) {
            return list;
        } else {
            for (FoodBean itemP : list) {
                int foodId = itemP.getId();
                int catesId = itemP.getCateId();
                for (FoodBean itemC : mList) {
                    if (itemC.getId() == foodId && itemC.getCateId() == catesId) {
                        itemP.setFavor(true);
                    }
                }
            }
            return list;
        }
    }

    /**
     * 点赞初始化
     */
    private void initFavor(FoodBean bean) {
        RequestCommonBean requestCommonBean = new RequestCommonBean();
        requestCommonBean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        requestCommonBean.setRecordId(MyApplication.recordId);
        requestCommonBean.setFoodId(bean.getId());
        requestCommonBean.setCateId(bean.getCateId());
        requestCommonBean.setMemberCode(MyApplication.memberCode);
        mFoodMode.favor(requestCommonBean, favorCallback);
    }

    /**
     * 点赞回调
     */
    Callback<Bean<ResponseCommonBean>> favorCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            if (bean != null) {
                T.showShort(mContext, bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {

        }
    };

}