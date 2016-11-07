package cn.sczhckg.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckg.order.R;

public class LeadActivity extends Activity {

    @Bind(R.id.right)
    Button right;
    @Bind(R.id.deny)
    Button deny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.right, R.id.deny})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.right:
                intent = new Intent(LeadActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.deny:
                intent = new Intent(LeadActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
