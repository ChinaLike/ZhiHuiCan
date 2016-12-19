package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ describe: 点赞
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
            favorImg.setSelected(false);
            favorText.setText((bean.getFavors() - 1) + "");
            bean.setFavors(bean.getFavors() - 1);
            favorText.setTextColor(ContextCompat.getColor(mContext, R.color.favor_nor));
            bean.setFavor(false);
        } else {
            favorImg.setSelected(true);
            favorText.setText((bean.getFavors() + 1) + "");
            bean.setFavors(bean.getFavors() + 1);
            favorText.setTextColor(ContextCompat.getColor(mContext, R.color.favor_sel));
            bean.setFavor(true);
            initFavor(bean);
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