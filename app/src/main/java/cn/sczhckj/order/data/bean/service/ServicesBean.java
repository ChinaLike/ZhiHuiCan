package cn.sczhckj.order.data.bean.service;

/**
 * @ describe: 服务
 * @ author: Like on 2016/12/16.
 * @ email: 572919350@qq.com
 */

public class ServicesBean {
    private Integer id;//服务ID
    private String name;//服务名称
    private Integer imageId;//图片ID
    private String imageUrl;//图片url

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
