package com.loadtrend.test;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.loadtrend.daos.UserDAO;

import junit.framework.TestCase;

public class TestSpringTransaction extends TestCase {
    
    private UserDAO userDAO = null;
    
    private TransactionTemplate transactionTemplate = null;

    protected void setUp() throws Exception {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/test_spring?autoReconnect=true");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("1234");
        
        this.userDAO = new UserDAO();
        userDAO.setDataSource(basicDataSource);
        
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(basicDataSource);
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    protected void tearDown() throws Exception {
    }
    
    public void testSpringTransaction() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                userDAO.insertUser();
            }
        });
    }
}
