package cdictv.news.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}

