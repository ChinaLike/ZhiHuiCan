package cn.sczhckj.order.data.bean;

import java.io.Serializable;
import java.util.List;

import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @describe: 菜单一级分类
 * @author: Like on 2016/11/10.
 * @Email: 572919350@qq.com
 */

public class ClassifyBean implements Serializable {

    private String table;//桌号

    private int defaultClassify;//默认显示第几个分类，可选，默认0

    private int orderMoreHint;//菜品过多提醒数量

    private int mainTale;//是否是主桌，0-是 1-不是

    private List<CateBean.CateItemBean> tabList;//导航栏，只有在单桌点菜的主桌才会显示

    private List<ClassifyItemBean> classifyItemList;//每一个小项

    private List<FoodBean> cartList;//购物车目前所有信息

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getDefaultClassify() {
        return defaultClassify;
    }

    public void setDefaultClassify(int defaultClassify) {
        this.defaultClassify = defaultClassify;
    }

    public int getOrderMoreHint() {
        return orderMoreHint;
    }

    public void setOrderMoreHint(int orderMoreHint) {
        this.orderMoreHint = orderMoreHint;
    }

    public int getMainTale() {
        return mainTale;
    }

    public void setMainTale(int mainTale) {
        this.mainTale = mainTale;
    }

    public List<CateBean.CateItemBean> getTabList() {
        return tabList;
    }

    public void setTabList(List<CateBean.CateItemBean> tabList) {
        this.tabList = tabList;
    }

    public List<ClassifyItemBean> getClassifyItemList() {
        return classifyItemList;
    }

    public void setClassifyItemList(List<ClassifyItemBean> classifyItemList) {
        this.classifyItemList = classifyItemList;
    }

    public List<FoodBean> getCartList() {
        return cartList;
    }

    public void setCartList(List<FoodBean> cartList) {
        this.cartList = cartList;
    }

    @Override
    public String toString() {
        return "ClassifyBean{" +
                "table='" + table + '\'' +
                ", defaultClassify=" + defaultClassify +
                ", orderMoreHint=" + orderMoreHint +
                ", mainTale=" + mainTale +
                ", tabList=" + tabList +
                ", classifyItemList=" + classifyItemList +
                ", cartList=" + cartList +
                '}';
    }
}
