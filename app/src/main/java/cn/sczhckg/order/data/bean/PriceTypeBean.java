package cn.sczhckg.order.data.bean;

import java.io.Serializable;

/**
 * @describe: 价格优惠类型
 * @author: Like on 2016/11/8.
 * @Email: 572919350@qq.com
 */

public class PriceTypeBean implements Serializable {

    private int type;//优惠类型

    private int price;//优惠价格

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                ", price=" + price +
                '}';
    }
}