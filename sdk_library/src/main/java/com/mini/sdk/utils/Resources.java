package com.mini.sdk.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import android.content.Context;

public class Resources {

	public static int getLayoutId(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}

	public static int getStringId(Context context, String name) {
		return context.getResources().getIdentifier(name, "string", context.getPackageName());
	}

	public static int getDrawableId(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}

	public static int getStyleId(Context context, String name) {
		return context.getResources().getIdentifier(name, "style", context.getPackageName());
	}

	public static int getId(Context context, String name) {
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}

	public static int getColorId(Context context, String name) {
		return context.getResources().getIdentifier(name, "color", context.getPackageName());
	}

	public static int getArrayId(Context context, String name) {
		return context.getResources().getIdentifier(name, "array", context.getPackageName());
	}

	public static int getStyleable(Context context, String name) {
		return context.getResources().getIdentifier(name, "styleable", context.getPackageName());
	}

	public static int getDimen(Context context, String name) {
		return context.getResources().getIdentifier(name, "dimen", context.getPackageName());
	}

	public static int getAnim(Context context, String name) {
		return context.getResources().getIdentifier(name, "anim", context.getPackageName());
	}

	public static int[] getStyleableIntArray(Context context, String name) {
		try {
			Field[] fields = Class.forName(context.getPackageName() + ".R$styleable").getFields();// .与$
																									// difference,$表示R的子类
			for (Field field : fields) {
				if (field.getName().equals(name)) {
					int ret[] = (int[]) field.get(null);
					return ret;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 遍历R类得到styleable数组资源下的子资源，1.先找到R类下的styleable子类，2.遍历styleable类获得字段值
	 *
	 * @param context
	 * @param styleableName
	 * @param styleableFieldName
	 * @return
	 */
	public static int getStyleableFieldId(Context context, String styleableName, String styleableFieldName) {
		String className = context.getPackageName() + ".R";
		String type = "styleable";
		String name = styleableName + "_" + styleableFieldName;
		try {
			Class<?> cla = Class.forName(className);
			for (Class<?> childClass : cla.getClasses()) {
				String simpleName = childClass.getSimpleName();
				if (simpleName.equals(type)) {
					for (Field field : childClass.getFields()) {
						String fieldName = field.getName();
						if (fieldName.equals(name)) {
							return (Integer) field.get(null);
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param context
	 * @param type
	 * @param name
	 * @return
	 */
	public static Object getResourceData(Context context, String type, String name) {
		try {
			Class<?> arrayClass = getResourceClass(context, type).getClass();
			Field intField = arrayClass.getField(name);
			return intField.get(arrayClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static HashMap<String, Object> ResourceClass = new HashMap<String, Object>();

	private static Object getResourceClass(Context context, String type) {
		if (ResourceClass.containsKey(type)) {
			return ResourceClass.get(type);
		} else {
			try {
				Class<?> resource = Class.forName(context.getPackageName() + ".R");
				Class<?>[] classes = resource.getClasses();
				for (Class<?> c : classes) {
					int i = c.getModifiers();
					String className = c.getName();
					String s = Modifier.toString(i);
					if (s.contains("static") && className.contains(type)) {
						ResourceClass.put(type, c.getConstructor().newInstance());
						return ResourceClass.get(type);
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
