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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.Been.User;
import cdictv.news.R;
import cdictv.news.Utils.TestUtills;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindMimaActivity extends AppCompatActivity {

    @InjectView(R.id.find_user)
    EditText findUser;
    @InjectView(R.id.find_phone)
    EditText findPhone;
    @InjectView(R.id.find_yzm)
    EditText findYzm;
    @InjectView(R.id.find_code)
    TextView findCode;
    @InjectView(R.id.find_paw)
    EditText findPaw;
    @InjectView(R.id.find_paw_two)
    EditText findPawTwo;
    @InjectView(R.id.find_okbt)
    TextView findOkbt;
    @InjectView(R.id.find_exit)
    TextView findExit;

    private boolean falg = true;
    private User user;
    private Thread thread;
    private String code;
    private long timenum = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String date = (String)msg.obj;
                    Toast.makeText(FindMimaActivity.this,date,Toast.LENGTH_SHORT).show();
                    if(date.equals("修改成功")){
                        finish();
                    }
                    break;
                case 2:
                    findCode.setBackgroundResource(R.drawable.regis_but);
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
        setContentView(R.layout.activity_find_mima);
        ButterKnife.inject(this);

    }



    @OnClick({R.id.find_okbt, R.id.find_exit,R.id.find_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_okbt:
                if(!thread.isInterrupted()){
                    handler.removeCallbacks(thread);
                }
                toFindData();
                break;
            case R.id.find_exit:
                startActivity(new Intent(FindMimaActivity.this,LoginActivity.class));
                break;
            case R.id.find_code:
                initCode();
                break;
        }
    }

    private void toFindData() {

        String username = findUser.getText().toString();
        String pawone = findPaw.getText().toString();
        String yzm = findYzm.getText().toString();
        String pawtwo = findPawTwo.getText().toString();
        String phone = findPhone.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (pawone.isEmpty() || pawtwo.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if (phone.equals("")) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //通过嵌套判断 可以一次性对数据进行添加到数据库中 免去分部分添加
                    if (!TestUtills.IsUserId(username)) {
                        Toast.makeText(this, "用户名开头必须为字母", Toast.LENGTH_SHORT).show();
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
                                    if (yzm.isEmpty()) {
                                        Toast.makeText(FindMimaActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                                    } else {
                                        user = new User();
                                        user.setName(username);
                                        user.setPassword(pawtwo);
                                        user.setTel(phone);
                                        initGetDate();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 从网上获取数据后返回String对象 显示是否修改成功
     *
     */

    private void initGetDate(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                Log.i("initGetDate", "run: "+user.getPassword()+user.getTel()+user.getPhoto());
                String userjson = gson.toJson(user);
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), userjson);

                Request request = new Request.Builder()
                        .url("http://134.175.154.154/new/api/news/updatepsd")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String date = response.body().string();
                    Message mag = new Message();
                    mag.what = 1;
                    mag.obj = date;
                    handler.sendMessage(mag);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 获取验证码 发送短信 按钮时间更改
     */
    private void initCode() {
        final String phone = findPhone.getText().toString();
        final String username = findUser.getText().toString();
        if (!phone.isEmpty()) {
            if (TestUtills.IsHandset(phone)) {
                //获取随机数生成验证码
                code = new String();
                for (int i = 0; i < 4; i++) {
                    Integer m = new Random().nextInt(9);
                    code += m.toString();
                }
                Log.i("Codemama", "initCode: " + code);

                final String finalCode = code;
                //通过OKhttp来同步的实施post请求操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User userphone = new User();
                        userphone.setPhoto(phone);
                        userphone.setTel(username);
                        final Gson gson = new Gson();
                        String userjson = gson.toJson(userphone);
                        if (!phone.isEmpty()) {
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
                        } else {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();

                //开启手机验证码按钮倒计时
                timenum = 60;
                thread = new Thread(new Runnable() {
                    //设置时间的的长度
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {
                        timenum--;
                        synchronized (this) {
                            if (timenum > 0) {
                                findCode.setBackgroundColor(R.color.lin);
                                findCode.setText(TestUtills.formatLongToTimeStr(timenum));
                                findCode.setClickable(false);
                                handler.postDelayed(this, 1000);
                            } else {
                                findCode.setClickable(true);
                                findCode.setText("再次获取");
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                    }
                });
                handler.post(thread);
            } else {
                Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
        }

    }

    }









