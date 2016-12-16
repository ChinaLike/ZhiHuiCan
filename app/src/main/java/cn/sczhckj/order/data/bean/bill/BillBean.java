package cn.sczhckj.order.data.bean.bill;

import java.util.List;

import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @ describe: 结账清单
 * @ author: Like on 2016/12/16.
 * @ email: 572919350@qq.com
 */

public class BillBean {

    private Integer id;//分类ID
    private String name;//分类名称
    private Integer count;//数量
    private Double sum;//小计
    private List<FoodBean> foods;//菜品

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public List<FoodBean> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodBean> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", sum=" + sum +
                ", foods=" + foods +
                '}';
    }
}
