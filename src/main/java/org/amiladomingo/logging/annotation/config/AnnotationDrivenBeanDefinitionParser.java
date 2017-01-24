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
package org.amiladomingo.logging.annotation.config;

import org.amiladomingo.logging.annotation.AnnotationLogAttributeSource;
import org.amiladomingo.logging.interceptor.BeanFactoryLogAttributeSourceAdvisor;
import org.amiladomingo.logging.interceptor.LogInterceptor;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Parser for the 'annotation-driven' element of the 'laundry' namespace.
 * 
 * @author Amila Domingo
 * @since 1.0
 * 
 */
public class AnnotationDrivenBeanDefinitionParser implements
		BeanDefinitionParser {

	/**
	 * The bean name of the internally managed loggable advisor.
	 */
	public static final String LOGGABLE_ADVISOR_BEAN_NAME = "org.easyframework.logging.annotation.config.internalLoggableAdvisor";

	/**
	 * {@inheritDoc}
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		AopAutoProxyConfigurer
				.configureAutoProxyCreator(element, parserContext);

		return null;
	}

	/**
	 * Inner class to just introduce an AOP framework dependency.
	 */
	private static class AopAutoProxyConfigurer {

		private static final String ADVICE_BEAN_NAME = "adviceBeanName";
		private static final String ORDER = "order";
		private static final String LOG_ATTRIBUTE_SOURCE = "logAttributeSource";

		public static void configureAutoProxyCreator(Element element,
				ParserContext parserContext) {
			AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(
					parserContext, element);

			if (!parserContext.getRegistry().containsBeanDefinition(
					LOGGABLE_ADVISOR_BEAN_NAME)) {
				Object eleSource = parserContext.extractSource(element);

				// Create the AnnotationLogAttributeSource definition.
				RootBeanDefinition sourceDef = new RootBeanDefinition(
						AnnotationLogAttributeSource.class);
				sourceDef.setSource(eleSource);
				sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				String sourceName = parserContext.getReaderContext()
						.registerWithGeneratedName(sourceDef);

				// Create the LogInterceptor definition.
				RootBeanDefinition interceptorDef = new RootBeanDefinition(
						LogInterceptor.class);
				interceptorDef.setSource(eleSource);
				interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				interceptorDef.getPropertyValues().add(LOG_ATTRIBUTE_SOURCE,
						new RuntimeBeanReference(sourceName));
				String interceptorName = parserContext.getReaderContext()
						.registerWithGeneratedName(interceptorDef);

				// Create the BeanFactoryLogAttributeSourceAdvisor definition.
				RootBeanDefinition advisorDef = new RootBeanDefinition(
						BeanFactoryLogAttributeSourceAdvisor.class);
				advisorDef.setSource(eleSource);
				advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				advisorDef.getPropertyValues().add(LOG_ATTRIBUTE_SOURCE,
						new RuntimeBeanReference(sourceName));
				advisorDef.getPropertyValues().add(ADVICE_BEAN_NAME,
						interceptorName);
				if (element.hasAttribute(ORDER)) {
					advisorDef.getPropertyValues().add(ORDER,
							element.getAttribute(ORDER));
				}
				parserContext.getRegistry().registerBeanDefinition(
						LOGGABLE_ADVISOR_BEAN_NAME, advisorDef);

				CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(
						element.getTagName(), eleSource);
				compositeDef.addNestedComponent(new BeanComponentDefinition(
						sourceDef, sourceName));
				compositeDef.addNestedComponent(new BeanComponentDefinition(
						interceptorDef, interceptorName));
				compositeDef.addNestedComponent(new BeanComponentDefinition(
						advisorDef, LOGGABLE_ADVISOR_BEAN_NAME));
				parserContext.registerComponent(compositeDef);
			}
		}
	}
}