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

import org.amiladomingo.logging.interceptor.LogAttribute;

/**
 * Parser for log annotations
 * 
 * @author Amila Domingo
 * @since 1.0
 *
 */
public interface LogAnnotationParser {

	/**
	 * Parse the log attribute for the given method or class.
	 * <p>This parses a known log annotation into attribute class. Returns <code>null</code> if the method class
	 * is not loggable.
	 * @param ae the annotated method or class
	 * @return LogAttribute the configured log attribute, or <code>null</code> if none was found
	 */
	LogAttribute parseLogAnnotation(AnnotatedElement ae);
}
