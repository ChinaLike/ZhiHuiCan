package cn.sczhckj.order.data.bean.produce;

import java.util.List;

/**
 * @ describe: 台桌属性
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class TableBean {

    //台ID
    private Integer id;
    //台桌名称
    private String tableName;
    //台桌UUID (即设备ID)
    private String deviceId;
    //台桌编码
    private String code;
    //人数
    private Integer person;
    //备注
    private String memo;
    //是否并桌点餐
    private Integer combine; //0否，1是
    //消费类型
    private Integer consumeType;//=0,单独消费;=1,并桌消费(主桌);=2,并桌消费(辅桌);当有并桌记录ID时有效
    //台桌状态
    private Integer status;
    //消费开始时间
    private String time;
    //台桌属性
    private List<TableAttrBean> attr;

    private boolean isSelect;//是否选中

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getCombine() {
        return combine;
    }

    public void setCombine(Integer combine) {
        this.combine = combine;
    }

    public Integer getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(Integer consumeType) {
        this.consumeType = consumeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TableAttrBean> getAttr() {
        return attr;
    }

    public void setAttr(List<TableAttrBean> attr) {
        this.attr = attr;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", code='" + code + '\'' +
                ", person=" + person +
                ", memo='" + memo + '\'' +
                ", combine=" + combine +
                ", consumeType=" + consumeType +
                ", status=" + status +
                ", time='" + time + '\'' +
                ", attr=" + attr +
                '}';
    }
}
