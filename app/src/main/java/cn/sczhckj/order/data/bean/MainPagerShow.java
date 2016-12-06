package cn.sczhckj.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 主页数据显示
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class MainPagerShow implements Serializable{

    private int cartStatus;//购物车状态

    private String tableNumber;//桌号

    private int defaultNumber;//本桌默认人数

    private String waitress;//服务员

    private String openTablePassword;//开桌密码

    private List<PersonBean> person;//就餐人数推荐表

    private List<DishesBean> potList;//锅底选择

    private List<DishesBean> dishesList;//推荐菜品

    public int getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(int cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public String getWaitress() {
        return waitress;
    }

    public void setWaitress(String waitress) {
        this.waitress = waitress;
    }

    public List<PersonBean> getPerson() {
        return person;
    }

    public void setPerson(List<PersonBean> person) {
        this.person = person;
    }

    public List<DishesBean> getPotList() {
        return potList;
    }

    public void setPotList(List<DishesBean> potList) {
        this.potList = potList;
    }

    public List<DishesBean> getDishesList() {
        return dishesList;
    }

    public void setDishesList(List<DishesBean> dishesList) {
        this.dishesList = dishesList;
    }

    public String getOpenTablePassword() {
        return openTablePassword;
    }

    public void setOpenTablePassword(String openTablePassword) {
        this.openTablePassword = openTablePassword;
    }

    @Override
    public String toString() {
        return "{" +
                "cartStatus=" + cartStatus +
                ", tableNumber='" + tableNumber + '\'' +
                ", defaultNumber=" + defaultNumber +
                ", waitress='" + waitress + '\'' +
                ", openTablePassword='" + openTablePassword + '\'' +
                ", person=" + person +
                ", potList=" + potList +
                ", dishesList=" + dishesList +
                '}';
    }
}
