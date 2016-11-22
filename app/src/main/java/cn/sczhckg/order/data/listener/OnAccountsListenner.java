package cn.sczhckg.order.data.listener;

import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;

/**
 * @describe: 结算方式监听
 * @author: Like on 2016/11/22.
 * @Email: 572919350@qq.com
 */

public interface OnAccountsListenner {
    void favorableType(FavorableTypeBean bean);
    void payType(PayTypeBean bean);
}
