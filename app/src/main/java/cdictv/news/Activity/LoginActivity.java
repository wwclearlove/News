package cdictv.news.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.JavaBean.UserInfo;
import cdictv.news.R;
import cdictv.news.Utils.DBManager;
import cdictv.news.Utils.TestUtills;

public class LoginActivity extends Activity {

    @InjectView(R.id.login_user)
    EditText loginUser;
    @InjectView(R.id.login_password)
    EditText loginPassword;
    @InjectView(R.id.bt_login)
    TextView btLogin;
    @InjectView(R.id.bt_resgis)
    TextView btResgis;
    @InjectView(R.id.img_touxian)
    ImageView imgTouxian;
    @InjectView(R.id.login_mima)
    CheckBox loginMima;
    @InjectView(R.id.login_wagnji)
    TextView loginWagnji;

    private boolean falg = true;
    private List<UserInfo> userId;
    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDBManger();
        userInfo = new UserInfo();
        ButterKnife.inject(this);
    }

    /**
     * 在登录界面导入数据库
     */
    private void initDBManger() {
        DBManager dbManager = new DBManager(this);
        dbManager.openDatabase();
        dbManager.closeDatabase();
    }

    @OnClick({R.id.bt_login, R.id.bt_resgis, R.id.img_touxian,R.id.login_mima,R.id.login_wagnji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                checkOut();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.bt_resgis:
                startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));
                finish();
                break;
            case R.id.img_touxian:
                break;
            case R.id.login_mima:
                break;
            case R.id.login_wagnji:
                break;
        }
    }

    private void initJiZhuMiMa(String username,String paw) {
        SharedPreferences.Editor editor = getSharedPreferences("mimadate",MODE_PRIVATE).edit();
        if(!falg){
            editor.putString("username",username);
            editor.putString("paw",paw);
        }else {
            SharedPreferences sp = getSharedPreferences("mimadate",MODE_PRIVATE);
            loginUser.setText(sp.getString("username",""));
            loginPassword.setText(sp.getString("paw",""));
        }

    }

    /**
     * 验证输入 用户名 密码 是否规范
     */
    private void checkOut() {
        String username = loginUser.getText().toString().trim();
        String paw = loginPassword.getText().toString();
        if (username.equals("")) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (paw.equals("")) {
                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if (!TestUtills.IsUserId(username)) {
                    Toast.makeText(LoginActivity.this, "用户名请以字母开头", Toast.LENGTH_SHORT).show();
                }
                if (!TestUtills.IsPassword(paw)) {
                    Toast.makeText(LoginActivity.this, "请输入6-11位密码", Toast.LENGTH_SHORT).show();
                }
            }
        }
        selectLog(username, paw);
    }

    /**
     * 查询用户的信息来进行判断信息是否存在
     * 用户的密码是否正确
     *
     * @param username 输入的用户名 或手机号
     * @param paw      输入的密码
     */
    private void selectLog(String username, String paw) {

        //通过正则表达式来比较电话号码 来查询密码
        if (TestUtills.IsHandset(username)) {
            userId = DataSupport.where("phone=?", username).find(UserInfo.class);
        } else {
            userId = DataSupport.where("name=?", username).find(UserInfo.class);
        }

        if (userId != null) {
            if (userId.get(0).getPassword().equals(paw)) {
                if(loginMima.isChecked()){
                    initJiZhuMiMa(username,paw);
                }

                //转跳到新闻界面

            } else {
                Toast.makeText(LoginActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
        }
    }
}
