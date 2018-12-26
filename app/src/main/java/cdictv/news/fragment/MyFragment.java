package cdictv.news.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cdictv.news.Activity.ContentActivity;
import cdictv.news.Adapter.MessageAdapter;
import cdictv.news.Been.MessBeen;
import cdictv.news.R;
import cdictv.news.Utils.GlideImageLoader;
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
    private List<MessBeen.NewslistBean> zymlist;
    private RecyclerView mRv;
    private MessageAdapter mMessageAdapter;
    private String uri;
    private String duri;
    RefreshLayout refreshLayout;
    public String getTitle() {
        return title;
    }

    public int getContent() {
        return content;
    }

    @SuppressLint("ValidFragment")
    public MyFragment(String title,int layout) {
        super();
        Log.d("title1",title);
        this.title = title;
        Log.d("title2", this.title);
        this.content = layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        uri="http://api.tianapi.com/guonei/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
        duri="http://api.tianapi.com/wxnew/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("==","没到");
        final View inflate = inflater.inflate(content, null);
        if(content==R.layout.itemlayout){

        mRv=inflate.findViewById(R.id.rv);
        mBanner = inflate.findViewById(R.id.banner);
        refreshLayout =inflate.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
            Log.d("title",title);
        switch(title){
            case "热门精选":
                uri="http://api.tianapi.com/wxnew/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                break;
            case "国内新闻":
                duri="http://api.tianapi.com/guonei/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
             break;
            case "国际新闻":
                uri="http://api.tianapi.com/world/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/world/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;

            case "社会新闻":
                uri="http://api.tianapi.com/social/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/social/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "足球新闻":
                uri="http://api.tianapi.com/football/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/football/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "娱乐新闻":
                uri="http://api.tianapi.com/huabian/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/huabian/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;

            case "IT资讯":
                uri="http://api.tianapi.com/it/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/it/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "NBA新闻":
                uri="http://api.tianapi.com/nba/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/nba/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "移动互联":
                uri="http://api.tianapi.com/mobile/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/mobile/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "健康知识":
                uri="http://api.tianapi.com/health/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/health/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "科技新闻":
                uri="http://api.tianapi.com/keji/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/keji/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "体育新闻":
                uri="http://api.tianapi.com/tiyu/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/tiyu/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "创业新闻":
                uri="http://api.tianapi.com/startup/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/startup/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "苹果新闻":
                uri="http://api.tianapi.com/apple/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/apple/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "军事新闻":
                uri="http://api.tianapi.com/military/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/military/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            case "旅游资讯":
                uri="http://api.tianapi.com/travel/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/travel/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "奇闻异事":
                uri="http://api.tianapi.com/qiwen/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/qiwen/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";

                break;
            case "人工智能":
                uri="http://api.tianapi.com/ai/?key=6edb9118c6ce61c710140aeb03b10e2c&num=50&rand=1";
                duri="http://api.tianapi.com/ai/?key=6edb9118c6ce61c710140aeb03b10e2c&num=5&rand=1";
                break;
            default:
        }
        getzhuyeData(duri);
        onLondBanner();
        getData(uri);
        refreshLayout.autoRefresh();
      }

        return inflate;
    }

    private void onLondBanner() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData(uri);
                getzhuyeData(duri);
                refreshLayout.finishRefresh();
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getData(uri);
                getzhuyeData(duri);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                },1000);

            }
        });
    }

    private void getData(String uri) {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(uri);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRv();
                        }
                    });
                }

            }
        });

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

                Intent intent=new Intent(getContext(), ContentActivity.class);
                intent.putExtra("title",mlist.get(position).getTitle());
                intent.putExtra("uri",mlist.get(position).getUrl());
//                intent.setAction("android.intent.action.VIEW");
//                intent.setData(Uri.parse(mlist.get(position).getUrl()));
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
    private void getzhuyeData( String duri) {
        Log.d("++","进入后");
        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(duri);
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= mOkHttpClient.newCall(request);
        Log.d("==","jiaz");
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
                    String data = response.body().string();
                    MessBeen resultBean = new Gson().fromJson(data,MessBeen.class);
                    //对象中拿到集合
                    zymlist = resultBean.getNewslist();
//                    Log.i("===1111", zymlist.toString());
//                    for (MessBeen.NewslistBean been: zymlist) {
//                        Log.d("===111",been.getTitle());
//                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initBanner();
                        }
                    });

                }

            }
        });
//        Log.d("===222","++");
//        Log.d("===222",zymlist.toString());


    }

    private void initBanner() {
//        Log.d("===333",zymlist.toString());
//        for (MessBeen.NewslistBean been: zymlist) {
//            Log.d("+++",been.getTitle());
//
//        }
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<String> imgs=new ArrayList<>();
        List<String> titles = new ArrayList<>();
//        mBanner.setImages();
        for (int i = 0; i < zymlist.size(); i++) {
//t
           imgs.add(zymlist.get(i).getPicUrl());
           titles.add(zymlist.get(i).getTitle());
        }
        mBanner.setImages(imgs);
        mBanner.setBannerTitles(titles);
        mBanner.setBannerAnimation(Transformer.RotateDown);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
      mBanner.setOnBannerListener(new OnBannerListener() {
          @Override
          public void OnBannerClick(int position) {
              Intent intent=new Intent(getContext(), ContentActivity.class);
              intent.putExtra("title",zymlist.get(position).getTitle());
              intent.putExtra("uri",zymlist.get(position).getUrl());
              startActivity(intent);
      }
      });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}


