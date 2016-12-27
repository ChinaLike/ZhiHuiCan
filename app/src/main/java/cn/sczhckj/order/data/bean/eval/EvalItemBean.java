package cn.sczhckj.order.data.bean.eval;

/**
 * @ describe:  评价内容子项
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class EvalItemBean {
    private Integer id;
    private String name;
    private Integer maximum;//最大星星数
    private Float number;//默认选择星星数

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

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Float getNumber() {
        return number;
    }

    public void setNumber(Float number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maximum=" + maximum +
                ", number=" + number +
                '}';
    }
}
