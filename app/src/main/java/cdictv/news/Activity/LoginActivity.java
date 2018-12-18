package cdictv.news.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cdictv.news.R;

public class LoginActivity extends Activity {

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
                startActivity(new Intent(this,MainActivity.class));

                break;
            case R.id.bt_resgis:
                startActivity(new Intent(this,RegisteredActivity.class));

                break;
        }
    }
}
