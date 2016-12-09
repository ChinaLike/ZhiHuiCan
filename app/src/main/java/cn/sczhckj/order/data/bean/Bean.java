package cn.sczhckj.order.data.bean;

/**
 * @describe: 通用父类参数,服务器返回码统一放在code字段中，备注信息统一放在message中，请求的数据放在result
 * @author: Like on 2016/11/18.
 * @Email: 572919350@qq.com
 */

public class Bean<T> {

    private int code;

    private String message;

    private T result;

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
