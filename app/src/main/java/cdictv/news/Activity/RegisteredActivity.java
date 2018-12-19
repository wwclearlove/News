package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.JavaBean.UserInfo;
import cdictv.news.R;
import cdictv.news.Utils.TestUtills;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    @InjectView(R.id.reg_touxian)
    ImageView regTouxian;

    private List<UserInfo> userInfos;
    private Thread thread;
    private String code;
    public static final int CHOOSE_PHONE = 2;
    //private long timenum = 3;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    regCode.setBackgroundResource(R.drawable.regis_but);
                    // regCode.setBackgroundColor(R.color.colorPrimaryDark);
                    handler.removeCallbacks(thread);
                    Log.i("TAG", "handleMessage: " + thread);
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

        String username = regUser.getText().toString().trim();
        String phone = regPhone.getText().toString();
        String pawone = regPaw.getText().toString();
        String pawtwo = regPawTwo.getText().toString();
        String yzm = regYzm.getText().toString();

        boolean ok_login = TestUtills.initCheckUser(username, pawone, pawtwo, phone, code, yzm, this);
        if (ok_login) {
            startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
        }

    }


    @OnClick({R.id.reg_code, R.id.reg_okbt, R.id.reg_exit,R.id.reg_touxian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_code:
                initCode();
                break;
            case R.id.reg_okbt:
                registerInfo();
                break;
            case R.id.reg_exit:
                startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.reg_touxian:
                //打开相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/jpeg");
                startActivityForResult(intent,CHOOSE_PHONE);

                break;
        }
    }

    /**
     * 获取验证码 发送短信 按钮时间更改
     */

    private void initCode() {
        final String phone = regPhone.getText().toString();
        if(!phone.isEmpty()){
            //获取随机数生成验证码
            code = new String();
            for (int i = 0; i < 4; i++) {
                Integer m = new Random().nextInt(10);
                code += m.toString();
            }

            //Log.i("=======", "initCode: "+code);

            final String finalCode = code;
            //通过OKhttp来同步的实施post请求操作
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder()
                            .add("mobile", phone)
                            .add("tpl_id", "116752")
                            .add("key", " 3647810c018a3b9c4c17b23c53aa8c4d")
                            .add("tpl_value", "%23code%23%3D" + finalCode)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://v.juhe.cn/sms/send")
                            .post(formBody)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String result = response.body().string();
                        Log.i("result", "initCode: " + result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            //开启手机验证码按钮倒计时
            thread = new Thread(new Runnable() {
                long timenum = 10;

                @SuppressLint("ResourceAsColor")
                @Override
                public void run() {
                    timenum--;
                    synchronized (this) {
                        if (timenum > 0) {
                            regCode.setBackgroundColor(R.color.lin);
                            regCode.setText(TestUtills.formatLongToTimeStr(timenum));
                            regCode.setClickable(false);
                            handler.postDelayed(this, 1000);
                        } else {
                            regCode.setClickable(true);
                            regCode.setText("再次获取");
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            });
            handler.post(thread);
        }else {
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
        }

    }
}
