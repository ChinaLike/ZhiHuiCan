package cn.sczhckg.order.data.event;

import java.util.List;

import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;

/**
 * @describe: 结账方式
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsCartEvent {
    /**加载数据*/
    public final static int LOADING=1;

    private int type;

    private List<FavorableTypeBean> favorableTypeList;

    private List<PayTypeBean> payTypeList;


    public SettleAountsCartEvent(int type, List<FavorableTypeBean> favorableTypeList, List<PayTypeBean> payTypeList) {
        this.type = type;
        this.favorableTypeList = favorableTypeList;
        this.payTypeList = payTypeList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<FavorableTypeBean> getFavorableTypeList() {
        return favorableTypeList;
    }

    public void setFavorableTypeList(List<FavorableTypeBean> favorableTypeList) {
        this.favorableTypeList = favorableTypeList;
    }

    public List<PayTypeBean> getPayTypeList() {
        return payTypeList;
    }

    public void setPayTypeList(List<PayTypeBean> payTypeList) {
        this.payTypeList = payTypeList;
    }
}
