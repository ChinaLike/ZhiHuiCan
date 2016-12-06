package cn.sczhckj.order.data.event;

import java.util.List;

import cn.sczhckj.order.data.bean.VipFavorableBean;

/**
 * @describe: 申请办理会员卡片
 * @author: Like on 2016/11/23.
 * @Email: 572919350@qq.com
 */

public class ApplyForVipCardEvent {
    /**申请办卡*/
    public final static int APPLY=0;
    /**取消办卡*/
    public final static int CANCEL_APPLY=1;
    /**办卡成功关闭界面*/
    public final static int CLOSE=2;

    private int type;

    private int id;

    private List<VipFavorableBean> mList;

    public ApplyForVipCardEvent(int type) {
        this.type = type;
    }

    public ApplyForVipCardEvent(int type, int id) {
        this.type = type;
        this.id = id;
    }

    public ApplyForVipCardEvent(int type, int id, List<VipFavorableBean> mList) {
        this.type = type;
        this.id = id;
        this.mList = mList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VipFavorableBean> getmList() {
        return mList;
    }

    public void setmList(List<VipFavorableBean> mList) {
        this.mList = mList;
    }
}
