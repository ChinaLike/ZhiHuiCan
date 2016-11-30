package cn.sczhckg.order.data.event;

import java.util.List;

/**
 * @describe: 团购券验证
 * @author: Like on 2016/11/30.
 * @Email: 572919350@qq.com
 */

public class GrouponVerifyEvent {
    private List<String> list;

    public GrouponVerifyEvent(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
