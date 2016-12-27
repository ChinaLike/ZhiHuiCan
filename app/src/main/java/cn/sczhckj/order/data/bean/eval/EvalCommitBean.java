package cn.sczhckj.order.data.bean.eval;

import java.util.List;

/**
 * @ describe:
 * @ author: Like on 2016/12/27.
 * @ email: 572919350@qq.com
 */

public class EvalCommitBean {

    private String deviceId;

    private String memberCode;

    private List<Integer> words;//热词Id

    private List<EvalItemBean> items;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public List<Integer> getWords() {
        return words;
    }

    public void setWords(List<Integer> words) {
        this.words = words;
    }

    public List<EvalItemBean> getItems() {
        return items;
    }

    public void setItems(List<EvalItemBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceId='" + deviceId + '\'' +
                ", memberCode='" + memberCode + '\'' +
                ", words=" + words +
                ", items=" + items +
                '}';
    }
}
