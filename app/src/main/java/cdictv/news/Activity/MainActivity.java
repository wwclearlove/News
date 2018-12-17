package cdictv.news.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cdictv.news.R;
import cn.bingoogolapple.bgabanner.BGABanner;

public class MainActivity extends AppCompatActivity {
    BGABanner mBGABanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBGABanner= findViewById(R.id.banner_indicatorId);
    }
}
