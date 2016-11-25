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
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.GrouponAdapter;
import cn.sczhckg.order.data.bean.Bean;
import cn.sczhckg.order.data.bean.GrouponBean;
import cn.sczhckg.order.data.bean.OP;
import cn.sczhckg.order.data.bean.RequestCommonBean;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.data.response.ResponseCode;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 团购券号输入界面
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class GrouponFragment extends BaseFragment implements Callback<Bean<GrouponBean>> {

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
        initLoadingPop();
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

    @OnClick({R.id.groupon_close, R.id.group_add_parent, R.id.groupon_finish, R.id.group_verify})
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
                    postData(groupInput.getText().toString());
                } else {
                    Toast.makeText(getContext(), "输入团购券不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.group_verify:
                /**验证所有，并刷新价格*/
                // TODO: 2016/11/23 这里还未实现刷新价格
                break;
        }
    }

    private void postData(String group) {
        RequestCommonBean bean=new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setGroup(group);
        RestRequest<RequestCommonBean> restRequest= JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_GROUPON)
                .time()
                .bean(bean);
        Call<Bean<GrouponBean>> grouponCall = RetrofitRequest.service().verifyGroup(restRequest.toRequestString());
        grouponCall.enqueue(this);
        showProgress("团购券验证中...");
    }

    @Override
    public void onResponse(Call<Bean<GrouponBean>> call, Response<Bean<GrouponBean>> response) {
        Bean<GrouponBean> bean = response.body();
        if (bean!=null&&bean.getCode() == ResponseCode.SUCCESS) {
            updata(groupInput.getText().toString());
            itemGrouponParent.setVisibility(View.GONE);
            groupInput.setText("");
            dismissProgress();
        } else if(bean!=null){
            loadingFail(bean.getMessage() + "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postData(groupInput.getText().toString());
                }
            });
        }
    }

    @Override
    public void onFailure(Call<Bean<GrouponBean>> call, Throwable t) {
        loadingFail(getString(R.string.overTime), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData(groupInput.getText().toString());
            }
        });
    }

    /**
     * 获取团购券组
     *
     * @return
     */
    public List<String> getGrouponList() {
        return grouponList;
    }
}
