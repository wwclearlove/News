package cdictv.news.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cdictv.news.Adapter.BaseRecAdapter;
import cdictv.news.Adapter.BaseRecViewHolder;
import cdictv.news.Been.Douyin;
import cdictv.news.R;
import cdictv.news.widget.MyVideoPlayer;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VidvoActivity extends AppCompatActivity {

    @InjectView(R.id.rv_page2)
    RecyclerView mRvPage2;
    private List<String> urlList = new ArrayList<>();;
    private ListVideoAdapter videoAdapter;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private  List<Douyin.DataBean>  datalists;
    public  RefreshLayout refreshLayout;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vidvo);
        ButterKnife.inject(this);
        getdata();
       refreshLayout =findViewById(R.id.refreshLayout);
        onLondBanner();
//        refreshLayout.autoRefresh();
    }

    private void onLondBanner() {
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                getdata();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                getdata();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                },1000);

            }
        });

    }
    private void initView() {
        snapHelper = new PagerSnapHelper();
        mRvPage2.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(mRvPage2);
        videoAdapter = new ListVideoAdapter(urlList);
        layoutManager = new LinearLayoutManager(VidvoActivity.this, LinearLayoutManager.VERTICAL, false);
        mRvPage2.setLayoutManager(layoutManager);
        mRvPage2.setAdapter(videoAdapter);

    }

    private void getdata() {
        String uri="http://api01.idataapi.cn:8000/video/gifshow?apikey=4qsYv2PzdKfCh8Zaz98wvo3XCYJzmkHPbnaLaajJ0UQkCPZudM9qCMTU2di5IEV6&rand=1";
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
                String data = response.body().string();
                    Douyin resultBean = new Gson().fromJson(data,Douyin.class);
//                    Log.d("data",data);
                    //对象中拿到集合
                    datalists= resultBean.getData();
                    for (Douyin.DataBean been:datalists) {
                        urlList.add(been.getVideoUrls().get(0));
//                        Log.d("data",been.getTitle());
//                        Log.d("data",been.getVideoUrls().get(0));
                    }
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                            addListener();
                        }
                    });
                }

            }
        });


    }

    private void addListener() {

        mRvPage2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(layoutManager);
                        JZVideoPlayer.releaseAllVideos();
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                        if (viewHolder != null && viewHolder instanceof VideoViewHolder) {
                            ((VideoViewHolder) viewHolder).mp_video.startVideo();
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    class ListVideoAdapter extends BaseRecAdapter<String, VideoViewHolder> {


        public ListVideoAdapter(List<String> list) {
            super(list);
        }

        @Override
        public void onHolder(VideoViewHolder holder, String bean, int position) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            holder.mp_video.setUp(bean, JZVideoPlayerStandard.CURRENT_STATE_NORMAL);
            if (position == 0) {
                holder.mp_video.startVideo();
            }
            Glide.with(context).load(bean).into(holder.mp_video.thumbImageView);
//            holder.tv_title.setText("标题"+datalists.get(position).getTitle());
            holder.data.setText("时间"+datalists.get(position).getPublishDateStr());
            holder.posterScreenName.setText("发布者："+datalists.get(position).getPosterScreenName());
            holder.tv_description.setText("描述:"+datalists.get(position).getDescription());
            holder.tv_likeCount.setText("点赞数："+datalists.get(position).getLikeCount());
            holder.tv_commentCount.setText("评论数："+datalists.get(position).getCommentCount());

        }

        @Override
        public VideoViewHolder onCreateHolder() {
            return new VideoViewHolder(getViewByRes(R.layout.item_page2));
        }


    }

    public class VideoViewHolder extends BaseRecViewHolder {
        public View rootView;
        public MyVideoPlayer mp_video;
        public TextView data;
        public TextView posterScreenName;
        public TextView tv_description;
        public TextView tv_likeCount;
        public TextView tv_commentCount;


        public VideoViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mp_video = rootView.findViewById(R.id.mp_video);
            this.data = rootView.findViewById(R.id.tv_data);
            this.posterScreenName = rootView.findViewById(R.id.tv_posterScreenName);
            this.tv_description = rootView.findViewById(R.id.tv_description);
            this.tv_likeCount = rootView.findViewById(R.id.tv_likeCount);
            this.tv_commentCount= rootView.findViewById(R.id.tv_commentCount);
        }


    }

}
