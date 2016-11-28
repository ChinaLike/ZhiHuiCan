package cn.sczhckg.order.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe: 评价数据
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class EvaluateBean implements Serializable {

    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
