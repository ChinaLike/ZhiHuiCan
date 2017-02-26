package cn.sczhckj.order.data.bean.produce;

/**
 * @ describe: 台桌分类
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class TableCateBean {

    //包间ID
    private Integer id;
    //包间名称
    private String district;
    //包间编码
    private String code;

    private boolean isSelect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", district='" + district + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
