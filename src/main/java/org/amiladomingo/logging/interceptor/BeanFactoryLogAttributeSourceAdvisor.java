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

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author Amila Domingo
 *
 */
public class BeanFactoryLogAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	private LogAttributeSource logAttributeSource;

	private final LogAttributeSourcePointcut pointcut = new LogAttributeSourcePointcut() {
		@Override
		protected LogAttributeSource getLogAttributeSource() {
			return logAttributeSource;
		}
	};

	/**
	 * @param logAttributeSource the logAttributeSource to set
	 */
	public void setLogAttributeSource(LogAttributeSource logAttributeSource) {
		this.logAttributeSource = logAttributeSource;
	}

	/**
	 * Set the {@link ClassFilter} to use for this pointcut.
	 * Default is {@link ClassFilter#TRUE}.
	 */
	public void setClassFilter(ClassFilter classFilter) {
		this.pointcut.setClassFilter(classFilter);
	}

	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
