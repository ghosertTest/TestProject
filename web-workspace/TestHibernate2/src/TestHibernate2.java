import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.Company;
import entities.Employee;
import entities.Model;

import junit.framework.TestCase;

public class TestHibernate2 extends TestCase
{
    private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    
    protected void setUp() throws Exception
    {
	    // I'm using a memeory-database called HSQL which support SQL92, SQL99 to create two
        // many-one association tables: Employee & Company, and insert the datas below.
        // "hibernate.show_sql" has been set to true in hibernate.cfg.xml so that you can
        // observe the real SQL to be produced by hibernate in console.
        
        // Company:  1 StubHub
        //           2 EBay
        
        // Employee: 1 Jay Zhang StubHub
        //           2 Frank Zhao StubHub
        //           3 Sean Fu StubHub
        //           
        //           4 Jenny Lv EBay
        //           5 Gary Gai EBay
    }
    
    protected void tearDown() throws Exception
    {
        // Conclusion:
        // 1. For a cached table, Hibernate.initialize() is ok, because no SQL will be produced.
        // 2. For a none-cached table:
        //    For a single id, Hibernate.initialize() is ok
        //    For a list or if only hibernate will produce too many SQLs
        //    use "left join fetch" HQL or SQLQuery.addEntity().addJoin() or return-join in hbm.xml
        //    all the three ways above will just join the tables to reduce the amount of the SQLs.
    }

    /**
     * Although the many-to-one association is defined in Employee.hbm.xml, it will only invoke one SQL below to get the info of Employee Id: 1
     * No SQL for company is invoked, that means, lazy loading is defined by default.
     * 
     * Hibernate: select employee0_.ID as ID1_0_, employee0_.NAME as NAME1_0_, employee0_.COMPANY_ID as COMPANY3_1_0_ from EMPLOYEE employee0_ where employee0_.ID=?
     */
    public void testManyToOneWithLazyLoadByDefault()
    {
        Session session = this.sessionFactory.openSession();
        Employee employee = (Employee) session.get(Employee.class, new Integer(1));
        System.out.println(employee.getName());
        session.close();
        System.out.println("test Many to One with lazy load by defualt finished.\n");
    }
    
    /**
     * If you do need company info after session is closed such as present it on a page like ${employee.company.name}
     * Use Hibernate.initialize() to retrieve it from database, just as we are invoking a findById method on company.
     * So this time we have two SQLs: one for employee and another for comapny. Notice that even session is closed I can get company info as well.
     * 
     * Hibernate: select employee0_.ID as ID5_0_, employee0_.NAME as NAME5_0_, employee0_.COMPANY_ID as COMPANY3_5_0_ from EMPLOYEE employee0_ where employee0_.ID=?
     * Hibernate: select company0_.ID as ID4_0_, company0_.NAME as NAME4_0_ from COMPANY company0_ where company0_.ID=?
     */
    public void testManyToOneWithOutLazyLoad()
    {
        Session session = this.sessionFactory.openSession();
        Employee employee = (Employee) session.get(Employee.class, new Integer(1));
        System.out.println(employee.getName());
        Hibernate.initialize(employee.getCompany());
        session.close();
        System.out.println(employee.getCompany().getName());
        System.out.println("test Many to One without lazy load finished.\n");
    }
    
    /**
     * I will not create new method for testing lazy load on "one-to-many" association, it just the same to "many-to-one" methods above.
     * One thing I'm interested is that hibernate second level cache is doing what the CodesMgrImpl.java has done. Hibernate has cached the instance of Company Id 1 after we retrieved
     * it in testManyToOneWithOutLazyLoad() method above. Make sure you'v already added "<cache usage="read-only"/>" in Company.hbm.xml to enable second level cache.
     * I agree to add this tag to all the static tables only like what we are doing in CodesMgrImpl.java.
     * So this time, although I'm asking for Company Id 1 in this method, no SQL for company table will be produced, just one SQL for all the employees in StubHub.
     * 
     * Hibernate: select employees0_.company_id as company3_1_, employees0_.ID as ID1_, employees0_.ID as ID9_0_, employees0_.NAME as NAME9_0_, employees0_.COMPANY_ID as COMPANY3_9_0_ from EMPLOYEE employees0_ where employees0_.company_id=?
     */
    public void testOneToManyForSecondLevelCache()
    {
        Session session = this.sessionFactory.openSession();
        Company company = (Company) session.get(Company.class, new Integer(1));
        System.out.println(company.getName());
        Set<Employee> employees = company.getEmployees();
        for (Employee employee : employees) {
            System.out.println(employee.getName());
        }
        session.close();
        System.out.println("test One to Many for second level cache finished.\n");
    }
    
