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

import java.util.concurrent.TimeUnit;

import org.amiladomingo.logging.LoggingMode;

/**
 * @author Amila Domingo
 *
 */
public class AnnotationLogAttribute implements LogAttribute {

	private LoggingMode[] modes;
	
	private TimeUnit timeUnit;

	/**
	 * @param mode the mode to set
	 */
	public void setMode(LoggingMode[] modes) {
		this.modes = modes;
	}

	public boolean inMode(LoggingMode mode) {
		boolean inMode = false;

		for (int i = 0; i < modes.length; i++) {
			if (modes[i] == mode) {
				inMode = true;
				break;
			}
		}

		return inMode;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
}
