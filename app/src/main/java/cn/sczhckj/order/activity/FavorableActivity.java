package cn.sczhckj.order.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.overwrite.RoundImageView;

/**
 * @ Describe:优惠详情界面，提供申请会员卡信息录入
 * Created by Like on 2016/12/12.
 * @ Email: 572919350@qq.com
 */
public class FavorableActivity extends AppCompatActivity {

    @Bind(R.id.favor_back)
    ImageView favorBack;
    @Bind(R.id.favor_recycler)
    RecyclerView favorRecycler;
    @Bind(R.id.table_number)
    TextView tableNumber;
    @Bind(R.id.waitress)
    TextView waitress;
    @Bind(R.id.table_person_num)
    TextView tablePersonNum;
    @Bind(R.id.no_login)
    ImageView noLogin;
    @Bind(R.id.header)
    RoundImageView header;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.vip_grade)
    ImageView vipGrade;
    @Bind(R.id.has_login)
    LinearLayout hasLogin;
    @Bind(R.id.table_info_parent)
    RelativeLayout tableInfoParent;
    @Bind(R.id.apply_for_vip_card_confirm)
    Button applyForVipCardConfirm;
    @Bind(R.id.apply_for_vip_card_name_input)
    EditText applyForVipCardNameInput;
    @Bind(R.id.apply_for_vip_card_name_cancel)
    ImageView applyForVipCardNameCancel;
    @Bind(R.id.apply_for_vip_card_phone_input)
    EditText applyForVipCardPhoneInput;
    @Bind(R.id.apply_for_vip_card_phone_cancel)
    ImageView applyForVipCardPhoneCancel;
    @Bind(R.id.apply_for_vip_card_recycler)
    RecyclerView applyForVipCardRecycler;
    @Bind(R.id.activity_pot_type)
    LinearLayout activityPotType;
    @Bind(R.id.favor_vipPro)
    WebView favorVipPro;
    @Bind(R.id.apply_for_vip_parent)
    RelativeLayout applyForVipParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorable);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.favor_back, R.id.no_login, R.id.apply_for_vip_card_confirm, R.id.apply_for_vip_card_name_cancel, R.id.apply_for_vip_card_phone_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favor_back:
                break;
            case R.id.no_login:
                break;
            case R.id.apply_for_vip_card_confirm:
                break;
            case R.id.apply_for_vip_card_name_cancel:
                break;
            case R.id.apply_for_vip_card_phone_cancel:
                break;
        }
    }
}
