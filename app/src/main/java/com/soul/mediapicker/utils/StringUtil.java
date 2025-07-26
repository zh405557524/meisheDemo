package com.soul.mediapicker.utils;

import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.util.Pair;

/**
 * Created by
 *
 * @author luopeng
 * @date 2019-12-26.
 * from FuYao company
 */
public class StringUtil {
    private static final double HUNDRED_MILLION = 100000000;
    private static final double TEN_THOUSAND = 10000;
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static String appendUrlParams(String url, Pair<String, ?>... params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        if (params != null) {
            for (Pair<String, ?> pair : params) {
                if (pair != null) {
                    builder.appendQueryParameter(pair.first, pair.second == null ? "" : String.valueOf(pair.second));
                }
            }
        }
        return builder.toString();
    }

    /**
     * 追加参数到url，如果新参数跟老参数重复，将覆盖老参数
     * @param url
     * @param params
     * @return
     */
    public static String appendReplaceUrlParams(String url, Pair<String, ?>... params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        List<String> queryParameterNameList = new ArrayList<>(queryParameterNames);
        if (params != null) {
            for (Pair<String, ?> pair : params) {
                if (pair != null) {
                    //删除跟新参数重复的参数
                    queryParameterNameList.remove(pair.first);
                }
            }
        }
        builder.clearQuery();
        for (String element : queryParameterNameList) {
            builder.appendQueryParameter(element, uri.getQueryParameter(element));
        }

        if (params != null) {
            for (Pair<String, ?> pair : params) {
                if (pair != null) {
                    builder.appendQueryParameter(pair.first, pair.second == null ? "" : String.valueOf(pair.second));
                }
            }
        }
        return builder.toString();
    }

    public static int countMatches(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    /**
     * float转String
     * @param value 需要转的值
     * @param bit 保留位数
     * @return float值的字符串
     */
    public static String formatDecimal(float value, int bit){
        double value2 = float2Double(value);
        return formatDecimal(value2, bit);
    }

    public static String formatDecimal(float value, int bit, boolean noDecimalWhenZero) {
        double value2 = float2Double(value);
        return formatDecimal(value2, bit, noDecimalWhenZero);
    }

    public static String formatDecimal(double value, int bit){
        return formatDecimal(value, bit, false);
    }

    /**
     * double转String
     * @param value 需要转的值
     * @param bit 保留位数
     * @return double值的字符串
     */
    public static String formatDecimal(double value, int bit, boolean noDecimalWhenZero){
        return formatDecimal(value, bit, noDecimalWhenZero, 0);
    }

    /**
     * double转String
     * @param value 需要转的值
     * @param bit 保留位数
     * @return double值的字符串
     */
    public static String formatDecimal(double value, int bit, boolean noDecimalWhenZero, int needDot) {
        if (noDecimalWhenZero && value == 0) {
            return "0";
        }

        DecimalFormat format;
        StringBuilder builder = new StringBuilder();
        if (needDot > 0) {
            builder.append("#,");
            for (int i = 0; i < needDot; i++) {
                builder.append("#");
            }
        } else {
            builder.append("0");
        }

        if (noDecimalWhenZero && value % 1 == 0) {
            format = new DecimalFormat(builder.toString());
        } else if (bit > 0) {
            builder.append(".");
            for (int i = 0; i < bit; i++) {
                builder.append("0");
            }
            format = new DecimalFormat(builder.toString());
        } else {
            format = new DecimalFormat();
        }
        format.setMaximumFractionDigits(bit);
        format.setGroupingSize(needDot);
        format.setRoundingMode(RoundingMode.FLOOR);
        return format.format(value);
    }

    /**
     *  float转double，避免精度丢失
     * @param data float数据
     * @return double数据
     */
    public static Double float2Double(float data){
        return Double.parseDouble(String.valueOf(data));
    }

    public static boolean isBase64Image(String base64) {
        return !TextUtils.isEmpty(base64) && base64.startsWith("data:image");
    }

    public static String shortenBigDouble(double number, int bit) {
        return shortenBigDouble(number, bit, true);
    }

    public static void decorateDiffPrice(double price, TextView prefixView, TextView suffixView) {
        String value = formatDecimal(price, 2);
        String[] charList = value.split("\\.");
        if (charList.length <= 1) {
            prefixView.setText(value);
            return;
        }

        prefixView.setText(String.format("%s.", charList[0]));
        suffixView.setText(charList[1]);
    }

    public static String shortenBigDouble(double number, int bit, boolean noDecimalWhenZero) {
        if (number >= HUNDRED_MILLION) {
            return String.format("%s亿", formatDecimal(number / HUNDRED_MILLION, bit, noDecimalWhenZero));
        }

        if (number >= TEN_THOUSAND) {
            return String.format("%s万", formatDecimal(number / TEN_THOUSAND, bit, noDecimalWhenZero));
        }

        return formatDecimal(number, bit, noDecimalWhenZero);
    }

    public static String padLeft(String origin, int length, char c) {
        if (TextUtils.isEmpty(origin)) {
            return origin;
        }

        if (origin.length() >= length) {
            return origin;
        }

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < length - origin.length(); i++) {
            stringBuffer.append(c);
        }

        return stringBuffer + origin;
    }

    public static String parseHourMinuteSecond(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long second = (seconds % 3600) % 60;
        return String.format("%s:%s:%s", padLeft(String.valueOf(hours), 2, '0'), padLeft(String.valueOf(minutes), 2, '0'), padLeft(String.valueOf(second), 2, '0'));
    }

    private static final char LOWER_CASE_A = 'a';
    private static final char LOWER_CASE_Z = 'z';
    public static String toUpperCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        char[] ch = str.toCharArray();
        if (ch[0] >= LOWER_CASE_A && ch[0] <= LOWER_CASE_Z) {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    public static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String removeChinese(String content){
        String reg="((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
        Matcher matcher = Pattern.compile(reg).matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            System.out.println(matcher.group());
            sb.append(matcher.group());
        }
        String str = sb.toString();
        return str.isEmpty() ? null : str;

    }
}
