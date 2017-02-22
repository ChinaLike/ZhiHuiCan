package cn.sczhckj.order.data.bean.table;

import java.util.List;

/**
 * @describe: 台桌信息
 * @author: Like on 2016/12/16.
 * @Email: 572919350@qq.com
 */

public class InfoBean {
    private Integer tableType;//台桌类型，0-单独点餐，1-主桌，2-辅桌
    private Integer id;//台桌ID
    private String name;//台桌名称

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
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

    @Override
    public String toString() {
        return "{" +
                "tableType=" + tableType +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
