package cdictv.news.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.Been.User;
import cdictv.news.R;
import cdictv.news.Utils.File_upload;
import cdictv.news.Utils.PhotoUtils;
import cdictv.news.Utils.TestUtills;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    @InjectView(R.id.reg_touxian)
    ImageView regTouxian;
    @InjectView(R.id.male_reg)
    RadioButton maleReg;
    @InjectView(R.id.famale_reg)
    RadioButton famaleReg;
    @InjectView(R.id.reg_exit)
    ImageView mRegExit;
    @InjectView(R.id.sex_rg)
    RadioGroup mSexRg;

    //    private List<UserInfo> userInfos;
    private Thread thread;
    private String code;
    public static final String PACKAGE_NAME = "cdictv.news";
    public static final String FILE_PATH = "/data";
    private static final int USER_MIMA = 0x05;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private User user;
    private Bitmap cricketbitmap;
    private long timenum = 0;


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
                case 2:
                    User user1 = (User) msg.obj;
                    if (user1 != null) {
                        Intent intent = new Intent(RegisteredActivity.this, LoginActivity.class);

                        intent.putExtra("data_user", user1);
                        if (fileCropUri.exists()) {
                            File_upload file_upload = new File_upload(RegisteredActivity.this, fileCropUri.getPath(), user1);
                        }
                        Toast.makeText(RegisteredActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        //startActivityForResult(intent,USER_MIMA);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisteredActivity.this, "用户已存在！！！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    User usertwo = (User) msg.obj;

                    if (usertwo != null) {
                        Toast.makeText(RegisteredActivity.this, "电话已存在！！！", Toast.LENGTH_SHORT).show();
                    }
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

        final String username = regUser.getText().toString().trim();
        String phone = regPhone.getText().toString();
        String pawone = regPaw.getText().toString();
        final String pawtwo = regPawTwo.getText().toString();
        final String yzm = regYzm.getText().toString();


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
                                        Toast.makeText(RegisteredActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (maleReg.isChecked() || famaleReg.isChecked()) {
                                            user = new User();
                                            user.setName(username);
                                            user.setPassword(pawtwo);
                                            user.setTel(phone);
                                            user.setPhoto("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/" + user.getName() + "Photo.jpg");
                                            String sex = null;
                                            if (maleReg.isChecked()) {
                                                //男为1
                                                sex = "1";
                                            } else if (famaleReg.isChecked()) {
                                                //女为0
                                                sex = "0";
                                            }
                                            user.setSex(sex);
                                            if (code.equals(yzm)) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Gson gson = new Gson();
                                                        String userjson = gson.toJson(user);
                                                        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                                                .connectTimeout(10, TimeUnit.SECONDS)
                                                                .writeTimeout(10, TimeUnit.SECONDS)
                                                                .readTimeout(10, TimeUnit.SECONDS)
                                                                .build();

                                                        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), userjson);

                                                        Request request = new Request.Builder()
                                                                .url("http://134.175.154.154/new/api/news/regist")
                                                                .post(requestBody)
                                                                .build();

                                                        try {
                                                            Response response = okHttpClient.newCall(request).execute();
                                                            User userinfo = gson.fromJson(response.body().string(), User.class);
                                                            Message mag = new Message();
                                                            mag.what = 2;
                                                            mag.obj = userinfo;
                                                            handler.sendMessage(mag);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (NullPointerException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            } else {
                                                Toast.makeText(RegisteredActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(RegisteredActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
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


    @OnClick({R.id.reg_code, R.id.reg_okbt, R.id.reg_exit, R.id.reg_touxian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_code:
                initCode();
                break;
            case R.id.reg_okbt:
                handler.removeCallbacks(thread);
                registerInfo();
                break;
            case R.id.reg_exit:
                startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.reg_touxian:
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                initPermissions();
                break;
        }
    }


    public void initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
//              preferencesUtility.setString("storage", "true");
            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
//              preferencesUtility.setString("storage", "true");
            }
            if (!permissions.isEmpty()) {
//              requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                        STORAGE_PERMISSIONS_REQUEST_CODE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, 150, 150, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "cdictv.news.cameraalbumtest.fileProvider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, 150, 150, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }

    /**
     * 展示在界面上的方法
     *
     * @param bitmap
     */
    private void showImages(Bitmap bitmap) {
        cricketbitmap = PhotoUtils.toRoundBitmap(bitmap);
        Log.i("showImages", "showImages: " + fileCropUri.toString());
        regTouxian.setImageBitmap(cricketbitmap);
    }

    /**
     * 保存图片进入数据库
     *
     * @param cricketbitmap_capy 返回Bitmap的参数 存入图片
     */
//    private void saveToImages(Bitmap cricketbitmap_capy) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        cricketbitmap_capy.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] imges = baos.toByteArray();
//        UserInfo userInfo = DataSupport.findFirst(UserInfo.class);
//        userInfo.setHeadimg(imges);
//        userInfo.save();
//    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {
            //有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(RegisteredActivity.this, "cdictv.news.cameraalbumtest.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
            }
            PhotoUtils.openPic(this, CODE_RESULT_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
//                imageUri = Uri.fromFile(fileUri);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    imageUri = FileProvider.getUriForFile(RegisteredActivity.this, "cdictv.news.cameraalbumtest.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
//                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(RegisteredActivity.this, "cdictv.news.cameraalbumtest.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请允许打开相机！！", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    //Toast.makeText(this, "请允许打操作SDCard！！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取验证码 发送短信 按钮时间更改
     */
    private void initCode() {
        final String phone = regPhone.getText().toString();
        final String username = regUser.getText().toString();
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
                            OkHttpClient okHttpuser = new OkHttpClient.Builder()
                                    .connectTimeout(10, TimeUnit.SECONDS)
                                    .writeTimeout(10, TimeUnit.SECONDS)
                                    .readTimeout(10, TimeUnit.SECONDS)
                                    .build();

                            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), userjson);

                            final Request request2 = new Request.Builder()
                                    .url("http://134.175.154.154/new/api/news/login")
                                    .post(requestBody)
                                    .build();


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
                                //获取电话的是否注册
                                Response response2 = okHttpuser.newCall(request2).execute();
                                User user1 = gson.fromJson(response2.body().string(), User.class);
                                Message mag = new Message();
                                mag.what = 3;
                                mag.obj = user1;
                                handler.sendMessage(mag);

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

                timenum = 60;
                //开启手机验证码按钮倒计时
                thread = new Thread(new Runnable() {
                    //设置时间的的长度


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
            } else {
                Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
        }

    }
}
