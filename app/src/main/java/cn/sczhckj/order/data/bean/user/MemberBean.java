package cn.sczhckj.order.data.bean.user;

import java.io.Serializable;

/**
 * @ describe: 卡号登录
 * @ author: Like on 2016/12/16.
 * @ email: 572919350@qq.com
 */

public class MemberBean implements Serializable{

    private Integer id;
    private String memberCode;//会员编码
    private String name;//全称
    private String shortName;//简称（*先生/*女士）
    private Integer gender;//性别
    private String phone;//电话
    private String headImageUrl;//头像地址
    private Integer memberType;//会员类型
    private String memberTypeName;//会员名称
    private String memberTypeImageUrl;//会员类型图片标识
    private Integer isValid;//是否激活
    private Integer isLocked;//是否锁定

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getMemberTypeName() {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

    public String getMemberTypeImageUrl() {
        return memberTypeImageUrl;
    }

    public void setMemberTypeImageUrl(String memberTypeImageUrl) {
        this.memberTypeImageUrl = memberTypeImageUrl;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", memberCode='" + memberCode + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", memberType=" + memberType +
                ", memberTypeName='" + memberTypeName + '\'' +
                ", memberTypeImageUrl='" + memberTypeImageUrl + '\'' +
                ", isValid=" + isValid +
                ", isLocked=" + isLocked +
                '}';
    }
}
