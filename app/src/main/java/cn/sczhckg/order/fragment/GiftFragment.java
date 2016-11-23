package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;
import cn.sczhckg.order.data.event.SettleAountsTypeEvent;

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
    @Bind(R.id.gift_price5)
    Button giftPrice5;
    @Bind(R.id.gift_button)
    Button giftButton;

    private int money = 0;

    private Button[] mButtons;

    private int current = 0;

    private int index = 0;

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
        init();
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        mButtons = new Button[]{giftPrice1, giftPrice2, giftPrice3, giftPrice4,giftPrice5};
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.gift_close, R.id.gift_price1, R.id.gift_price2, R.id.gift_price3, R.id.gift_price4, R.id.gift_button,R.id.gift_price5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gift_close:
                EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.TTYPE));
                break;
            case R.id.gift_price1:
                /**两元打赏*/
                money = 2;
                current = 0;
                break;
            case R.id.gift_price2:
                /**5元打赏*/
                money = 5;
                current = 1;
                break;
            case R.id.gift_price3:
                /**10元打赏*/
                money = 10;
                current = 2;
                break;
            case R.id.gift_price4:
                /**15元打赏*/
                money = 15;
                current = 3;
                break;
            case R.id.gift_price5:
                /**自定义打赏*/
                current = 4;
                break;
            case R.id.gift_button:
                /**确认金额*/
                if (onGiftListenner != null) {
                    onGiftListenner.money(money);
                    EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.TTYPE));
                }
                break;
        }
        mButtons[index].setSelected(false);
        mButtons[index].setTextColor(getContext().getResources().getColor(R.color.text_color_red));
        mButtons[current].setSelected(true);
        mButtons[current].setTextColor(getContext().getResources().getColor(R.color.white));
        index = current;
    }
}
