package cn.sczhckg.order.data.event;

/**
 * @describe: 二维码验证事件
 * @author: Like on 2016/11/29.
 * @Email: 572919350@qq.com
 */

public class QRCodeVerifyEvent {

    private Integer code;

    private String msg;

    public QRCodeVerifyEvent(int code,String msg) {
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "QRCodeVerifyEvent{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
