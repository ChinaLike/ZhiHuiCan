package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 结账清单
 * @author: Like on 2016/11/13.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsBean implements Serializable {

    private String table;

    private List<FavorableTypeBean> favorableType;

    private List<PayTypeBean> payTypeBeen;

    private List<SettleAccountsDishesBean> settleAccountsDishesBeen;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<FavorableTypeBean> getFavorableType() {
        return favorableType;
    }

    public void setFavorableType(List<FavorableTypeBean> favorableType) {
        this.favorableType = favorableType;
    }

    public List<PayTypeBean> getPayTypeBeen() {
        return payTypeBeen;
    }

    public void setPayTypeBeen(List<PayTypeBean> payTypeBeen) {
        this.payTypeBeen = payTypeBeen;
    }

    public List<SettleAccountsDishesBean> getSettleAccountsDishesBeen() {
        return settleAccountsDishesBeen;
    }

    public void setSettleAccountsDishesBeen(List<SettleAccountsDishesBean> settleAccountsDishesBeen) {
        this.settleAccountsDishesBeen = settleAccountsDishesBeen;
    }

    @Override
    public String toString() {
        return "{" +
                "table='" + table + '\'' +
                ", favorableType=" + favorableType +
                ", payTypeBeen=" + payTypeBeen +
                ", settleAccountsDishesBeen=" + settleAccountsDishesBeen +
                '}';
    }
}
