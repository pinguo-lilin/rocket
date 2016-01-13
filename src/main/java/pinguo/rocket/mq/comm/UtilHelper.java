package pinguo.rocket.mq.comm;

public class UtilHelper {
    /**
     * 字符串首字母小写
     *
     * @param str
     * @return
     */
    public static String toLowerFirstCase(String str) {
        if (Character.isLowerCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 字符串首字母大写
     *
     * @param str
     * @return
     */
    public static String toUpperFirstCase(String str) {
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
    }
}
