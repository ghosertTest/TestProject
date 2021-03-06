package com.loadtrend.bbs.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.loadtrend.bbs.dao.DAO;
import com.loadtrend.bbs.service.Manager;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 * 
 * <p><a href="BaseManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseManager implements Manager {
    protected final Log log = LogFactory.getLog(getClass());
    protected DAO dao = null;
    
    /**
     * @see org.appfuse.service.Manager#setDAO(org.appfuse.dao.DAO)
     */
    public void setDAO(DAO dao) {
        this.dao = dao;
    }
    
    /**
     * @see org.appfuse.service.Manager#getObject(java.lang.Class, java.io.Serializable)
     */
    public Object getObject(Class clazz, Serializable id) {
        return dao.getObject(clazz, id);
    }
    
    /**
     * @see org.appfuse.service.Manager#getObjects(java.lang.Class)
     */
    public List getObjects(Class clazz) {
        return dao.getObjects(clazz);
    }
    
    /**
     * @see org.appfuse.service.Manager#removeObject(java.lang.Class, java.io.Serializable)
     */
    public void removeObject(Class clazz, Serializable id) {
        dao.removeObject(clazz, id);
    }
    
    /**
     * @see org.appfuse.service.Manager#saveObject(java.lang.Object)
     */
    public void saveObject(Object o) {
        dao.saveObject(o);
    }
}
