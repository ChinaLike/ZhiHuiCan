package cn.sczhckj.order.data.bean;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe:
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public class RetrofitBean {

    private Integer total_count;
    private Boolean incompleteResults;
    private List<ClipData.Item> items = new ArrayList<ClipData.Item>();

    /**
     *
     * @return
     *     The totalCount
     */
    public Integer getTotalCount() {
        return total_count;
    }

    /**
     *
     * @param totalCount
     *     The total_count
     */
    public void setTotalCount(Integer totalCount) {
        this.total_count = totalCount;
    }

    /**
     *
     * @return
     *     The incompleteResults
     */
    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    /**
     *
     * @param incompleteResults
     *     The incomplete_results
     */
    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    /**
     *
     * @return
     *     The items
     */
    public List<ClipData.Item> getItems() {
        return items;
    }
}
