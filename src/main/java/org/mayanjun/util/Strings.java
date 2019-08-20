package org.mayanjun.util;

public class Strings {

    private Strings() {
    }

    /**
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串脱敏
     * @param src 源字符串
     * @param percent 隐藏百分比
     * @return 脱敏字符串
     */
    public static String insensitive(String src, float percent) {
        if (src == null) return "";
        percent = Math.abs(percent);
        if (percent >= 1) return src;
        char cs[] = src.toCharArray();
        int cslen = cs.length;
        int range = (int) (cslen * percent);
        if (range == 0) range = 1;
        int start = (cslen - range) / 2;
        int end = start + range - 1;
        if (end >= cslen) end = cslen - 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cslen; i++) {
            if (i >= start && i <= end) {
                sb.append('*');
            } else {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 随机生成字符串
     * @param len 长度
     * @return
     */
    public static String random(int len) {
        // 33 - 126
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int cp = (int) (Math.random() * 94);
            char c = (char) (cp + 33);
            sb.append(c);
        }
        return sb.toString();
    }
}
