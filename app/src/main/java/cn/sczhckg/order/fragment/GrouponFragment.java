package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.GrouponAdapter;

/**
 * @describe: 团购券号输入界面
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class GrouponFragment extends BaseFragment {

    @Bind(R.id.groupon_number)
    TextView grouponNumber;
    @Bind(R.id.group_input)
    EditText groupInput;
    @Bind(R.id.group_card)
    RecyclerView groupCard;
    @Bind(R.id.item_groupon_parent)
    RelativeLayout itemGrouponParent;
    @Bind(R.id.group_add_parent)
    LinearLayout groupAddParent;
    @Bind(R.id.input_parent)
    LinearLayout inputParent;

    private List<String> grouponList = new ArrayList<>();

    private GrouponAdapter mGrouponAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupon, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        /**首先把输入框设置为隐藏，当点击添加时显示*/
        itemGrouponParent.setVisibility(View.GONE);

        grouponNumber.setVisibility(View.GONE);
        inputParent.setVisibility(View.VISIBLE);
        mGrouponAdapter = new GrouponAdapter(getContext(), grouponList);
        groupCard.setLayoutManager(new LinearLayoutManager(getContext()));
        groupCard.setAdapter(mGrouponAdapter);
    }

    /**
     * 刷新数据，增加团购券
     *
     * @param number
     */
    private void updata(String number) {
        grouponList.add(number);
        mGrouponAdapter.notifyDataSetChanged(grouponList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.groupon_close, R.id.group_add_parent, R.id.groupon_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupon_close:
                itemGrouponParent.setVisibility(View.GONE);
                break;
            case R.id.group_add_parent:
                if (itemGrouponParent.getVisibility() == View.GONE) {
                    itemGrouponParent.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.groupon_finish:

                if (!(groupInput.getText().toString().equals(""))) {
                    updata(groupInput.getText().toString());
                }
                break;
        }
    }
}
