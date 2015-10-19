package pinguo.rocket.mq.comm;

public class UtilHelper {
	
	/**
	 * 获取配置properties类
	 * @return PropertyManage
	 */
	public static PropertyManage getProperties() {
		return (PropertyManage) ApplicationContextUtil.getBean("propertyManage");
	}
	
	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 字符串首字母小写
	 * 
	 * @param str
	 * @return
	 */
    public static String toLowerFirstCase(String str)
    {
        if(Character.isLowerCase(str.charAt(0)))
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
    public static String toUpperFirstCase(String str)
    {
        if(Character.isUpperCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
    }
}
