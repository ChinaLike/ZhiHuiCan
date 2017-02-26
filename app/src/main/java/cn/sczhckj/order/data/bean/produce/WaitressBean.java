package cn.sczhckj.order.data.bean.produce;

/**
 * @ describe: 服务员信息
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class WaitressBean {

    //员工ID
    private String id;
    //员工名称
    private String name;
    //员工状态
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
