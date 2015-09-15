package pinguo.rocket.mq.comm;

import java.lang.reflect.Field;

public class ConsumerHelper {

	/**
	 * 对象属性值转换
	 * 
	 * @param object
	 * @param otherObject
	 * @return
	 */
	public static Object objectPropertiesToOtherOne(Object object, Object otherObject){
		Class<? extends Object> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			String getMethodName = "get" + UtilHelper.toUpperFirstCase(field.getName());
			String setMethodName = "set" + UtilHelper.toUpperFirstCase(field.getName());
			
			try {
				Object getValue = ReflectionUtils.invokeMethod(object, getMethodName, new Class<?>[]{}, new Object[]{});
				if (field.getGenericType().toString().equals("class java.lang.String")) {
					Class<?>[] parameterTypes = {String.class};
					Object[] parameters = {getValue.toString()};
					ReflectionUtils.invokeMethod(otherObject, setMethodName, parameterTypes, parameters);
					
				} else if (field.getGenericType().toString().equals("int")) {
					Class<?>[] parameterTypes = {int.class};
					Object[] parameters = {Integer.parseInt(getValue.toString())};
					ReflectionUtils.invokeMethod(otherObject, setMethodName, parameterTypes, parameters);
					
				} else if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
					Class<?>[] parameterTypes = {boolean.class};
					Object[] parameters = {Boolean.parseBoolean(getValue.toString())};
					ReflectionUtils.invokeMethod(otherObject, setMethodName, parameterTypes, parameters);
				}
			} catch (Exception exception) {
//				exception.printStackTrace();
				System.out.println(otherObject.toString() + "没有方法，" + setMethodName);
				continue;
			}
		}
		
		return otherObject;
	}
}
