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
package org.amiladomingo.logging;

/**
 * Supported logging modes. LoggingMode defines what level of logging needs to be happen
 * on methods
 * 
 * @author Amila Domingo
 * @since 1.0
 */
public enum LoggingMode {
	// All logging modes, which includes START, RETURN, EXCEPTION
	ALL,
	// Start logging mode, this mode logs start of a method execution with the passed arguments
	START,
	// Return logging mode, this mode logs return of a method execution with the return object
	RETURN,
	// Exception logging mode, this mode logs exceptional return of a method execution
	EXCEPTION;
}
