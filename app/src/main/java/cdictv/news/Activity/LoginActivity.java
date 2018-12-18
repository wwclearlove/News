package cdictv.news.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.R;

public class LoginActivity extends Activity {

    @InjectView(R.id.login_user)
    EditText loginUser;
    @InjectView(R.id.login_password)
    EditText loginPassword;
    @InjectView(R.id.bt_login)
    TextView btLogin;
    @InjectView(R.id.bt_resgis)
    TextView btResgis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.bt_login, R.id.bt_resgis})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                break;
            case R.id.bt_resgis:
                break;
        }
    }
}
