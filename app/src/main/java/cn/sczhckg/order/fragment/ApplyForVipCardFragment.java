package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.ApplyForVipCardAdapter;
import cn.sczhckg.order.data.bean.Bean;
import cn.sczhckg.order.data.bean.CommonBean;
import cn.sczhckg.order.data.bean.Constant;
import cn.sczhckg.order.data.bean.VipFavorableBean;
import cn.sczhckg.order.data.event.ApplyForVipCardEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 申请VIP办理界面，录入消费者初步信息
 * @author: Like on 2016/11/23.
 * @Email: 572919350@qq.com
 */

public class ApplyForVipCardFragment extends BaseFragment implements Callback<Bean<CommonBean>> {


    @Bind(R.id.apply_for_vip_card_name_input)
    EditText applyForVipCardNameInput;
    @Bind(R.id.apply_for_vip_card_phone_input)
    EditText applyForVipCardPhoneInput;
    @Bind(R.id.apply_for_vip_card_recycler)
    RecyclerView applyForVipCardRecycler;

    private ApplyForVipCardAdapter adapter;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_vip_card, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setData(Object object) {
        List<VipFavorableBean> bean = (List<VipFavorableBean>) object;
        for (VipFavorableBean item : bean) {
            if (item.isSelect()) {
                type = item.getId();
            }
        }
        adapter = new ApplyForVipCardAdapter(getContext(), bean);
        applyForVipCardRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        applyForVipCardRecycler.setAdapter(adapter);
    }

    @Override
    public void init() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.apply_for_vip_card_confirm, R.id.apply_for_vip_card_name_cancel, R.id.apply_for_vip_card_phone_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_for_vip_card_confirm:
                if (applyForVipCardNameInput.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请您输入您的姓名", Toast.LENGTH_SHORT).show();
                } else if (applyForVipCardPhoneInput.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请您输入您的电话号码", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject object = new JSONObject();
                    try {
                        object.put(Constant.APPLY_FOR_VIP_NAME, applyForVipCardNameInput.getText().toString());
                        object.put(Constant.APPLY_FOR_VIP_PHONE, applyForVipCardPhoneInput.getText().toString());
                        object.put(Constant.APPLY_FOR_VIP_TYPE, type);
                        Call<Bean<CommonBean>> applyCall = RetrofitRequest.service().applyForVip(object.toString());
                        applyCall.enqueue(this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case R.id.apply_for_vip_card_name_cancel:
                /**清空名字*/
                applyForVipCardNameInput.setText("");
                break;
            case R.id.apply_for_vip_card_phone_cancel:
                /**清空电话号码*/
                applyForVipCardPhoneInput.setText("");
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<CommonBean>> call, Response<Bean<CommonBean>> response) {
        Bean<CommonBean> bean = response.body();
        Toast.makeText(getContext(), bean.getMessage(), Toast.LENGTH_SHORT).show();
        if (bean.getCode() == 0) {
            EventBus.getDefault().post(new ApplyForVipCardEvent(ApplyForVipCardEvent.CLOSE));
        }
    }

    @Override
    public void onFailure(Call<Bean<CommonBean>> call, Throwable t) {
        Toast.makeText(getContext(), getResources().getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }
}
