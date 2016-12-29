package cn.sczhckj.order.data.bean.table;

import java.util.List;

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
                '}';
    }
}
