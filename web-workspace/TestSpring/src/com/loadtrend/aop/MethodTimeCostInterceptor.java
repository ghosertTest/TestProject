package com.loadtrend.aop;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class MethodTimeCostInterceptor implements MethodInterceptor, Serializable
{
	protected static final Logger logger = Logger.getLogger( MethodTimeCostInterceptor.class );

	public Object invoke( MethodInvocation invocation ) throws Throwable
	{
		long time = System.currentTimeMillis();
		
		Object rval = invocation.proceed();
		
		time = System.currentTimeMillis() - time;
		
		logger.info("Method Cost Time => " + time + " ms");
		
		return rval;
	}
}