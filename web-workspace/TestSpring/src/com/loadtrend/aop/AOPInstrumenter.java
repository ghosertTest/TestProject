package com.loadtrend.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class AOPInstrumenter implements MethodInterceptor
{
    private static Logger logger = Logger.getLogger( AOPInstrumenter.class );
    
    public Object getInstrumentedClass( Class cls )
    {
        return Enhancer.create(cls, this);
    }
    
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy ) throws Throwable
    {
        logger.debug( "Before Method =>" + method.getName() );
        
        Object result = methodProxy.invokeSuper( object, args );
        
        logger.debug( "After Method =>" + method.getName() );
        
        return result;
    }
}
