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
package org.amiladomingo.logging.config;

import org.amiladomingo.logging.annotation.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <code>NamespaceHandler</code> allowing for the configuration of support feature using 
 * either XML or using annotations.
 * 
 * @author Amila Domingo
 * @since 1.0
 */
public class LoggingNamespaceHandlerSupport extends NamespaceHandlerSupport {

	private static final String ANNOTATION_DRIVEN = "annotation-driven";

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	public void init() {
		this.registerBeanDefinitionParser(ANNOTATION_DRIVEN, new AnnotationDrivenBeanDefinitionParser());
	}
}
