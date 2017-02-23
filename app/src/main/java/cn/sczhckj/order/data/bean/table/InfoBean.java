package cn.sczhckj.order.data.bean.table;

import cn.sczhckj.order.until.TextUntils;

/**
 * @describe: 台桌信息
 * @author: Like on 2016/12/16.
 * @Email: 572919350@qq.com
 */

public class    InfoBean {
    private Integer consumeType;//台桌消费类型，0-单独点餐，1-并桌主桌，2-并桌辅桌
    private Integer combine;//0-不合并点餐，1-合并点餐
    private Integer id;//台桌ID
    private String name;//台桌名称

    public Integer getConsumeType() {
        return TextUntils.empty(consumeType, -1);
    }

    public void setConsumeType(Integer consumeType) {
        this.consumeType = consumeType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCombine() {
        return TextUntils.empty(combine, -1);
    }

    public void setCombine(Integer combine) {
        this.combine = combine;
    }

    @Override
    public String toString() {
        return "{" +
                "consumeType=" + consumeType +
                ", combine=" + combine +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
