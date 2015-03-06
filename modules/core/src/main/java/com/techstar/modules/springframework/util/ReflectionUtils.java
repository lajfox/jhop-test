package com.techstar.modules.springframework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * Get all declared fields on the leaf class and all superclasses. Leaf class methods are included first.
	 */
	public static Field[] getAllDeclaredFields(Class<?> leafClass) throws IllegalArgumentException {
		final List<Field> fields = new ArrayList<Field>(32);
		doWithFields(leafClass, new FieldCallback() {
			public void doWith(Field field) {
				fields.add(field);
			}
		});
		return fields.toArray(new Field[fields.size()]);
	}

}
