package cn.sczhckj.order.data.bean.food;

import java.util.List;

import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.until.TextUntils;

/**
 * @describe: 分类属性
 * @author: Like on 2016/12/12.
 * @Email: 572919350@qq.com
 */

public class CateBean {

    private Integer defaultCate;//默认选择分类
    private List<CateItemBean> cates;

    public Integer getDefaultCate() {
        return TextUntils.empty(defaultCate, 0);
    }

    public void setDefaultCate(Integer defaultCate) {
        this.defaultCate = defaultCate;
    }

    public List<CateItemBean> getCates() {
        return cates;
    }

    public void setCates(List<CateItemBean> cates) {
        this.cates = cates;
    }

    public class CateItemBean {
        private Integer id;//分类ID
        private String name;//分类名字
        private Integer required;//是否必选
        private Integer maximum;//最大数量，0-不控制
        private Integer permiss;//权限，0-不可选，1-可选，required 为1 时，permiss参数必须为1

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

        public Integer getRequired() {
            return TextUntils.empty(required);
        }

        public void setRequired(Integer required) {
            this.required = required;
        }

        public Integer getPermiss() {
            return TextUntils.empty(permiss, Constant.PERMISS_AGREE);
        }

        public void setPermiss(Integer permiss) {
            this.permiss = permiss;
        }

        public Integer getMaximum() {
            return TextUntils.empty(maximum, 0);
        }

        public void setMaximum(Integer maximum) {
            this.maximum = maximum;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", required=" + required +
                    ", maximum=" + maximum +
                    ", permiss=" + permiss +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "defaultCate=" + defaultCate +
                ", cates=" + cates +
                '}';
    }
}
