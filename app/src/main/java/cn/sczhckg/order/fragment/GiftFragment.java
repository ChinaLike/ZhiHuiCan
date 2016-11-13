package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;

/**
 * @describe: 打赏界面
 * @author: Like on 2016/11/12.
 * @Email: 572919350@qq.com
 */

public class GiftFragment extends BaseFragment {

    @Bind(R.id.gift_close)
    ImageView giftClose;
    @Bind(R.id.gift_price1)
    Button giftPrice1;
    @Bind(R.id.gift_price2)
    Button giftPrice2;
    @Bind(R.id.gift_price3)
    Button giftPrice3;
    @Bind(R.id.gift_price4)
    Button giftPrice4;
    @Bind(R.id.gift_button)
    Button giftButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.gift_close, R.id.gift_price1, R.id.gift_price2, R.id.gift_price3, R.id.gift_price4, R.id.gift_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gift_close:
                break;
            case R.id.gift_price1:
                break;
            case R.id.gift_price2:
                break;
            case R.id.gift_price3:
                break;
            case R.id.gift_price4:
                break;
            case R.id.gift_button:
                break;
        }
    }
}