    /**
     * For the idea on Model to contain both Company & Employee, if Comapany is a static table which can be cached by CodesMgrImpl.java or 
     * hibernate second level cache, that's ok, if not, it maybe produce a new n+1 SQL select queries performance issue.
     * That is, If only I want to get a list of employees with their company infos, hibernate will produce one SQL for all the employees,
     * and for each employee, hibernate will produce one sql for his/her company infos. In our case we have 5 employees so that hibernate
     * producing 5 more sqls.
     * This problem happens because company is not an instance of employee, and model is populated out of session scope,
     * so we can neither use hibernate first level cache nor the left out join fetch feature.
     * See how to resolve this problem in the testWithoutModelForBothEntities1() & testWithoutModelForBothEntities2() methods below.
     * 
     * Hibernate: select employee0_.ID as ID13_, employee0_.NAME as NAME13_, employee0_.COMPANY_ID as COMPANY3_13_ from EMPLOYEE employee0_
     * Hibernate: select company0_.ID as ID12_0_, company0_.NAME as NAME12_0_ from COMPANY company0_ where company0_.ID=?
     * Hibernate: select company0_.ID as ID12_0_, company0_.NAME as NAME12_0_ from COMPANY company0_ where company0_.ID=?
     * Hibernate: select company0_.ID as ID12_0_, company0_.NAME as NAME12_0_ from COMPANY company0_ where company0_.ID=?
     * Hibernate: select company0_.ID as ID12_0_, company0_.NAME as NAME12_0_ from COMPANY company0_ where company0_.ID=?
     * Hibernate: select company0_.ID as ID12_0_, company0_.NAME as NAME12_0_ from COMPANY company0_ where company0_.ID=?
     */
    public void testModelForBothEntities()
    {
        List<Model> models = new ArrayList<Model>();
        List list = this.mockGetEmployeeListFacadeMethod();
        for (Object object : list) {
            Employee employee = (Employee) object;
            
            // clear hibernate second level cache as we are not enable it, that means we look company as a non static table which can't be cached.
            this.sessionFactory.evict(Company.class);
            
            // Get company info.
            Company company = this.mockGetCompanyFacadeMethod(employee.getCompany().getId());
            
            // populate model.
            Model model = new Model();
            model.setCompany(company);
            model.setEmployee(employee);
            models.add(model);
        }
        System.out.println("test model for both entities finished.\n");
    }
    
    /**
     * Get employee list.
     * @return
     */
    private List mockGetEmployeeListFacadeMethod() {
        Session session = this.sessionFactory.openSession();
        List list = session.createQuery("from Employee").list();
        session.close();
        return list;
    }
    
    /**
     * Get company info.
     * @param id
     * @return
     */
    private Company mockGetCompanyFacadeMethod(Integer id) {
        Session session = this.sessionFactory.openSession();
        Company company = (Company) session.get(Company.class, id);
        session.close();
        return company;
    }
    
    /**
     * The resolution 1 for: n+1 SQL select queries performance issue.
     * Set lazy=true in hbm.xml(This is default option for hibernate3)
     * Use LEFT JOIN FETCH below, only one sql will be produced by hibernate to get the list of employees with their company infos.
     * 
     * Hibernate: select employee0_.ID as ID17_0_, company1_.ID as ID16_1_, employee0_.NAME as NAME17_0_, employee0_.COMPANY_ID as COMPANY3_17_0_, company1_.NAME as NAME16_1_ from EMPLOYEE employee0_ left outer join COMPANY company1_ on employee0_.COMPANY_ID=company1_.ID
     */
    public void testWithoutModelForBothEntities1()
    {
        // For test purpose, make sure clear all the hibernate second level cache first.
        this.sessionFactory.evict(Company.class);
        Session session = this.sessionFactory.openSession();
        List list = session.createQuery("from Employee e left join fetch e.company").list();
        session.close();
        for (Object object : list) {
            Employee employee = (Employee) object;
            System.out.println(employee.getName() + " " + employee.getCompany().getName() );
        }
        System.out.println("test without model for both entities1 finished.\n");
    }
    
    /**
     * The resolution 2 for: n+1 SQL select queries performance issue.
     * Use the hibernate first level cache by default, all the retreived company infos will be cached in one session scope.
     * Three SQL will be produced by hibernate.
     * 
     * Hibernate: select employee0_.ID as ID21_, employee0_.NAME as NAME21_, employee0_.COMPANY_ID as COMPANY3_21_ from EMPLOYEE employee0_
     * Hibernate: select company0_.ID as ID20_0_, company0_.NAME as NAME20_0_ from COMPANY company0_ where company0_.ID=?
     * Hibernate: select company0_.ID as ID20_0_, company0_.NAME as NAME20_0_ from COMPANY company0_ where company0_.ID=?
     */
    public void testWithoutModelForBothEntities2()
    {
        // For test purpose, make sure clear all the hibernate second level cache first.
        this.sessionFactory.evict(Company.class);
        Session session = this.sessionFactory.openSession();
        List list = session.createQuery("from Employee").list();
        for (Object object : list) {
            Employee employee = (Employee) object;
            Hibernate.initialize(employee.getCompany());
        }
        session.close();
        System.out.println("test without model for both entities2 finished.\n");
    }
}
