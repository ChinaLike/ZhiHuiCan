package cn.sczhckj.order.data.bean;

import java.util.List;

/**
 * @describe: 开桌信息属性
 * @author: Like on 2016/12/13.
 * @Email: 572919350@qq.com
 */

public class OpenInfoBean {

    private Integer id;//台桌ID
    private String tableName;//台桌名称
    private Integer maximum;//最大人数，即本桌默认的人数
    private String waiter;//服务员
    private List<Integer> persons;//候选人数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public List<Integer> getPersons() {
        return persons;
    }

    public void setPersons(List<Integer> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", maximum=" + maximum +
                ", waiter='" + waiter + '\'' +
                ", persons=" + persons +
                '}';
    }
}