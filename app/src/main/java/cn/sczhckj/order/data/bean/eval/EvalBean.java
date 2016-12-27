package cn.sczhckj.order.data.bean.eval;

import java.util.List;

/**
 * @ describe: 评价信息
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class EvalBean {

    private List<EvalItemBean> words;//热词

    private List<EvalItemBean> items;//评价参数列表


    public List<EvalItemBean> getWords() {
        return words;
    }

    public void setWords(List<EvalItemBean> words) {
        this.words = words;
    }

    public List<EvalItemBean> getItems() {
        return items;
    }

    public void setItems(List<EvalItemBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "{" +
                "words=" + words +
                ", items=" + items +
                '}';
    }
}
