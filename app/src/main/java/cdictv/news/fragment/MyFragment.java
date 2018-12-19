package cdictv.news.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cdictv.news.Adapter.MessageAdapter;
import cdictv.news.Been.MessBeen;
import cdictv.news.R;
import cdictv.news.Utils.GlideImageLoader;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {
   private Banner mBanner;
    private Handler handler = new Handler();
    private final String title;
    private final int content;
    private Context context;
    private View inflate = null;
    private String data;
    private List<MessBeen.NewslistBean> mlist;
    private RecyclerView mRv;
    private MessageAdapter mMessageAdapter;
    public String getTitle() {
        return title;
    }

    public int getContent() {
        return content;
    }

    @SuppressLint("ValidFragment")
    public MyFragment(String title,int layout) {
        super();
        this.title = title;
        this.content = layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View inflate = inflater.inflate(content, null);
        if(content== R.layout.itemlayout){
            mBanner = inflate.findViewById(R.id.banner);
            Log.i("==",mBanner+"");
            initBanner();
        }
        if(content==R.layout.itemtwolayout) {
            mRv=inflate.findViewById(R.id.rv);
            final WaveSwipeRefreshLayout mWaveSwipeRefreshLayout = inflate.findViewById(R.id.wave);
            //设置中间小圆从白色到黑色
            mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.BLACK);
            //设置整体的颜色
            mWaveSwipeRefreshLayout.setWaveColor(Color.argb(255, 92, 227, 248));
            //下拉刷新
            mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            getData();
//                            initRv();
                            //三秒后停止刷新
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(inflate.getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
                        }
                    },3000);
                }
            });

            getData();

        }

        return inflate;
    }

    private void getData() {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("https://api.tianapi.com/social/?key=\n" +
                "6edb9118c6ce61c710140aeb03b10e2c\n&num=50&rand=1");
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("===", "cache---" + str);
                } else {
                    data = response.body().string();
                    MessBeen resultBean = new Gson().fromJson(data,MessBeen.class);
                    //对象中拿到集合
                    mlist = resultBean.getNewslist();

//                    mlist= gson.fromJson(data,new TypeToken<List<MessBeen>>(){}.getType());
//                    for (MessBeen.NewslistBean been: mlist) {
//                        Log.d("==",been.getPicUrl()+"");
//                        Log.d("==",been.getTitle()+"");
//                        Log.d("==",been.getDescription()+"");
//                    }
                }

            }
        });
        initRv();
    }
    private void initRv() {
        //设置recyclerview的布局样式（从上往下，还是从左往右，还是瀑布流）从左往右LinearLayoutManager.HORIZONTAL
        mRv.setLayoutManager(new LinearLayoutManager(context));//默认表示从上往下

        mMessageAdapter=new MessageAdapter(R.layout.item_message,mlist);

        //设置动画
        mMessageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //如果设置成true,动画只加载一次
        mMessageAdapter.isFirstOnly(false);
        //设置点击事件
        //设置点击事件
        mMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort(position+"");
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(mlist.get(position).getUrl()));
                startActivity(intent);
            }
        });
        mRv.setAdapter(mMessageAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //textView.setText(content);
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
