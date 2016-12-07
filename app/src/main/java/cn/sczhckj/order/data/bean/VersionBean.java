package cn.sczhckj.order.data.bean;

/**
 * @describe: 版本信息
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionBean {

    private Integer versionCode;//版本号

    private String versionName;//版本名字

    private String versionSize;//版本大小

    private String updateContent;//版本更新内容

    private String host;//主机

    private String url;//版本更新下载地址

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

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
