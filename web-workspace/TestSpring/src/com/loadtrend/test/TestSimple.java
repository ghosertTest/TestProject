package com.loadtrend.test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.loadtrend.actions.Action;
import com.loadtrend.aop.AOPInstrumenter;
import com.loadtrend.aop.ITest;
import com.loadtrend.aop.TxHandler;
import com.loadtrend.beans.LoginAction;
import com.loadtrend.beans.User;
import com.loadtrend.daos.IUserDAO;
import com.loadtrend.daos.UserDAO;
import com.loadtrend.daos.UserDAOProxy;
import com.loadtrend.daos.UserHibernateDAO;

import junit.framework.TestCase;

public class TestSimple extends TestCase
{
	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
	
	public void testQuickStart() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");
		Action action = (Action) ctx.getBean("TheAction");
		String string = action.execute("Rod Johnson");
		assertEquals( "Should be Upper the string", string, "HELLOROD JOHNSON" );
	}
	
	public void testReflection() throws Exception {
		Class cls = Class.forName("com.loadtrend.beans.User");
		Method mtd = cls.getMethod("setUsername",new Class[]{String.class});
		Object obj = (Object)cls.newInstance();
		mtd.invoke(obj,new Object[]{"Erica"});
		assertEquals( "User name should be Erica", ( (User) obj ).getUsername(), "Erica" );
	}
	
	public void testBeanWrapper() throws Exception
	{
		Object obj = Class.forName("com.loadtrend.beans.User").newInstance();
		BeanWrapper bw = new BeanWrapperImpl(obj);
		bw.setPropertyValue("username", "Erica");
		assertEquals( "User name should be Erica", bw.getPropertyValue("username"), "Erica" );
	}
	
	public void testBeanFactory() throws Exception
	{
		Resource resource = new FileSystemResource( "bean.xml" );
		XmlBeanFactory factory = new XmlBeanFactory( resource );
		Action action = (Action) factory.getBean( "TheAction" );
		String string = action.execute("Rod Johnson");
		assertEquals( "Should be Upper the string", string, "HELLOROD JOHNSON" );
	}
	
	public void testI18N() throws Exception
	{
		ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");
		Object[] arg = new Object[] { "Erica", Calendar.getInstance().getTime() };
		
		// 1. I18N  以系统默认Locale加载信息(对于中文WinXP而言，默认为zh_CN)
		String msg = ctx.getMessage( "userinfo", arg, Locale.US );
		System.out.println("Message is ===> " + msg);
		
		// 2. Resource Access
        //  file:C:/config.properties
        //  /config.properties
        //  classpath:config.properties
		Resource rs = ctx.getResource("classpath:log4j.properties");
		if ( rs.exists() )
		{
			File file = rs.getFile();
			System.out.println( "FileName: " + file.getName() );
		}
		
		// 3. Event pass
		LoginAction action = (LoginAction)ctx.getBean("loginAction");
		action.login("Erica","mypass");
	}
    
	/**
	 * Implement the transaction by spring configuration file.
     * Target class UserDAO without super class. So Spring create proxy object with CGLIB 
	 * @throws Exception
	 */
    public void testDAO() throws Exception
    {
        Resource resource = new FileSystemResource( "bean.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        // 注意这里须通过代理Bean"userDAOProxy"获得引用，而不是直接getBean(“userDAO”)
        // 此外这里还存在一个有关强制转型的潜在问题，请参见Hibernate in Spring一节后
        // 关于强制转型的补充描述。
        UserDAO userDAO = (UserDAO) factory.getBean("userDAOProxy");
        userDAO.insertUser();
        this.printList( userDAO.getUserList() );
    }
    
    /**
     * Implement the transaction by spring configuration file with the hibernate support.
     * Target class UserHibernateDAO has Interface IUserDAO. So Spring create proxy object with dynamic proxy pattern.
     * @throws Exception
     */
    public void testHibernateDAO() throws Exception
    {
        Resource resource = new FileSystemResource( "Hibernate_Context.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        // 这里并没有直接用UserDAO对获得的Bean实例进行强制转型。这与上面JdbcTemplate的测试代码不同
        // 实际开发中，应该面向接口编程，通过接口来调用Bean提供的服务
        IUserDAO usersDAO = (IUserDAO) factory.getBean("userHibernateDAOProxy");
        User user = new User();
        user.setUsername( "hibernate" );
        user.setPassword( "hibernate-password" );
        usersDAO.insertUser( user );
        
        // Something error should be fixed in usersDAO.getusers() method.
        this.printList( usersDAO.getUsers() );
    }
    
    /**
     * Implement the transaction by own code. ( For the Interface, using proxy design pattern. )
     */
    public void testProxyAOP()
    {
        Resource resource = new FileSystemResource( "Hibernate_Context.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        UserHibernateDAO userHibernateDAO = (UserHibernateDAO) factory.getBean( "userHibernateDAO" );
        
        // Create the proxy object with the transaction ability.
        IUserDAO userDAO = new UserDAOProxy( userHibernateDAO );
        User user = new User();
        user.setUsername( "testProxyAOP" );
        user.setPassword( "testProxyAOP-password" );
        userDAO.insertUser( user );
    }
    
    /**
     * Implement the transaction by own code. ( For the Interface, using dynamic proxy design pattern. )
     */
    public void testDynamicProxyAOP()
    {
        Resource resource = new FileSystemResource( "Hibernate_Context.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        UserHibernateDAO userHibernateDAO = (UserHibernateDAO) factory.getBean( "userHibernateDAO" );
        
        TxHandler handler = new TxHandler();
        
        // Create the proxy object with the transaction ability.
        IUserDAO userDAO = (IUserDAO) handler.bind( userHibernateDAO );
        User user = new User();
        user.setUsername( "testDynamicProxyAOP" );
        user.setPassword( "testDynamicProxyAOP-password" );
        userDAO.insertUser( user );
    }
    
    /**
     * Implement the transaction by own code. ( For the class without the super class, using CGLib to create a subclass. )
     */
    public void testCGLibAOP()
    {
        Resource resource = new FileSystemResource( "bean.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        DataSource dataSource = (DataSource) factory.getBean( "dataSource" );
        
        // Create the subclass object with the transaction ability.
        AOPInstrumenter instrumenter = new AOPInstrumenter();
        UserDAO subUserDAO = (UserDAO) instrumenter.getInstrumentedClass( UserDAO.class );
        subUserDAO.setDataSource( dataSource );
        subUserDAO.insertUser();
    }
    
    /**
     * Implement the AOP by spring configuration file. ( We can also implement it with our own code. )
     */
    public void testSpringAOPFramework()
    {
        Resource resource = new FileSystemResource( "bean.xml" );
        XmlBeanFactory factory = new XmlBeanFactory( resource );
        
        ITest test = (ITest) factory.getBean( "myAOPProxy" );
        
        test.doTest();
        test.executeTest();
    }
    
    private void printList( List list )
    {
        Iterator it = list.iterator();
        while ( it.hasNext() )
        {
            User user = (User) it.next();
            System.out.println( "id: " + user.getId() + " username: " + user.getUsername() + 
                                " password: " + user.getPassword() );
        }
    }
}
