//$Id: ASTQueryTranslatorFactory.java,v 1.6 2005/07/12 20:27:24 steveebersole Exp $
package org.hibernate.hql.ast;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.QueryTranslatorFactory;

import java.util.Map;

/**
 * @author Gavin King
 */
public class ASTQueryTranslatorFactory implements QueryTranslatorFactory {

	private static final Log log = LogFactory.getLog( ASTQueryTranslatorFactory.class );

	public ASTQueryTranslatorFactory() {
		log.info( "Using ASTQueryTranslatorFactory" );
	}

	/**
	 * @see QueryTranslatorFactory#createQueryTranslator
	 */
	public QueryTranslator createQueryTranslator(
	        String queryString,
	        Map filters,
	        SessionFactoryImplementor factory) {
		return new QueryTranslatorImpl( queryString, filters, factory );
	}

	/**
	 * @see QueryTranslatorFactory#createFilterTranslator
	 */
	public FilterTranslator createFilterTranslator(
	        String queryString,
	        Map filters,
	        SessionFactoryImplementor factory) {
		return new QueryTranslatorImpl( queryString, filters, factory );
	}

}
