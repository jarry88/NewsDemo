package com.gzp.baselib.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.ArrayRes;
import androidx.annotation.PluralsRes;
import androidx.annotation.StringRes;

import com.gzp.baselib.BaseApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final char[] BASE_64_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '+', '/',};

    /*----------------------------------normal strings--------------------------------------*/

    /**
     * 判断指定字符是否为空白字符，空白符包含：空格、tab 键、换行符
     *
     * @param s 要判断的字符串
     * @return 当字符串为空或者字符串中所有的字符都是空白字符的时候返回 true
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定的字符串是否为空，null 或者长度为 0
     *
     * @param s 字符串
     * @return true 表示为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 指定的字符串 {@link String#trim()} 之后是否为空
     *
     * @param s 字符串
     * @return true 表示为空
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两个 CharSequence 是否相等
     *
     * @param s1 CharSequence 1
     * @param s2 CharSequence 2
     * @return true 表示相等
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 忽略大小写之后，判断两个 String 是否相等
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return true 表示相等
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    /**
     * 获取 CharSequence 的长度
     *
     * @param s CharSequence
     * @return null 的话返回 0，否则返回字符串长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 字符串的首字符大写
     *
     * @param s 字符串
     * @return 处理之后的字符串
     */
    public static String upperFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 字符串的首字符小写
     *
     * @param s 字符串
     * @return 处理之后的字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 字符串反转
     *
     * @param s 字符串
     * @return 反转之后的字符串
     */
    public static String reverse(final String s) {
        if (s == null) return "";
        int len = s.length();
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 获取指定的字节数组对应的十六进制字符串，按照 ASCII 码表计算
     * 比如 ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * 将得到 4142434445464748494A4B4C4D4E4F505152535455565758595A
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            // 字节的高八位
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            // 字节的低八位
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * 将十六进制字符串转换回字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将数字转换成六十四进制字符串
     *
     * @param number 数字
     * @return 字符串
     */
    public static String long2Base64String(long number) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << 6;
        long mask = radix - 1L; // 截取后几位，在 [0,63] 之间
        do {
            buf[--charPos] = BASE_64_DIGITS[(int) (number & mask)];
            number >>>= 6;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }

    /**
     * 将六十四进制字符串还原回数字
     *
     * @param base64String 六十四进制字符串
     * @return 数字
     */
    public static long base64String2Long(String base64String) {
        long result = 0;
        int length = base64String.length();
        for (int i = length - 1; i >= 0; i--) {
            for (int j = 0; j < BASE_64_DIGITS.length; j++) {
                if (base64String.charAt(i) == BASE_64_DIGITS[j]) {
                    result += ((long) j) << 6 * (base64String.length() - 1 - i);
                }
            }
        }
        return result;
    }

    /**
     * 使用指定的字符串将容器中的元素拼接起来
     *
     * @param c         容器
     * @param connector 连接的字符串
     * @param <T>       容器元素类型
     * @return 拼接结果
     */
    public static <T> String connect(Collection<T> c, String connector) {
        return connect(c, connector, new StringFunction<T>() {
            @Override
            public String apply(T from) {
                return from.toString();
            }
        });
    }

    /**
     * 将传入的列表按照指定的格式拼接起来
     *
     * @param c         容器
     * @param connector 连接的字符串
     * @param function  对象到字符串映射格式
     * @param <T>       对象类型
     * @return 拼接结果
     */
    public static <T> String connect(Collection<T> c, String connector, StringFunction<T> function) {
        if (c == null || c.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int size = c.size();
        for (T element : c) {
            if (count++ != size - 1) {
                sb.append(function.apply(element)).append(connector);
            } else {
                sb.append(function.apply(element));
            }
        }
        return sb.toString();
    }

    /*----------------------------------android resources--------------------------------------*/

    public static String getString(@StringRes int id) {
        return BaseApplication.instance.getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return BaseApplication.instance.getResources().getString(id, formatArgs);
    }

    public static CharSequence getText(@StringRes int id) {
        return BaseApplication.instance.getResources().getText(id);
    }

    public static CharSequence getQuantityText(@PluralsRes int id, int quantity) {
        return BaseApplication.instance.getResources().getQuantityText(id, quantity);
    }

    public static String getQuantityString(@PluralsRes int id, int quantity) {
        return BaseApplication.instance.getResources().getQuantityString(id, quantity);
    }

    public static String getQuantityString(@PluralsRes int id, int quantity, Object... formatArgs) {
        return BaseApplication.instance.getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static CharSequence[] getTextArray(@ArrayRes int id) {
        return BaseApplication.instance.getResources().getTextArray(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return BaseApplication.instance.getResources().getStringArray(id);
    }

    public static String format(@StringRes int resId, Object... arg) {
        try {
            return String.format(BaseApplication.instance.getString(resId), arg);
        } catch (Exception e) {
            return BaseApplication.instance.getString(resId);
        }
    }

    public static String trim(String s) {
        return s.replaceAll("\\s*", "");
    }


    /**
     * Get text from html
     *
     * @param html the html text
     * @return the spanned text
     */
    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    /**
     * 邮箱验证规则
     *
     * @param email 被校验的邮箱字符串
     * @return the spanned text
     */
    public static Boolean isCorrectEmail(String email) {
        // 邮箱验证规则
        String regEx = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 手机号验证规则
     *
     * @param phone 被校验的邮箱字符串
     * @return the spanned text
     */
    public static Boolean isCorrectPhone(String phone) {
        // 邮箱验证规则
        String regEx = "^[1-9]\\d{9}$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 手机号验证规则
     *
     * @param name 被校验的邮箱字符串
     * @return the spanned text
     */
    public static Boolean isCorrectName(String name) {
        // 邮箱验证规则
        String regEx = "^[\\w \\-\\/&\\[\\]#\\*\\(\\)\\.\\,';]{1,20}$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 生成一批密码
     * <br>生成规则：
     * 大写字母+小写字母+数字
     * @param num 要生成多少个密码
     * @param wordNum 要生成的密码长度是多少
     * @return
     */
    public static List<String> getPasswords(int num, Integer wordNum){
        int total=wordNum;//密码总位数
        List<String> passwords=new ArrayList<String>();
        while(passwords.size()<num){
            StringBuffer sb=new StringBuffer();
            int upperNum=getRadomInt(1,total-2);//大写字母位数，保留至少两位，用来放小写和数字
            int lowerNum=getRadomInt(1, total-upperNum-1);//小写字母位数，为总数减去大写字母占用的数量，再为数字区域保留至少1
            int nnum=total-upperNum-lowerNum;//最后剩余数字的位数，为总数减去大写和小写字母位数之后剩余的位数
            //随机获取到每个类型的位置index
            Map<Integer,String> indexMap=new HashMap<Integer,String>();
            while(indexMap.size()<upperNum){
                //确定大写字母的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "upper");
                }
            }
            while(indexMap.size()<upperNum+lowerNum){
                //确定小写字母的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "lower");
                }
            }
            while(indexMap.size()<total){
                //确定数字的索引号
                int rint=getRadomInt(0, total-1);
                if(indexMap.get(rint)==null){
                    indexMap.put(rint, "nnum");
                }
            }
            //组装密码
            for(int i=0;i<total;i++){
                if("upper".equals(indexMap.get(i))){
                    sb.append(getUpper());
                }else if("lower".equals(indexMap.get(i))){
                    sb.append(getLetter());
                }else{
                    sb.append(getNum());
                }
            }
            passwords.add(sb.toString());
        }
        return passwords;
    }
    /**
     * 随机获取一个小写字母
     */
    public static char getLetter(){
        char c=(char)getRadomInt(97, 122);
        return c;
    }

    /**
     * 随机获取一个大写字母
     */
    public static char getUpper(){
        char c=(char)getRadomInt(65, 90);
        return c;
    }

    /**
     * 随机获取一个0-9的数字
     * @return
     */
    public static int getNum(){
        return getRadomInt(0, 9);
    }

    /**
     * 获取一个范围内的随机数字
     * @return
     */
    public static int getRadomInt(int min,int max){
        return new Random().nextInt(max-min+1)+min;
    }

    /**
     * 将字符串数组使用分隔符拼接为新字符串
     *
     * @param source    原始数组
     * @param delimiter 分隔符
     * @return 拼接后的新字符串
     */
    public static String join(String[] source, String delimiter) {
        StringBuffer target = new StringBuffer();
        boolean delimiterIsValid = true;

        if (source == null || source.length < 1) {
            return target.toString();
        }
        if (delimiter == null || delimiter.trim().length() < 1) {
            delimiterIsValid = false;
        }

        for (int i = 0; i < source.length; i++) {
            if (delimiterIsValid && i != 0) {
                target.append(delimiter.trim());
            }
            target.append(source[i]);
        }

        return target.toString();
    }

    /**
     * 校验联系人姓名
     *
     * @param value 联系人姓名输入框值
     * @return 校验结果（成功/失败消息）
     */
    public static String nameValidate(String value) {
        String message = null;
        String name = trim(value);
        String nameSpecial = name.replaceAll("[\\w \\-\\/&\\[\\]#\\*\\(\\)\\.\\,';]", "");//将合法字符去掉，留下不合法字符

        if (nameSpecial.length() > 0) {
            nameSpecial = join(nameSpecial.split(""), ",");
            message = String.format("Special symbols are not allowed: \"%s\" in name.", nameSpecial);
        } else if (name.length() > 20) {
            message = "Enter up to 20 characters without spaces for name.";
        } else if (name.length() <= 0) {
            message = "Please enter Contact name.";
        } else {
            message = "Succeed";
        }

        return message;
    }
}
