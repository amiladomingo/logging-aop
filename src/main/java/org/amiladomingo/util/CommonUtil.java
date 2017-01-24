/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.amiladomingo.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Amila Domingo
 *
 */
public class CommonUtil {

	private static final HashSet<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	/**
	 * This method is used to check whether passed parameter is null or empty.
	 * 
	 * @param object can be String, Collection, Map or Object[]
	 * @return true / false
	 */

	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(final Object object) {
		if (object == null) {
			return true;
		}

		boolean result = true;

		// For String empty check
		if (object instanceof String) {
			String str = (String) object;

			result = str == null || str.length() == 0;

			// For Collection empty check
		} else if (object instanceof Collection) {
			result = ((Collection) object).isEmpty();

			// For Map empty check
		} else if (object instanceof Map) {
			result = ((Map) object).isEmpty();

			// For Object[] empty check
		} else if (object instanceof Object[]) {
			Object[] array = (Object[]) object;

			result = array.length != 0;
		} else {
			String str = (String) object;

			result = str == null || str.length() == 0;
		}

		return result;
	}

	public static boolean isNotNullOrEmpty(final Object object) {
		return !isNullOrEmpty(object);
	}

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	private static HashSet<Class<?>> getWrapperTypes() {
		HashSet<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(String.class);
		return ret;
	}

}
