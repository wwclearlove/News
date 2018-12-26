package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.JavaBean.User;
import cdictv.news.R;
import cdictv.news.Utils.DBManager;
import cdictv.news.Utils.File_upload;
import cdictv.news.Utils.TestUtills;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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


    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static final int USER_MIMA = 0x05;
    private Bitmap touxianimg;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.i("handleMessage", "handleMessage: "+"=======");
                    try {
                        User user2 = (User) msg.obj;
                        if(user2 != null){
                            Glide.with(LoginActivity.this).load("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/"+user2
                            .getPhoto()).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgTouxian) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(LoginActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imgTouxian.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                            intent.putExtra("pawtwo",user2.getPassword());
//                            intent.putExtra("username",user2.getName());
//                            intent.putExtra("imageString",user2.getPhoto());
                            intent.putExtra("data_user",user2);
                            //intent.putExtra("photo",user.getName()+"Photo.jpg");
                            initJiZhuMiMa(user2.getName(),user2.getPassword(),user2.getPhoto());
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "密码或用户名错误！", Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){
                            Toast.makeText(LoginActivity.this, "密码或用户名错误！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initDBManger();
        initMiMa();
        initIntentDate();
        ButterKnife.inject(this);
    }

    /**
     * 在注册时完成是把密码回传回来显示在界面上
     */
    private void initIntentDate() {
        loginUser = findViewById(R.id.login_user);
        loginPassword = findViewById(R.id.login_password);
        imgTouxian = findViewById(R.id.img_touxian);

        User intent = (User) getIntent().getSerializableExtra("data_user");
        if(intent != null){
            Glide.with(LoginActivity.this).load("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/"+
                    intent.getPhoto()).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgTouxian) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(LoginActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imgTouxian.setImageDrawable(circularBitmapDrawable);
                }
            });
            loginUser.setText(intent.getName());
            loginPassword.setText(intent.getPassword());
        }
    }

    /**
     * 在登录界面导入数据库 (废弃)
     */
    private void initDBManger() {
        DBManager dbManager = new DBManager(this);
        dbManager.openDatabase();
        dbManager.closeDatabase();
    }

    @OnClick({R.id.bt_login, R.id.bt_resgis, R.id.img_touxian,R.id.login_wagnji,R.id.login_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                checkOut();
                break;
            case R.id.bt_resgis:
                startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));
                finish();
                break;
            case R.id.login_wagnji:
                startActivity(new Intent(LoginActivity.this,FindMimaActivity.class));
                break;
            case R.id.login_password:
                //在输入完用户名后 获取图片
                if(!TextUtils.isEmpty(loginUser.getText().toString())){
                    Glide.with(LoginActivity.this).load("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/"+
                            loginUser.getText().toString().trim()+"Photo.jpg").asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgTouxian) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(LoginActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgTouxian.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                break;
        }
    }


    /**
     * 通过loginMima.isChecked()的点击开启时
     * 来对SharedPreferences里写上数据
     * @param username
     * @param paw
     *
     *
     */
    private void initJiZhuMiMa(String username, String paw,String imageString) {
        editor = getSharedPreferences("mimadate",MODE_PRIVATE).edit();
        //如果勾选CheckBox的选项 把数据写入SharedPreferences中 没有则在SharedPreferences的Boolean中写入false中
        if(loginMima.isChecked()){
            editor.putString("username",username);
            editor.putString("paw",paw);
            editor.putString("imageString",imageString);
            editor.putBoolean("falg",true);
            editor.apply();
        }else {
            editor.putBoolean("falg",false);
            editor.apply();
        }
    }

    /**
     * 验证输入 用户名 密码 是否规范
     * 调用 验证用户信息登录是否有 或无
     */
    private void checkOut() {
        //Log.i("userlog", "initMiMa: "+loginUser);
        String username = loginUser.getText().toString().trim();
        String paw = loginPassword.getText().toString();
        //selectLog(username,paw);
        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (paw.isEmpty()) {
                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if(TestUtills.IsHandset(username)){
                    if(TestUtills.IsPassword(paw)){
                        selectLog(username,paw);
                    }else {
                        Toast.makeText(LoginActivity.this, "请输入6-11位密码", Toast.LENGTH_SHORT).show();
                    }
                }else if (!TestUtills.IsUserId(username)) {
                    Toast.makeText(LoginActivity.this, "用户名请以字母开头", Toast.LENGTH_SHORT).show();
                }else if (!TestUtills.IsPassword(paw)) {
                    Toast.makeText(LoginActivity.this, "请输入6-11位密码", Toast.LENGTH_SHORT).show();
                }else {
                    selectLog(username,paw);
                }
            }
        }

    }

    //这个回调方法好像没啥用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            switch (requestCode){
                case USER_MIMA:
                     loginUser = findViewById(R.id.login_user);
                     loginPassword = findViewById(R.id.login_password);
                     imgTouxian = findViewById(R.id.img_touxian);
                    if(data != null){
                        Bundle bundle = data.getExtras();
                        Glide.with(LoginActivity.this).load("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/"+
                                bundle.getString("photo")).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgTouxian) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(LoginActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imgTouxian.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        String pawtwo = bundle.getString("pawtwo");
                        String username = bundle.getString("username");
                        loginUser.setText(username);
                        loginPassword.setText(pawtwo);

                    }
                    break;
            }
        }
    }

    /**
     * 查询用户的信息来进行判断信息是否存在
     * 用户的密码是否正确
     *
     * @param username 输入的用户名 或手机号
     * @param paw      输入的密码
     */
    private void selectLog(String username, String paw) {
        //通过服务器拿到带数据的方法
         final User user = new User();
         //通过输入的是否是电话号码来验证登录 不过服务器还没写好
//        if (TestUtills.IsHandset(username)) {
//            user.setTel(username);
//        }
        user.setName(username);
        user.setPassword(paw);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Gson gson = new Gson();
                String userjson = gson.toJson(user);
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10,TimeUnit.SECONDS)
                        .readTimeout(10,TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),userjson);

                final Request request = new Request.Builder()
                        .url("http://134.175.154.154/new/api/news/login")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    User user1 = gson.fromJson(response.body().string(),User.class);
                    //通过Handel来传输数据
                    Message mag = new Message();
                    mag.what = 1;
                    mag.obj = user1;
                    handler.sendMessage(mag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 在重新打开界面时通过读取SharedPreferences里的
     * 数据的来进行 实现密码和用户名显示在界面上的功能
     */
    private void initMiMa() {
        loginUser = findViewById(R.id.login_user);
        loginPassword = findViewById(R.id.login_password);
        loginMima = findViewById(R.id.login_mima);
        imgTouxian = findViewById(R.id.img_touxian);
        sp = getSharedPreferences("mimadate",MODE_PRIVATE);

        String userlog = sp.getString("username","");
        String pawlog = sp.getString("paw","");
        String imgString = sp.getString("imageString","");
        boolean falg  = sp.getBoolean("falg",false);

        //通过读取SharedPreferences里面的数据来判断是否勾选CheckBox的选项 来设置数据
        if(falg){
            loginUser.setText(userlog);
            loginPassword.setText(pawlog);
            loginMima.setChecked(falg);
            Log.i("11111111", "initMiMa: "+imgString);
            Glide.with(LoginActivity.this).load("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/"+imgString).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgTouxian) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(LoginActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imgTouxian.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

}
