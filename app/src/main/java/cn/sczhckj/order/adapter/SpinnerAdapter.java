package cn.sczhckj.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.produce.TableAttrBean;

/**
 * @ describe: 下拉列表适配器
 * @ author: Like on 2017-02-24.
 * @ email: 572919350@qq.com
 */

public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;

    private List<TableAttrBean> mList;

    public SpinnerAdapter(Context mContext, List<TableAttrBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_table_attr, null);
        if (convertView != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.table_attr);
            textView.setText(mList.get(position).getAttrName());
        }
        return convertView;
    }
}
