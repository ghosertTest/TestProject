package com.loadtrend.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;


public class TxHandler implements InvocationHandler
{
    private Object originalObject = null;
    
    public Object bind( Object originalObject )
    {
        this.originalObject = originalObject;
        return Proxy.newProxyInstance( originalObject.getClass().getClassLoader(),
                                       originalObject.getClass().getInterfaces(),
                                       this );
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object result = null;
        
        if ( method.getName().startsWith( "insert" ) )
        {
            UserTransaction tx = null;
            try
            {
                tx = (UserTransaction) ( new InitialContext().lookup( "java/tx" ) );
                
                result = method.invoke( originalObject, args );
                
                tx.commit();
            }
            catch ( Exception ex )
            {
                if ( null != tx )
                {
                    try
                    {
                        tx.rollback();
                    }
                    catch ( Exception e )
                    {
                        System.out.println( e.getMessage() );
                    }
                }
            }
        }
        else
        {
            result = method.invoke( originalObject, args );
        }
        
        return result;
    }
}