package cn.sczhckj.order.data.bean.push;

/**
 * @ describe:
 * @ author: Like on 2017-01-13.
 * @ email: 572919350@qq.com
 */

public class HeartBean {
    private String deviceId;
    private String ip;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceId='" + deviceId + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
