package cn.sczhckj.order.data.bean.device;

/**
 * @ describe: 异常信息收集
 * @ author: Like on 2017-02-16.
 * @ email: 572919350@qq.com
 */

public class ExceptionBean {

    private String deviceId;//设备ID
    private Integer type;//标识0-pad点菜端 1-后端平板端
    private Integer versionCode;//版本号
    private String versionName;//版本名
    private String systemVersionCode;//系统版本号
    private String phoneMode;//版本
    private String exceptionMsg;//异常信息

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSystemVersionCode() {
        return systemVersionCode;
    }

    public void setSystemVersionCode(String systemVersionCode) {
        this.systemVersionCode = systemVersionCode;
    }

    public String getPhoneMode() {
        return phoneMode;
    }

    public void setPhoneMode(String phoneMode) {
        this.phoneMode = phoneMode;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceId='" + deviceId + '\'' +
                ", type=" + type +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", systemVersionCode='" + systemVersionCode + '\'' +
                ", phoneMode='" + phoneMode + '\'' +
                ", exceptionMsg='" + exceptionMsg + '\'' +
                '}';
    }
}
