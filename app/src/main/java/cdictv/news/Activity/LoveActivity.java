package cdictv.news.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import cdictv.news.Been.FragmentBeen;
import cdictv.news.R;
import cdictv.news.Utils.saveUtil;
import cdictv.news.fragment.FlowLayout;

public class LoveActivity extends Activity{
    private ArrayList<FragmentBeen> arrayList;
    ArrayList<FragmentBeen> copyArray=new ArrayList<>();
    ArrayList<FragmentBeen>   addArray=new ArrayList<>();

    private ArrayList<FragmentBeen> allList=new ArrayList<>();
    private String titles[]={"热门精选","国内新闻","国际新闻","社会新闻","娱乐新闻","IT资讯",
            "NBA新闻","足球新闻","移动互联", "健康知识","体育新闻","科技新闻"
            ,"创业新闻","苹果新闻","军事新闻","旅游资讯","奇闻异事","人工智能"};
    private int contents[]={R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout
            ,R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout,R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout ,R.layout.itemlayout,R.layout.itemlayout,
            R.layout.itemlayout,};
    private LayoutInflater mInflater;
    private FlowLayout mFlowLayout;
    private FlowLayout allmFlowLayout;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        mInflater = LayoutInflater.from(this);
        mFlowLayout =findViewById(R.id.id_flowlayout);
        allmFlowLayout =findViewById(R.id.id_allflowlayout);
        addArr();
        Intent intent = this.getIntent();
        arrayList = (ArrayList<FragmentBeen>) intent.getSerializableExtra("list");;//获取list方式
        showHavetv();
        showXsarry();

    }
    public void click(View v){
        setDate();
        finish();
    }
    private void setDate() {
        arrayList.removeAll(copyArray);
        arrayList.addAll(addArray);
        saveUtil util=new saveUtil(this,"fragmentList",arrayList);
        util.savaList();
        for (FragmentBeen been : arrayList) {
            Log.i("love-返回",been.getTitle());
        }
        Intent mintent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("List",(Serializable)arrayList);
        mintent.putExtras(bundle);
        Log.i("love-arraylist",copyArray.toString());
        setResult(2, mintent);//返回值调用函数，其中2为resultCode，返回值的标志
//        mintent.putExtra("",allList);
//startActivity(mintent);
        //        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showHavetv() {
        mFlowLayout.removeAllViews();
        arrayList.removeAll(copyArray);
        arrayList.addAll(addArray);
        copyArray.clear();
        addArray.clear();
//        arrayList.addAll(addArray);
        for (int i = 0; i < arrayList.size() ; i++) {
            final TextView tv = (TextView) mInflater.inflate(
                    R.layout.search_lablelayout, mFlowLayout, false);
            tv.setText(arrayList.get(i).getTitle());
            final int n=i ;
            //点击事件
            if(i!=0) {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tv.setVisibility(View.GONE);

                        copyArray.add(arrayList.get(n));

                        showXsarry();
//                        }


                    }
                });
            }else {
                tv.setTextColor(Color.RED);
            }
            mFlowLayout.addView(tv);//添加到父View
        }
        for (FragmentBeen been:copyArray){
            arrayList.remove(been);
        }

    }

    private void showXsarry() {
        allmFlowLayout.removeAllViews();
        allList.clear();
        addArr();
        allList.removeAll(arrayList);
        allList.addAll(copyArray);
        for (int i = 0; i < allList.size(); i++) {
//                if (allList.get(j).getTitle().equals(arrayList.get(i).getTitle())) {
            final TextView tv = (TextView) mInflater.inflate(
                    R.layout.search_nolablelayout, allmFlowLayout, false);
            tv.setText(allList.get(i).getTitle());
            Log.d("da", allList.get(i).getTitle());
            final int n = i;
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    tv.setVisibility(View.GONE);
                    addArray.add(allList.get(n));
                    showHavetv();
                }

            });

            allmFlowLayout.addView(tv);//添加到父View
        }
    }


    @Override
    public void onBackPressed() {
        setDate();
        super.onBackPressed();
    }

    private void addArr() {
        for (int i = 0; i <titles.length ; i++) {
            FragmentBeen been=new FragmentBeen();
            been.setContent(contents[i]);
            been.setTitle(titles[i]);
            allList.add(been);
        }

    }

}
