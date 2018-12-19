package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    private List<UserInfo> userInfos;
    private Thread thread;
    //private long timenum = 3;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    regCode.setBackgroundResource(R.drawable.regis_but);
                   // regCode.setBackgroundColor(R.color.colorPrimaryDark);
                    handler.removeCallbacks(thread);
                    Log.i("TAG", "handleMessage: "+thread);
                    break;
            }
        }
    };

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

        if(username.isEmpty()){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else {
            if(pawone.isEmpty()|| pawtwo.isEmpty()){
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
//                                    if(!TestUtills.IsCodeNmb(yzm)){
//                                        Toast.makeText(this, "验证码输入为4位数", Toast.LENGTH_SHORT);
//                                    }else {
//
//                                    }
                                    List<UserInfo> infoList =  DataSupport.where("phone=?", phone).find(UserInfo.class);
                                     String phoneone = infoList.get(0).getPhone();
                                    if(!phoneone.equals(phone)){
                                        user.setName(username);
                                        user.setPassword(pawtwo);
                                        user.setPhone(phone);
                                        if(user.save()){
                                            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisteredActivity.this,LoginActivity.class));
                                        }else {
                                            Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(this, "电话号码已被注册", Toast.LENGTH_SHORT).show();
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
                initCode();
//                thread.start();
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

    private void initCode() {




      thread = new Thread(new Runnable() {
          long timenum = 10;
          @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                timenum--;
                synchronized (this){
                    if(timenum>0){
                        regCode.setBackgroundColor(R.color.lin);
                        regCode.setText(TestUtills.formatLongToTimeStr(timenum));
                        regCode.setClickable(false);
                        handler.postDelayed(this,1000);
                    }else {
                        Log.i("thread", "run: 11111111111111111111111");
                        regCode.setClickable(true);
                        regCode.setText("再次获取");
                        Message message = new Message();
                        message.what=1;
                        handler.sendMessage(message);

                    }
                }
            }
        });
      handler.post(thread);
    }


}
