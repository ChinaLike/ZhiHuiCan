package cn.sczhckj.order.data.bean.push;

/**
 * @ describe: 上菜
 * @ author: Like on 2017-02-09.
 * @ email: 572919350@qq.com
 */

public class ArriveBean {

    private Integer id;//菜品ID
    private Integer cateId;//分类ID
    private Integer arriveCount;//上菜数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(Integer arriveCount) {
        this.arriveCount = arriveCount;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", arriveCount=" + arriveCount +
                '}';
    }
}
