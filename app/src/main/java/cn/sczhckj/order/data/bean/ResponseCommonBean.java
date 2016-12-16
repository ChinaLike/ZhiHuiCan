package cn.sczhckj.order.data.bean;

/**
 * @describe: 通用响应数据
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class ResponseCommonBean{

    private Integer foodCountHint;//菜品过多提醒
    private String waiter;//服务员
    private String message;//消息




    private int showType;//显示点菜方式

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
