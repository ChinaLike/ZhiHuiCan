package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.BaseCommAdapter;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.overwrite.TagCloudLayout;
import cn.sczhckj.order.overwrite.TextViewBorder;
import cn.sczhckj.order.until.ColorUntils;

/**
 * @describe: 标签云实现
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class TagCloudImpl {

    private Context mContext;

    public TagCloudImpl(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 设置价格标签
     * @param recyclerView
     * @param bean
     */
    public void setPrice(TagCloudLayout recyclerView , List<PriceBean> bean){
        if (bean != null && bean.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new BaseCommAdapter<PriceBean>(bean, mContext) {
                @Override
                protected void setUI(ViewHolder holder, int position, Context context) {
                    PriceBean bean=getItem(position);
                    /**设置内容*/
                    TextViewBorder mTextViewBorder=holder.getItemView(R.id.favorable_context);
                    mTextViewBorder.setText(bean.getTitle()+"：¥"+bean.getPrice());
                    /**设置字体颜色*/
                    mTextViewBorder.setTextColor(ColorUntils.stringToHex(bean.getColor()));
                    /**设置边框颜色*/
                    mTextViewBorder.setBorderColor(ColorUntils.stringToHex(bean.getColor()));
                }

                @Override
                protected int getLayoutId() {
                    return R.layout.item_favorable;
                }
            });
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void getPrice(TextView price,List<PriceBean> been){
        if (been!=null) {
            for (PriceBean item : been) {
                if (item.getActive() != null && item.getActive() == Constant.PRICE_ACTIVE) {
                    price.setText(item.getPrice() + "");
                }
            }
        }
    }

}
