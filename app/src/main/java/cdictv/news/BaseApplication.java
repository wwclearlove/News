package cdictv.news;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cdictv.news.widget.MyFileNameGenerator;

public class BaseApplication extends Application {

    private HttpProxyCacheServer proxy;
    private List<Activity> activityList;
    /**
     * 添加activity到activityList集合中
     * @param activity
     *            每一個activity
     */

    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityList != null) {

            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }

    }

    public void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing() && activity != null) {
                activity.finish();
            }
        }
        clearActivity();
    }

    public void clearActivity() {
        activityList.clear();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        BaseApplication app = (BaseApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LitePal
        LitePal.initialize(this);
    }
}
