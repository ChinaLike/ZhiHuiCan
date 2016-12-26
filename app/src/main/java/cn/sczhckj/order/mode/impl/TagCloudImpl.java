package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.BaseCommAdapter;
import cn.sczhckj.order.adapter.TagCloudAdapter;
import cn.sczhckj.order.data.bean.food.PriceBean;
import cn.sczhckj.order.overwrite.FlowLayout;
import cn.sczhckj.order.adapter.TagAdapter;
import cn.sczhckj.order.overwrite.TagCloudLayout;
import cn.sczhckj.order.overwrite.TagFlowLayout;
import cn.sczhckj.order.overwrite.TextViewBorder;
import cn.sczhckj.order.until.ColorUntils;

/**
 * @describe: 标签云实现
 * @author: Like on 2016/12/14.
 * @Email: 572919350@qq.com
 */

public class TagCloudImpl {

    private Context mContext;

    private final int ROW = 2;

    public TagCloudImpl(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 设置价格标签云（未对齐,只能刷新一次）
     *
     * @param recyclerView 自定义格式
     * @param bean
     */
    public void setPrice(TagCloudLayout recyclerView, List<PriceBean> bean) {
        if (bean != null && bean.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new BaseCommAdapter<PriceBean>(bean, mContext) {
                @Override
                protected void setUI(ViewHolder holder, int position, Context context) {
                    PriceBean beanP = getItem(position);
                    /**设置内容*/
                    TextViewBorder mTextViewBorder = holder.getItemView(R.id.favorable_context);
                    mTextViewBorder.setText(beanP.getTitle() + "：¥" + beanP.getPrice());
                    /**设置字体颜色*/
                    mTextViewBorder.setTextColor(ColorUntils.stringToHex(beanP.getColor()));
                    /**设置边框颜色*/
                    mTextViewBorder.setBorderColor(ColorUntils.stringToHex(beanP.getColor()));
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

    /**
     * 设置价格标签云（对齐）
     *
     * @param recyclerView 系统列表格式控件
     * @param bean
     */
    public void setPrice(RecyclerView recyclerView, List<PriceBean> bean) {
        if (bean != null && bean.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            TagCloudAdapter mTagCloudAdapter = new TagCloudAdapter(bean, mContext);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, ROW));
            recyclerView.setAdapter(mTagCloudAdapter);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置价格标签云（支持刷新）
     *
     * @param recyclerView 系统列表格式控件
     * @param bean
     */
    public void setPrice(final TagFlowLayout recyclerView, List<PriceBean> bean) {
        if (bean != null && bean.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(new TagAdapter<PriceBean>(bean) {
                @Override
                public View getView(FlowLayout parent, int position, PriceBean item) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_cloud, recyclerView, false);
                    /**设置内容*/
                    TextViewBorder mTextViewBorder = (TextViewBorder) view.findViewById(R.id.favorable_context);
                    mTextViewBorder.setText(item.getTitle() + "：¥" + item.getPrice());
                    /**设置字体颜色*/
                    mTextViewBorder.setTextColor(ColorUntils.stringToHex(item.getColor()));
                    /**设置边框颜色*/
                    mTextViewBorder.setBorderColor(ColorUntils.stringToHex(item.getColor()));
                    return view;
                }
            });
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置热词
     * @param recyclerView
     * @param mList
     */
    private void setWord(final TagFlowLayout recyclerView,List<Integer> mList){
        recyclerView.setAdapter(new TagAdapter<Integer>(mList) {
            @Override
            public View getView(FlowLayout parent, int position, Integer o) {
                return null;
            }
        });
    }

}
