package cdictv.news.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.R;

public class RegisteredActivity extends AppCompatActivity {

    @InjectView(R.id.reg_user)
    EditText regUser;
    @InjectView(R.id.reg_phone)
    EditText regPhone;
    @InjectView(R.id.reg_paw)
    EditText regPaw;
    @InjectView(R.id.reg_paw_two)
    EditText regPawTwo;
    @InjectView(R.id.reg_yzm)
    EditText regYzm;
    @InjectView(R.id.reg_code)
    TextView regCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.inject(this);
        if(regPaw.getText().toString().trim()==regPawTwo.getText().toString().trim()){

        }
    }

    @OnClick(R.id.reg_code)
    public void onViewClicked() {

    }
}
