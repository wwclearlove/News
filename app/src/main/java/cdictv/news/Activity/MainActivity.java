package cdictv.news.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cdictv.news.R;
import cdictv.news.Utils.GlideImageLoader;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.banner)
    Banner mBanner;
    @InjectView(R.id.mTooalbar)
    Toolbar mMTooalbar;
    @InjectView(R.id.mNavifationVion)
    NavigationView mMNavifationVion;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mBanner = findViewById(R.id.banner);
        initBanner();
        initNav();
    }

    private void initNav() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mMTooalbar, 0, 0);
// 添加此句，toolbar左上角显示开启侧边栏图标
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mMNavifationVion.setItemIconTintList(null);
        mMNavifationVion.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, (item.getItemId() + "--" + item.getTitle().toString()), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    private void initBanner() {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        Integer[] images = {R.drawable.uoko_guide_background_1, R.drawable.uoko_guide_background_2, R.drawable.uoko_guide_background_3};
        mBanner.setImages(Arrays.asList(images));
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.RotateDown);
        //设置标题集合（当banner样式有显示title时）
        List<String> titles = new ArrayList<>();
        titles.add("第一个标题");
        titles.add("第二个标题");
        titles.add("第三个标题");
        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }


}
