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
package org.amiladomingo.logging.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.amiladomingo.logging.interceptor.AnnotationLogAttribute;
import org.amiladomingo.logging.interceptor.LogAttribute;
import org.amiladomingo.logging.interceptor.LogAttributeSource;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of the LogAttributeSource
 * 
 * <p>This class reads {@link Loggable} annotation and exposes corresponding log attributes log infrastructure
 * 
 * @author Amila Domingo
 * @since 1.0
 *
 */
public class AnnotationLogAttributeSource implements LogAttributeSource {

	private final static LogAttribute DEFAULT_LOG_ATTRIBUTE = new AnnotationLogAttribute();

	final Map<Object, LogAttribute> attributeCache = new ConcurrentHashMap<Object, LogAttribute>();

	private final Set<LogAnnotationParser> annotationParsers;

	public LogAttribute getLogAttribute(Method method, Class<?> targetClass) {
		// First, see if we have a cached value.
		Object cacheKey = getCacheKey(method, targetClass);
		Object cached = this.attributeCache.get(cacheKey);
		if (cached != null) {
			// Value will either be canonical value indicating there is no log attribute,
			// or an actual log attribute.
			if (cached == DEFAULT_LOG_ATTRIBUTE) {
				return null;
			} else {
				return (LogAttribute) cached;
			}
		} else {
			// We need to work it out.
			LogAttribute logAtt = computeLogAttribute(method, targetClass);
			// Put it in the cache.
			if (logAtt == null) {
				this.attributeCache.put(cacheKey, DEFAULT_LOG_ATTRIBUTE);
			} else {
				this.attributeCache.put(cacheKey, logAtt);
			}
			return logAtt;
		}
	}

	private LogAttribute computeLogAttribute(Method method, Class<?> targetClass) {
		// Don't allow no-public methods as required.
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return null;
		}

		// Ignore CGLIB subclasses - introspect the actual user class.
		Class<?> userClass = ClassUtils.getUserClass(targetClass);
		// The method may be on an interface, but we need attributes from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
		// If we are dealing with method with generic parameters, find the original method.
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

		// First try is the method in the target class.
		LogAttribute logAtt = findLogAttribute(specificMethod);
		if (logAtt != null) {
			return logAtt;
		}

		// Second try is the log attribute on the target class.
		logAtt = findLogAttribute(specificMethod.getDeclaringClass());
		if (logAtt != null) {
			return logAtt;
		}

		if (specificMethod != method) {
			// Fallback is to look at the original method.
			logAtt = findLogAttribute(method);
			if (logAtt != null) {
				return logAtt;
			}
			// Last fallback is the class of the original method.
			return findLogAttribute(method.getDeclaringClass());
		}
		return null;
	}

	protected LogAttribute findLogAttribute(Method method) {
		return determineLogAttribute(method);
	}

	protected LogAttribute findLogAttribute(Class<?> clazz) {
		return determineLogAttribute(clazz);
	}

	protected LogAttribute determineLogAttribute(AnnotatedElement ae) {
		for (LogAnnotationParser annotationParser : this.annotationParsers) {
			LogAttribute attr = annotationParser.parseLogAnnotation(ae);
			if (attr != null) {
				return attr;
			}
		}
		return null;
	}

	protected Object getCacheKey(Method method, Class<?> targetClass) {
		return new DefaultCacheKey(method, targetClass);
	}

	public AnnotationLogAttributeSource() {
		this.annotationParsers = new LinkedHashSet<LogAnnotationParser>(2);
		this.annotationParsers.add(new SpringLogAnnotationParser());
	}

	/**
	 * Should only public methods be allowed to have log semantics?
	 * <p>The default implementation returns <code>false</code>.
	 */
	protected boolean allowPublicMethodsOnly() {
		return true;
	}

	/**
	 * Default cache key for the LogAttribute cache.
	 */
	@SuppressWarnings("rawtypes")
	private static class DefaultCacheKey {

		private final Method method;

		private final Class targetClass;

		public DefaultCacheKey(Method method, Class targetClass) {
			this.method = method;
			this.targetClass = targetClass;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof DefaultCacheKey)) {
				return false;
			}
			DefaultCacheKey otherKey = (DefaultCacheKey) other;
			return (this.method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(this.targetClass,
					otherKey.targetClass));
		}

		@Override
		public int hashCode() {
			return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
		}
	}

}
