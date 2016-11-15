package cn.sczhckg.order.data.listener;

import cn.sczhckg.order.data.bean.PayTypeBean;

/**
 * @describe: 支付方式选择
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public interface OnPayTypeListenner {

    void payType(PayTypeBean bean);
    
}
