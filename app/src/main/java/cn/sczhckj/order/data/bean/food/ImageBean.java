package cn.sczhckj.order.data.bean.food;

/**
 * @ describe: 菜品详情图片轮播信息
 * @ author: Like on 2016/12/16.
 * @ email: 572919350@qq.com
 */

public class ImageBean {

    private Integer imageId;//图片ID
    private String remark;//说明
    private String imageUrl;//图片地址

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
                "imageId=" + imageId +
                ", remark='" + remark + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
