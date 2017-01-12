package cn.sczhckj.order.data.bean.device;

/**
 * @describe: 版本信息
 * @author: Like on 2016/12/6.
 * @Email: 572919350@qq.com
 */

public class VersionBean {

    private Integer code;//版本号

    private String name;//APK名称

    private String size;//版本大小

    private String content;//版本更新内容

    private String url;//版本更新下载地址

    private String version;//版本

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
