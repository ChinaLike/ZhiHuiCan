package cn.sczhckj.order.data.bean.order;

/**
 * @ describe: 点餐下单
 * @ author: Like on 2016/12/16.
 * @ email: 572919350@qq.com
 */

public class OrderBean {
    private Integer id;//菜品ID
    private Integer cateId;//分类ID
    private Integer count;//数量
    private Integer price;//价格

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
