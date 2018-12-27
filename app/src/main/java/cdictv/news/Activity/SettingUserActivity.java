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
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.news.JavaBean.User;
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

public class SettingUserActivity extends AppCompatActivity {

    @InjectView(R.id.set_touxian)
    ImageView setTouxian;
    @InjectView(R.id.set_user)
    TextView setUser;
    @InjectView(R.id.set_phone)
    TextView setPhone;
    @InjectView(R.id.male_rb)
    RadioButton maleRb;
    @InjectView(R.id.famale_rb)
    RadioButton famaleRb;
    @InjectView(R.id.sex_rg)
    RadioGroup sexRg;
    @InjectView(R.id.setting_okbt)
    TextView settingOkbt;
    @InjectView(R.id.set_exit)
    TextView setExit;
    @InjectView(R.id.setting_exittologin)
    TextView settingExittologin;

    private User intent;
    private User userset;
    private String nameuser;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");

    private Uri imageUri;
    private Uri cropImageUri;
    private Bitmap cricketbitmap;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String userinfo = (String) msg.obj;
                    Toast.makeText(SettingUserActivity.this, userinfo, Toast.LENGTH_SHORT).show();
                    datatouser();
                    break;
            }
        }
    };

    /**
     * 修改完成的数据传回渲染界面
     * userset 传回修改后的数据
     */

    private void datatouser() {
        setTouxian = findViewById(R.id.set_touxian);
        setUser = findViewById(R.id.set_user);
        setPhone = findViewById(R.id.set_phone);
        maleRb = findViewById(R.id.male_rb);

        Glide.with(SettingUserActivity.this).load(userset.getPhoto()).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new BitmapImageViewTarget(setTouxian) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(SettingUserActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        setTouxian.setImageDrawable(circularBitmapDrawable);
                    }
                });
        setUser.setText(userset.getName());
        setPhone.setText(userset.getTel());
        if (userset.getSex().equals("0")) {
            maleRb.setChecked(true);
        } else if (userset.getSex().equals("1")) {
            famaleRb.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        ButterKnife.inject(this);
        initDateUser();
    }

    /**
     * 调转过来的数据进行对显示用户的信息渲染
     */
    private void initDateUser() {
        intent = (User) getIntent().getSerializableExtra("data_user");
        setTouxian = findViewById(R.id.set_touxian);
        setUser = findViewById(R.id.set_user);
        setPhone = findViewById(R.id.set_phone);
        maleRb = findViewById(R.id.male_rb);
        famaleRb = findViewById(R.id.famale_rb);
        if (intent != null) {
            Glide.with(SettingUserActivity.this).load(intent.getPhoto()).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new BitmapImageViewTarget(setTouxian) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(SettingUserActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            setTouxian.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            setUser.setText(intent.getName());
            setPhone.setText(intent.getTel());

            if (!TextUtils.isEmpty(intent.getSex())) {
                if (intent.getSex().equals("0")) {
                    maleRb.setChecked(true);
                } else if (intent.getSex().equals("1")) {
                    famaleRb.setChecked(true);
                }
            }

        }
    }

    @OnClick({R.id.setting_okbt, R.id.set_exit, R.id.set_touxian,R.id.setting_exittologin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_okbt:
                settingDateuser();
                break;
            case R.id.set_exit:
                Intent intent = new Intent(SettingUserActivity.this, MainActivity.class);
                intent.putExtra("data_user", userset);
                startActivity(intent);
                break;
            case R.id.set_touxian:
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                initPermissions();
                break;
            case R.id.setting_exittologin:
                startActivity(new Intent(SettingUserActivity.this,LoginActivity.class));
                finish();
                break;
        }
    }

    /**
     * 8.0以上的手机获取的权限
     */
    public   void initPermissions() {
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


    /**
     * 修改信息的逻辑
     */
    private void settingDateuser() {
        String phone = setPhone.getText().toString().trim();
        String sex = null;
        userset = new User();
        userset.setName(intent.getName());
        userset.setTel(intent.getTel());
        if (TestUtills.IsHandset(phone)) {
            userset.setTel(phone);
        } else {
            Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
        }
        if (maleRb.isChecked()) {
            //男为1
            sex = "1";
        } else if (famaleRb.isChecked()) {
            //女为0
            sex = "0";
        }
        userset.setSex(sex);
        userset.setSex(intent.getPassword());

        if (fileCropUri.exists()) {
            new File_upload(SettingUserActivity.this, fileCropUri.getPath(), userset);
        }
        userset.setPhoto("https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/news/" + intent.getName() + "Photo.jpg");
        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                String userjson = gson.toJson(userset);
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
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(SettingUserActivity.this, "cdictv.news.cameraalbumtest.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(SettingUserActivity.this, "cdictv.news.cameraalbumtest.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
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
                    Toast.makeText(this, "请允许打操作SDCard！！", Toast.LENGTH_SHORT).show();
                }
                break;
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
        setTouxian.setImageBitmap(cricketbitmap);
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
}
