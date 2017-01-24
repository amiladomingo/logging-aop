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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.amiladomingo.logging.interceptor.AnnotationLogAttribute;
import org.amiladomingo.logging.interceptor.LogAttribute;

/**
 * @author Amila Domingo
 *
 */
public class SpringLogAnnotationParser implements LogAnnotationParser, Serializable {

	/* (non-Javadoc)
	 * @see com.amiladomingo.laundry.logging.annotation.LogAnnotationParser#parseLogAnnotation(java.lang.reflect.AnnotatedElement)
	 */
	public LogAttribute parseLogAnnotation(AnnotatedElement ae) {
		Loggable ann = ae.getAnnotation(Loggable.class);
		if (ann == null) {
			for (Annotation metaAnn : ae.getAnnotations()) {
				ann = metaAnn.annotationType().getAnnotation(Loggable.class);
				if (ann != null) {
					break;
				}
			}
		}
		if (ann != null) {
			return parseLogAnnotation(ann);
		} else {
			return null;
		}
	}

	public LogAttribute parseLogAnnotation(Loggable ann) {
		AnnotationLogAttribute ala = new AnnotationLogAttribute();
		ala.setMode(ann.mode());

		ala.setTimeUnit(ann.timeUnit());

		return ala;
	}

}
