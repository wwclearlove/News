package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.JavaBean.UserInfo;
import cdictv.news.R;
import cdictv.news.Utils.DBManager;
import cdictv.news.Utils.TestUtills;

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
    @InjectView(R.id.reg_okbt)
    TextView regOkbt;
    @InjectView(R.id.reg_exit)
    TextView regExit;

    List<UserInfo> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.inject(this);

    }

    /**
     * 判断注册信息是否重复，规范
     * 然后写入数据到数据库中
     */
    private void registerInfo() {

        UserInfo user = new UserInfo();

        String username = regUser.getText().toString().trim();
        String phone = regPhone.getText().toString();
        String pawone = regPaw.getText().toString();
        String pawtwo = regPawTwo.getText().toString();
        String yzm = regYzm.getText().toString();

        if(username.equals("")){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else {
            if(pawone.equals("")|| pawtwo.equals("")){
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            }else {
                if(phone.equals("")){
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }else {

                    //通过嵌套判断 可以一次性对数据进行添加到数据库中 免去分部分添加

                    //Log.i("reg",TestUtills.IsUserId(username)+"");

                    if (!TestUtills.IsUserId(username)) {
                        Toast.makeText(this, "用户名开头必须为字母", Toast.LENGTH_SHORT).show();
                        Log.i("---------", "registerInfo: "+TestUtills.IsUserId(username));
                    } else {
                        if (!TestUtills.IsHandset(phone)) {
                            Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!TestUtills.IsPassword(pawone)) {
                                Toast.makeText(this, "请输入6—11位密码", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!pawone.equals(pawtwo)) {
                                    Toast.makeText(this, "密码两次输入不一致", Toast.LENGTH_SHORT).show();
                                } else {
                                    if(!TestUtills.IsCodeNmb(yzm)){
                                        Toast.makeText(this, "验证码输入为4位数", Toast.LENGTH_SHORT);
                                    }else {
                                        user.setName(username);
                                        user.setPassword(pawtwo);
                                        user.setPhone(phone);
                                        if(user.save()){
                                            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisteredActivity.this,LoginActivity.class));
                                        }else {
                                            Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }


    }

    @OnClick({R.id.reg_code,R.id.reg_okbt, R.id.reg_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_code:
                break;
            case R.id.reg_okbt:
                registerInfo();
                break;
            case R.id.reg_exit:
                startActivity(new Intent(RegisteredActivity.this,LoginActivity.class));
                finish();
                break;
        }
    }


}
