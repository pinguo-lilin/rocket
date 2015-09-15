package pinguo.rocket.mq.comm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ConsumerHelper {

	/**
	 * 对象属性值转换
	 * 
	 * @param object
	 * @param otherObject
	 * @return
	 */
	public static Object objectPropertiesToOtherOne(Object object, Object otherObject){
		Class<? extends Object> clazzOne = object.getClass();
		Class<? extends Object> clazzTwo = otherObject.getClass();
		Field[] fields = clazzOne.getDeclaredFields();
		
		for (Field field : fields) {
			
			String getMethodName = "get" + UtilHelper.toUpperFirstCase(field.getName());
			String setMethodName = "set" + UtilHelper.toUpperFirstCase(field.getName());
			
			try {
				Method getMethod = clazzOne.getDeclaredMethod(getMethodName);
				String getValue = (String) getMethod.invoke(object);
				
				if (field.getGenericType().toString().equals("class java.lang.String")) {
					Method setMethod = clazzTwo.getDeclaredMethod(setMethodName, String.class);
					setMethod.invoke(object, getValue);

				} else if (field.getGenericType().toString().equals("int")) {
					Method setMethod = clazzTwo.getDeclaredMethod(setMethodName, int.class);
					setMethod.invoke(object, Integer.parseInt(getValue));
				}
				
			} catch (Exception exception) {
				exception.printStackTrace();
				continue;
			}
		}
		
		return otherObject;
	}
}
