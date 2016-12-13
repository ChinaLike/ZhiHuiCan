package cn.sczhckj.order.data.listener;

import android.view.View;

/**
 * @describe: Recyclerview的Item点击事件
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public interface OnItemClickListener {
    void onItemClick(View view ,int position ,Object bean);
}
