package pinguo.rocket.mq.comm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * java反射方法类
 *
 */
public class ReflectionUtils {

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * 
	 * @param  object         子类对象
	 * @param  methodName     父类中的方法名
	 * @param  parameterTypes 父类中的方法参数类型
	 * @return 父类中的方法对象
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;

		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
				// 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了

			}
		}

		return null;
	}

	/**
	 * 直接调用对象方法, 而忽略修饰符(private, protected, default)
	 * 
	 * @param  object         子类对象
	 * @param  methodName     父类中的方法名
	 * @param  parameterTypes 父类中的方法参数类型
	 * @param  parameters     父类中的方法参数
	 * @return 父类中方法的执行结果
	 */
	public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
		// 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		// 抑制Java对方法进行检查,主要是针对私有方法而言
		method.setAccessible(true);

		try {
			if (null != method) {

				// 调用object 的 method 所代表的方法，其方法的参数是 parameters
				return method.invoke(object, parameters);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * 
	 * @param  object    子类对象
	 * @param  fieldName 父类中的属性名
	 * @return 父类中的属性对象
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;

		Class<?> clazz = object.getClass();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
				// 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了

			}
		}

		return null;
	}
	
	public static Map<String, Object> getProperties(Object object) {
		Map<String, Object> properties = new HashMap<String, Object>();

		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldVal = getFieldValue(object, fieldName);
			properties.put(fieldName, fieldVal);
		}
		return properties;
	}
	
	/**
	 * 查询对象所有属性
	 * 
	 * @param  object	子对象
	 * @return Field[]
	 */
	public static Field[] getDeclaredFieds(Object object){
		Class<?> clazz = object.getClass();
		return clazz.getDeclaredFields();
	}

	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * 
	 * @param object    子类对象
	 * @param fieldName 父类中的属性名
	 * @param value     将要设置的值
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {

		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName);

		// 抑制Java对其的检查
		field.setAccessible(true);

		try {
			// 将 object 中 field 所代表的值 设置为 value
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * 
	 * @param  object    子类对象
	 * @param  fieldName 父类中的属性名
	 * @return 父类中的属性值
	 */
	public static Object getFieldValue(Object object, String fieldName) {

		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName);

		// 抑制Java对其的检查
		field.setAccessible(true);

		try {
			// 获取 object 中 field 所代表的属性值
			return field.get(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 对象属性值转换
	 * 
	 * @param object		对象A，get方法获取值
	 * @param otherObject	对象b根据A中属性调用setter方法
	 * @return Object
	 */
	public static Object objectPropertiesToOtherOne(Object object, Object otherObject) {
		Field[] fields = getDeclaredFieds(object);

		for (Field field : fields) {
			String getMethodName = "get" + UtilHelper.toUpperFirstCase(field.getName());
			String setMethodName = "set" + UtilHelper.toUpperFirstCase(field.getName());

			try {
				Object getValue = invokeMethod(object, getMethodName, new Class<?>[] {}, new Object[] {});
				if (field.getGenericType().toString().equals("class java.lang.String")) {
					invokeMethod(otherObject, setMethodName, new Class<?>[] { String.class }, new Object[] { getValue.toString() });

				} else if (field.getGenericType().toString().equals("int")) {
					invokeMethod(otherObject, setMethodName, new Class<?>[] { int.class }, new Object[] { Integer.parseInt(getValue.toString()) });

				} else if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
					invokeMethod(otherObject, setMethodName, new Class<?>[] { boolean.class }, new Object[] { Boolean.parseBoolean(getValue.toString()) });
				}
			} catch (Exception exception) {
				System.out.println(otherObject.getClass() + "没有方法，" + setMethodName);
				continue;
			}
		}
		return otherObject;
	}
}
