package pinguo.rocket.mq.comm;

import java.util.regex.Pattern;
public class ValidateHelper {
	
	/**
	 * 字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public final static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim()) || "null".equals(str.toLowerCase());
	}
	
	/**
	 * 字符串是否是float类型
	 * 
	 * @param str
	 * @return
	 */
    public final static boolean isFloat(String str) {
    	return match(str, "^[-\\+]?\\d+(\\.\\d+)?$");
    }
    
    /**
     * 判断是否为合法字符(a-zA-Z0-9-_.)
     * 
     * @param str
     * @return
     */
    public final static boolean isRightfulString(String str){
        return match(str, "^[A-Za-z0-9_\\.-]+$"); 
    }
    
    private final static boolean match(String text, String reg) {
        return Pattern.compile(reg).matcher(text).matches();
    }
    
}
