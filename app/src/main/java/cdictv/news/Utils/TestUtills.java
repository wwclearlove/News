package cdictv.news.Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdictv.news.Been.User;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 用于验证输入的密码 验证码验证规则
 */

public class TestUtills {


    /**
     * @param regex
     *正则表达式的字符串
     * @param str
     * 要验证的字符串
     * @return 如果正确匹配 则返回true 否则返回false
     */
    private static boolean match(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证手机号码是否正确
     * @param str 要验证的输入的电话号码
     * @return 返回电话是否正确 如果正确匹配 则返回true 否则返回false
     */
    public static boolean IsHandset(String str){
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        return match(regex,str);
    }

    /**
     * @param str 输入验证码为4位
     * @return 验证成功为true
     */
    public static boolean IsCodeNmb(String str){
        String regex = "^[0-9]{4}*$";
        return match(regex,str);
    }

    /**
     * @param str 验证输入6—11位的密码
     * @return
     */
    public static boolean IsPassword(String str){
        String regex = "^([0-9]|[a-zA-Z]){6,12}$";
        return match(regex,str);
    }

    /**
     * @param str 用户名以字母开头 长度为5-11的用户名
     * @return
     */
    public static boolean IsUserId(String str){
        String regex = "^[a-zA-Z]\\w{5,17}$";
        return match(regex,str);
    }

    /**
     * 设置数值装换为时间
     * @param l 转换的时间
     * @return 剩余的秒数
     */
    public static String formatLongToTimeStr(Long l) {
        int second = 0;
        second = l.intValue();
        String strtime = "剩余"+ second + "秒";
        return strtime;
    }


    /**
     * 判断是用户名规范
     *
     * @param username
     * @param pawone
     * @param pawtwo
     * @param phone
     * @param context
     * @return
     */
    public static boolean initCheckUser(String username, String pawone, String pawtwo, String phone,Context context){
        if(username.isEmpty()){
            Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else {
            if(pawone.isEmpty()|| pawtwo.isEmpty()){
                Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            }else {
                if(phone.equals("")){
                    Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    //通过嵌套判断 可以一次性对数据进行添加到数据库中 免去分部分添加
                    if (!TestUtills.IsUserId(username)) {
                        Toast.makeText(context, "用户名开头必须为字母", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!TestUtills.IsHandset(phone)) {
                            Toast.makeText(context, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!TestUtills.IsPassword(pawone)) {
                                Toast.makeText(context, "请输入6—11位密码", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!pawone.equals(pawtwo)) {
                                    Toast.makeText(context, "密码两次输入不一致", Toast.LENGTH_SHORT).show();
                                } else {
                                   return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * 用于注册数据
     * @param user
     * @return
     */

    public static User initUserRegiser(User user){
        final User[] userinfo = {null};

        Gson gson = new Gson();
        String userjson = gson.toJson(user);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),userjson);

        Request request = new Request.Builder()
                .url("http://134.175.154.154/new/api/news/regist")
                .post(requestBody)
                .build();

        try {
            Response response =okHttpClient.newCall(request).execute();
            return  gson.fromJson(response.body().string(),User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}

