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
    private Integer recordId;//消费记录ID




    private int showType;//显示点菜方式

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public Integer getFoodCountHint() {
        return foodCountHint;
    }

    public void setFoodCountHint(Integer foodCountHint) {
        this.foodCountHint = foodCountHint;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "{" +
                "foodCountHint=" + foodCountHint +
                ", waiter='" + waiter + '\'' +
                ", message='" + message + '\'' +
                ", showType=" + showType +
                '}';
    }
}
