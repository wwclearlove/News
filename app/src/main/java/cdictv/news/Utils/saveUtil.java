package cdictv.news.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cdictv.news.Been.FragmentBeen;

public class saveUtil {
    private ArrayList<FragmentBeen> list;
    private SharedPreferences sp;

    public saveUtil(Context context, String name, ArrayList<FragmentBeen> list) {
        this.list = list;
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    @SuppressLint("ApplySharedPref")
    public void savaList() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(list); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KEY_PEOPLE_LIST_DATA", jsonStr); //存入json串
        editor.commit();  //提交
    }

    public ArrayList<FragmentBeen> showlist() {
        ArrayList<FragmentBeen> qlist = new ArrayList<FragmentBeen>();
        String frListJson = sp.getString("KEY_PEOPLE_LIST_DATA", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (frListJson != "")  //防空判断
        {
            Gson gson = new Gson();
            qlist = gson.fromJson(frListJson, new TypeToken<ArrayList<FragmentBeen>>() {
            }.getType());

        }
        return qlist;
    }
}