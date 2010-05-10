// $Id: JoinProcessor.java,v 1.4 2005/10/06 20:10:46 steveebersole Exp $
package org.hibernate.hql.ast.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collections;

import org.hibernate.AssertionFailure;
import org.hibernate.engine.JoinSequence;
import org.hibernate.hql.antlr.SqlTokenTypes;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.hibernate.hql.ast.tree.FromClause;
import org.hibernate.hql.ast.tree.FromElement;
import org.hibernate.hql.ast.tree.QueryNode;
import org.hibernate.sql.JoinFragment;
import org.hibernate.util.StringHelper;

import antlr.ASTFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Performs the post-processing of the join information gathered during semantic analysis.
 * The join generating classes are complex, this encapsulates some of the JoinSequence-related
 * code.
 *
 * @author josh Jul 22, 2004 7:33:42 AM
 */
public class JoinProcessor implements SqlTokenTypes {

	private static final Log log = LogFactory.getLog( JoinProcessor.class );

	private QueryTranslatorImpl queryTranslatorImpl;
	private SyntheticAndFactory andFactory;

	/**
	 * Constructs a new JoinProcessor.
	 *
	 * @param astFactory The factory for AST node creation.
	 * @param queryTranslatorImpl The query translator.
	 */
	public JoinProcessor(ASTFactory astFactory, QueryTranslatorImpl queryTranslatorImpl) {
		this.andFactory = new SyntheticAndFactory( astFactory );
		this.queryTranslatorImpl = queryTranslatorImpl;
	}

	/**
	 * Translates an AST join type (i.e., the token type) into a JoinFragment.XXX join type.
	 *
	 * @param astJoinType The AST join type (from HqlSqlTokenTypes or SqlTokenTypes)
	 * @return a JoinFragment.XXX join type.
	 * @see JoinFragment
	 * @see SqlTokenTypes
	 */
	public static int toHibernateJoinType(int astJoinType) {
		switch ( astJoinType ) {
			case LEFT_OUTER:
				return JoinFragment.LEFT_OUTER_JOIN;
			case INNER:
				return JoinFragment.INNER_JOIN;
			case RIGHT_OUTER:
				return JoinFragment.RIGHT_OUTER_JOIN;
			default:
				throw new AssertionFailure( "undefined join type " + astJoinType );
		}
	}

	public void processJoins(QueryNode query, boolean inSubquery) {
		final FromClause fromClause = query.getFromClause();

		// TODO : found it easiest to simply reorder the FromElements here into ascending order
		// in terms of injecting them into the resulting sql ast in orders relative to those
		// expected by the old parser; this is definitely another of those "only needed
		// for regression purposes".  The SyntheticAndFactory, then, simply injects them as it
		// encounters them.
		ArrayList orderedFromElements = new ArrayList();
		ListIterator liter = fromClause.getFromElements().listIterator( fromClause.getFromElements().size() );
		while ( liter.hasPrevious() ) {
			orderedFromElements.add( liter.previous() );
		}

		// Iterate through the alias,JoinSequence pairs and generate SQL token nodes.
		Iterator iter = orderedFromElements.iterator();
		while ( iter.hasNext() ) {
			final FromElement fromElement = ( FromElement ) iter.next();
			JoinSequence join = fromElement.getJoinSequence();
			join.setSelector(
			        new JoinSequence.Selector() {
				        public boolean includeSubclasses(String alias) {
					        boolean shallowQuery = queryTranslatorImpl.isShallowQuery();
					        boolean containsTableAlias = fromClause.containsTableAlias( alias );
					        boolean includeSubclasses = fromElement.isIncludeSubclasses();
					        boolean subQuery = fromClause.isSubQuery();
					        return includeSubclasses && containsTableAlias && !subQuery && !shallowQuery;
				        }
			        }
				);
			addJoinNodes( query, join, fromElement, inSubquery );
		} // while

	}

	private void addJoinNodes(QueryNode query, JoinSequence join, FromElement fromElement, boolean inSubquery) {
		// Generate FROM and WHERE fragments for the from element.
		JoinFragment joinFragment = join.toJoinFragment(
				inSubquery ? Collections.EMPTY_MAP : queryTranslatorImpl.getEnabledFilters(),
				fromElement.useFromFragment(),
		        fromElement.getAdHocOnClauseFragment()
			);

		String frag = joinFragment.toFromFragmentString();
		String whereFrag = joinFragment.toWhereFragmentString();

		// If the from element represents a JOIN_FRAGMENT and it is
		// a theta-style join, convert its type from JOIN_FRAGMENT
		// to FROM_FRAGMENT
		if ( fromElement.getType() == JOIN_FRAGMENT &&
				( join.isThetaStyle() || StringHelper.isNotEmpty( whereFrag ) ) ) {
			fromElement.setType( FROM_FRAGMENT );
			fromElement.getJoinSequence().setUseThetaStyle( true ); // this is used during SqlGenerator processing
		}

		// If there is a FROM fragment and the FROM element is an explicit, then add the from part.
		if ( fromElement.useFromFragment() /*&& StringHelper.isNotEmpty( frag )*/ ) {
			String fromFragment = processFromFragment( frag, join );
			if ( log.isDebugEnabled() ) log.debug( "Using FROM fragment [" + fromFragment + "]" );
			fromElement.setText( fromFragment.trim() ); // Set the text of the fromElement.
		}
		andFactory.addWhereFragment( joinFragment, whereFrag, query, fromElement );
	}

	private String processFromFragment(String frag, JoinSequence join) {
		String fromFragment = frag.trim();
		// The FROM fragment will probably begin with ', '.  Remove this if it is present.
		if ( fromFragment.startsWith( ", " ) ) {
			fromFragment = fromFragment.substring( 2 );
		}
		/*
		// *** BEGIN FROM FRAGMENT VOODOO ***
		// If there is more than one join, reverse the order of the tables in the FROM fragment.
		if ( join.getJoinCount() > 1 && fromFragment.indexOf( ',' ) >= 0 ) {
			String[] froms = StringHelper.split( ",", fromFragment );
			StringBuffer buf = new StringBuffer();
			for ( int i = froms.length - 1; i >= 0; i-- ) {
				buf.append( froms[i] );
				if ( i > 0 ) {
					buf.append( ", " );
				}
			}
			fromFragment = buf.toString();
		}
		// *** END OF FROM FRAGMENT VOODOO ***
		*/
		return fromFragment;
	}

}
