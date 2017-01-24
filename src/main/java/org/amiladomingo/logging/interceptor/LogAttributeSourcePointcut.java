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
package org.amiladomingo.logging.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.ObjectUtils;

/**
 * @author Amila Domingo
 *
 */
abstract class LogAttributeSourcePointcut extends StaticMethodMatcherPointcut{
	
	public boolean matches(Method method, Class targetClass) {
		LogAttributeSource tas = getLogAttributeSource();
		return (tas == null || tas.getLogAttribute(method, targetClass) != null);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LogAttributeSourcePointcut)) {
			return false;
		}
		LogAttributeSourcePointcut otherPc = (LogAttributeSourcePointcut) other;
		return ObjectUtils.nullSafeEquals(getLogAttributeSource(), otherPc.getLogAttributeSource());
	}

	@Override
	public int hashCode() {
		return LogAttributeSourcePointcut.class.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + getLogAttributeSource();
	}


	/**
	 * Obtain the underlying LogAttributeSource (may be <code>null</code>).
	 * To be implemented by subclasses.
	 */
	protected abstract LogAttributeSource getLogAttributeSource();
}
