package cn.sczhckj.order.data.bean.produce;

/**
 * @ describe:台桌属性
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class TableAttrBean {

    //属性ID
    private Integer attrId;
    //属性名称
    private String attrName;

    public Integer getAttrId() {
        if (attrId == null){
            return 0;
        }
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    @Override
    public String toString() {
        return "{" +
                "attrId=" + attrId +
                ", attrName='" + attrName + '\'' +
                '}';
    }
}
