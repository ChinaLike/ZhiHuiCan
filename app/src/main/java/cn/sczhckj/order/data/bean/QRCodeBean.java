package cn.sczhckj.order.data.bean;

import java.util.List;

/**
 * @describe: 二维码回调
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class QRCodeBean {

    private String table;

    private int status;

    private String msg;

    private String url;

    private List<ListBean> listBeanList;


    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ListBean> getListBeanList() {
        return listBeanList;
    }

    public void setListBeanList(List<ListBean> listBeanList) {
        this.listBeanList = listBeanList;
    }
}
