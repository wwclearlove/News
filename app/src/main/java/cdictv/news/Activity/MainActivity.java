package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cdictv.news.BaseApplication;
import cdictv.news.Been.FragmentBeen;
import cdictv.news.Been.User;
import cdictv.news.R;
import cdictv.news.Utils.saveUtil;
import cdictv.news.fragment.MyFragment;
import cdictv.news.fragment.ViewpagerAdapter;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.mTooalbar)
    Toolbar mMTooalbar;
    @InjectView(R.id.mNavifationVion)
    NavigationView mMNavifationVion;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.tablayout)
    TabLayout mTablayout;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;


    private User userinfo;
    private boolean isExit;
    private int tag=0;
    private SharedPreferences sp;
    protected BaseApplication application;
    //双击退出的 handler
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = true;
        }
    };
    private ImageView HeaderImageView;
    private TextView UserNameText;

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            System.exit(0);
        } else {
          Toast.makeText(getApplicationContext(),"再点击一次就退出应用",Toast.LENGTH_SHORT).show();
            isExit = true;
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }
    private ArrayList<MyFragment> arrayList=new ArrayList<>();
    private ArrayList<FragmentBeen> arrays=new ArrayList<>();
    private ViewpagerAdapter adapter;
    private String titles[]={"热门精选","国内新闻","国际新闻","社会新闻","娱乐新闻","IT资讯",
            "NBA新闻","足球新闻"};
    private int contents[]={R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout
            ,R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout,R.layout.itemlayout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (application == null) {
            application = (BaseApplication) getApplicationContext();
        }
        application.addActivity(this);
        ButterKnife.inject(this);
        mViewPager=findViewById(R.id.viewPager);
        mTablayout=findViewById(R.id.tablayout);
        mViewPager.setOffscreenPageLimit(0);

        saveUtil util=new saveUtil(this,"fragmentList",arrays);
        ArrayList<FragmentBeen> arrlist= util.showlist();
        for (FragmentBeen been : arrlist) {
            Log.d("==", been.getTitle());
        }
        arrays=util.showlist();
        if(arrays.isEmpty()){
            addArr();
        }

        addList(arrays);
        initView(arrayList);
        initNav();

    }

    private void addArr() {
        for (int i = 0; i <titles.length ; i++) {
            FragmentBeen been=new FragmentBeen();
            been.setContent(contents[i]);
            been.setTitle(titles[i]);
            arrays.add(been);
        }

    }

    private void addList( ArrayList<FragmentBeen> arrays) {
        for (FragmentBeen been:arrays) {
            arrayList.add(new MyFragment(been.getTitle(),been.getContent()));
        }
        for (MyFragment been : arrayList) {
            Log.i("arraylist444",been.getTitle());

        }
    }

    private void initView(ArrayList<MyFragment>  arrayList) {
        for (MyFragment been : arrayList) {
            Log.i("arraylist111",been.getTitle());

        }
        adapter=new ViewpagerAdapter(getSupportFragmentManager(),arrayList);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(false, new BackgroundToForegroundTransformer());

        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void initNav() {

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mMTooalbar, 0, 0);
// 添加此句，toolbar左上角显示开启侧边栏图标
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mMNavifationVion.setItemIconTintList(null);
        View headerView = mMNavifationVion.getHeaderView(0);
        initIntentDate(headerView);
        mMNavifationVion.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()){
                    case "我的导航":
//                        Toast.makeText(MainActivity.this, ("--" + item.getTitle().toString()), Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(MainActivity.this,LoveActivity.class);
                        Bundle bundle = new Bundle();
////
                        bundle.putSerializable("list",(Serializable)arrays);
//
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        startActivityForResult(intent,1);
//                        finish();
                        break;
                    case "我的视频":
                        Intent tent=new Intent(MainActivity.this,VidvoActivity.class);
                        startActivity(tent);
                        break;
                    case "我的信息":
                        Intent setintent =new Intent(MainActivity.this,SettingUserActivity.class);
                        setintent.putExtra("data_user",userinfo);
                        startActivity(setintent);
                        break;
                    case "关于我们":
                        Intent meintent =new Intent(MainActivity.this,AboutWeActivity.class);
                        startActivity( meintent);
                        break;
                    default:
                }
//                Toast.makeText(MainActivity.this, (item.getItemId() + "--" + item.getTitle().toString()), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    /**
     * 设置数据的在侧边栏显示
     * @param headerView
     */

    private void initIntentDate(View headerView) {
        UserNameText = headerView.findViewById(R.id.userNameText);
        HeaderImageView = headerView.findViewById(R.id.headerImageView);

        userinfo = (User) getIntent().getSerializableExtra("data_user");
        sp = getSharedPreferences("mimadate",MODE_PRIVATE);
        if(userinfo != null){
            Glide.with(MainActivity.this).load(sp.getString("imageString","")).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new BitmapImageViewTarget(HeaderImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            HeaderImageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            String username = userinfo.getName();
            UserNameText.setText("用户"+username);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("ff","调用");
        if (requestCode==1&&resultCode== 2){
//            Log.d("ff","调用2");
            ArrayList<FragmentBeen>  arr = (ArrayList<FragmentBeen>)data.getSerializableExtra("List");
            //arrays.removeAll(arrays);
            arrays.clear();
            for (FragmentBeen been : arrays) {
                Log.i("ma-arrays",been.getTitle());
                Log.i("ma-arrays","清空");

            }
            for (FragmentBeen been : arr) {
                Log.i("ma-arr",been.getTitle());
                arrays.add(been);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("===","onstart");
        arrayList.clear();
        for (FragmentBeen been : arrays) {
            Log.i("arrays",been.getTitle());

        }
        addList(arrays);
        for (MyFragment been : arrayList) {
            Log.i("arraylist",been.getTitle());

        }
        initView(arrayList);
        initNav();
}

//    @Override
//    protected void onStop() {
//        super.onStop();
//        for (MyFragment been:arrayList) {
//            been.getActivity().finish();
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (application != null) {
            application.removeActivity(this);
        }
    }
}
