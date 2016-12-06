package cn.sczhckj.order.data.bean;

/**
 * @describe: 支付清单返回
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class ListBean {

    private int id;

    private String title;

    private String context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
