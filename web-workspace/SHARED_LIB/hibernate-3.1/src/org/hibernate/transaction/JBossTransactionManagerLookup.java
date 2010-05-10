//$Id: JBossTransactionManagerLookup.java,v 1.3 2005/10/18 16:23:33 steveebersole Exp $
package org.hibernate.transaction;

/**
 * A <tt>TransactionManager</tt> lookup strategy for JBoss
 * @author Gavin King
 */
public final class JBossTransactionManagerLookup extends JNDITransactionManagerLookup {

	protected String getName() {
		return "java:/TransactionManager";
	}

	public String getUserTransactionName() {
		return "UserTransaction";
	}

}
