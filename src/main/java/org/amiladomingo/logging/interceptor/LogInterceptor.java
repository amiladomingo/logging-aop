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

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import org.amiladomingo.logging.LoggingMode;
import org.amiladomingo.util.CommonUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

/**
 * @author Amila Domingo
 *
 */
public class LogInterceptor extends LogAspectSupport implements MethodInterceptor {

	private Logger logger = null;

	public Object invoke(MethodInvocation invocation) throws Throwable {

		// Work out the target class: may be <code>null</code>.
		// The LogAttribute should be passed the target class
		// as well as the method, which may be from an interface.
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

		logger = LoggerFactory.getLogger(targetClass);

		// If the log attribute is null, the method is non-loggable.
		final LogAttribute logAttribute = getLogAttributeSource().getLogAttribute(invocation.getMethod(), targetClass);

		Object retVal = null;
		final long startTime = System.nanoTime();

		try {

			if (logAttribute.inMode(LoggingMode.ALL) || logAttribute.inMode(LoggingMode.START)) {
				doBefore(targetClass, invocation);
			}

			// This is an around advice: Invoke the next interceptor in the chain.
			// This will normally result in a target object being invoked.
			retVal = invocation.proceed();
		} catch (Throwable ex) {
			if (logAttribute.inMode(LoggingMode.ALL) || logAttribute.inMode(LoggingMode.EXCEPTION)) {
				doException(targetClass, invocation, ex);
			}
			throw ex;
		} finally {
		}

		final long endTime = System.nanoTime() - startTime;

		String elapsedTime = null;

		switch (logAttribute.getTimeUnit()) {
		case DAYS:
			elapsedTime = TimeUnit.NANOSECONDS.toDays(endTime) + " days.";
			break;
		case MICROSECONDS:
			elapsedTime = TimeUnit.NANOSECONDS.toMicros(endTime) + " microseconds.";
			break;
		case HOURS:
			elapsedTime = TimeUnit.NANOSECONDS.toHours(endTime) + " hours.";
			break;
		case MINUTES:
			elapsedTime = TimeUnit.NANOSECONDS.toMinutes(endTime) + " minutes.";
			break;
		case NANOSECONDS:
			elapsedTime = TimeUnit.NANOSECONDS.toNanos(endTime) + " nanoseconds.";
			break;
		case SECONDS:
			elapsedTime = TimeUnit.NANOSECONDS.toSeconds(endTime) + " seconds.";
			break;
		default:
			elapsedTime = TimeUnit.NANOSECONDS.toMillis(endTime) + " miliseconds.";
			break;
		}

		if (logAttribute.inMode(LoggingMode.ALL) || logAttribute.inMode(LoggingMode.RETURN)) {
			doAfter(targetClass, invocation, retVal, elapsedTime);
		}

		return retVal;
	}

	private void doException(Class<?> targetClass, MethodInvocation invocation, Throwable throwable) {
		logger.error(throwable.getMessage(), throwable);
	}

	private void doAfter(Class<?> targetClass, MethodInvocation invocation, Object retVal, String elapsedTime) {
		final StringBuilder message = new StringBuilder();

		message.append("Completed executing " + invocation.getMethod().getName() + " in " + elapsedTime);

		if (CommonUtil.isNotNullOrEmpty(retVal)) {
			final Type paramType = invocation.getMethod().getGenericReturnType();

			message.append(" Following was returned: [type: " + paramType + ", value: " + retVal + "]");
		}

		logger.info(message.toString());
	}

	private void doBefore(Class<?> targetClass, MethodInvocation invocation) {
		final StringBuilder message = new StringBuilder();

		message.append("Started executing " + invocation.getMethod().getName() + ".");

		Object[] arguments = invocation.getArguments();

		if (CommonUtil.isNullOrEmpty(arguments)) {
			final Type[] paramTypes = invocation.getMethod().getGenericParameterTypes();

			message.append(" Following arguments were passed: ");

			for (int i = 0; i < arguments.length; i++) {
				message.append("[type: " + paramTypes[i] + ", value: " + arguments[i] + "]");
			}
		}

		logger.info(message.toString());
	}

}