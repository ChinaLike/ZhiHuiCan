package cn.sczhckj.order.data.bean.table;

import java.util.List;

import cn.sczhckj.order.data.bean.user.MemberBean;
import cn.sczhckj.order.data.constant.Constant;

/**
 * @describe: 开桌信息属性
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class TableBean {

    private Integer id;//台桌ID
    private String name;//台桌名称
    private Integer maximum;//最大人数，即本桌默认的人数
    private String waiter;//服务员
    private Integer status;//台桌状态 1-不可用 2-未开台 3-预定 4-空桌 5-已开桌 6-已上菜 7-结帐中 8-打扫中
    private String remark;//消息内容
    private List<Integer> persons;//候选人数
    private Integer recordId;//消费记录ID
    private Integer foodCountHint;//菜品过多提醒,只有status=3,5,6,7,8有效，其余时候可不返回
    private MemberBean user;//会员信息
    private Integer isShow;//是否显示点餐方式，0-不显示 1-显示
    private Integer orderType;//点餐方式
    private boolean isLogin;//本地字段是否登录

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return name;
    }

    public void setTableName(String tableName) {
        this.name = tableName;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Integer> getPersons() {
        return persons;
    }

    public void setPersons(List<Integer> persons) {
        this.persons = persons;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getFoodCountHint() {
        if (foodCountHint == null) {
            return 10;
        }
        return foodCountHint;
    }

    public void setFoodCountHint(Integer foodCountHint) {
        this.foodCountHint = foodCountHint;
    }

    public MemberBean getUser() {
        return user;
    }

    public void setUser(MemberBean user) {
        this.user = user;
    }

    public Integer getIsShow() {
        if (isShow == null) {
            return Constant.SHOW_TYPE;
        }
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getOrderType() {
        if (orderType == null) {
            return Constant.ORDER_TYPE_ALONE;
        }
        return orderType;
    }

    public boolean isLogin() {
        if (getUser() == null)
            return false;
        else
            return true;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maximum=" + maximum +
                ", waiter='" + waiter + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", persons=" + persons +
                ", recordId=" + recordId +
                ", foodCountHint=" + foodCountHint +
                ", user=" + user +
                ", isShow=" + isShow +
                ", orderType=" + orderType +
                ", isLogin=" + isLogin +
                '}';
    }
}
