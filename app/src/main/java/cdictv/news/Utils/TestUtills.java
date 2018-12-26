package cdictv.news.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdictv.news.Been.UserInfo;

/**
 * 用于验证输入的密码 验证码验证规则
 *
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
        String regex = "^\\d{6,11}$";
        return match(regex,str);
    }

    /**
     * @param str 用户名以字母开头 长度为5-11的用户名
     * @return
     */
    public static boolean IsUserId(String str){
        String regex = "^[a-zA-Z]\\w{4,10}$";
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
     *  判断密码用户名是否规范 验证密码用户名登录
     * @param username 用户名
     * @param pawone    第一次密码
     * @param pawtwo   第二次密码
     * @param phone     手机号码
     * @param checkcode 发送的验证码
     * @param yzm       输入的验证码
     * @param context   上下文 用于Toast的
     * @return          登录验证成功 返回true 否则false
     */
    public static boolean initCheckUser(String username, String pawone, String pawtwo, String phone, String checkcode, String yzm, Context context){
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
                                    //查询电话号码是否被注册
                                    List<UserInfo> infoList =  DataSupport.where("phone=?", phone).find(UserInfo.class);
                                    List<UserInfo> infoList_one =  DataSupport.where("name=?", username).find(UserInfo.class);
                                    UserInfo user = new UserInfo();
                                    Log.i("infoList", "registerInfo: "+infoList);
                                    //判断不为空 则被注册
                                    if(!infoList.isEmpty()){
                                        Toast.makeText(context, "电话号码已被注册", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if(infoList_one.isEmpty()){
                                            if(checkcode.equals(yzm)){
                                                user.setName(username);
                                                user.setPassword(pawtwo);
                                                user.setPhone(phone);
                                                if(user.save()){
                                                    Toast.makeText(context, "注册成功！", Toast.LENGTH_SHORT).show();
                                                    return true;
                                                }else {
                                                    Toast.makeText(context, "注册失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(context, "用户名以存在", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

