= Logging AOP

Utility framework for annotation based logging with Spring AOP

Bean with methods to be logged

[source,java,indent=0]
----
	package org.amiladomingo.logging.annotation;

	import java.util.concurrent.TimeUnit;

	import org.amiladomingo.logging.annotation.Loggable;
	import org.junit.Ignore;

	@Ignore
	public class TestBean {

		@Loggable(timeUnit = TimeUnit.DAYS)
		public void someMethod() throws InterruptedException {
			Thread.sleep(1000);
		}
	}
----

Bean configurations

[source,xml,indent=0]
----
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:easy="https://github.com/amiladomingo/logging-aop/schema"
		xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		https://github.com/amiladomingo/logging-aop/schema https://github.com/amiladomingo/logging-aop/schema/logging-aop-1.0.xsd">


		<easy:annotation-driven />

		<bean id="testBean" class="org.amiladomingo.logging.annotation.TestBean" />

	</beans>
----

log4j.properties

[source,properties,indent=0]
----
	log4j.rootLogger=debug, stdout, R

	log4j.appender.stdout=org.apache.log4j.ConsoleAppender
	log4j.appender.stdout.layout=org.apache.log4j.PatternLayout

	# Pattern to output the caller's file name and line number.
	log4j.appender.stdout.layout.ConversionPattern=%5p [%t] (%F:%L) - %m%n

	log4j.appender.R=org.apache.log4j.RollingFileAppender
	log4j.appender.R.File=example.log

	log4j.appender.R.MaxFileSize=100KB
	# Keep one backup file
	log4j.appender.R.MaxBackupIndex=1

	log4j.appender.R.layout=org.apache.log4j.PatternLayout
	log4j.appender.A1.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
----

Test class

[source,java,indent=0]
----
package org.amiladomingo.logging.annotation;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoggableTest {

	private TestBean bean;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("simple.xml");
		bean = (TestBean) context.getBean("testBean");
	}

	@Test
	public void test() throws InterruptedException {
		bean.someMethod();
	}
}
----

