//$Id: CascadeStyle.java,v 1.3 2005/08/22 17:09:44 oneovthafew Exp $
package org.hibernate.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.util.ArrayHelper;

/**
 * A style of cascade that can be specified by the mapping for an association.
 * The style is specified by the <tt>cascade</tt> attribute in the mapping file.
 * 
 * @author Gavin King
 */
public abstract class CascadeStyle implements Serializable {
	
	/**
	 * Should the given action be cascaded?
	 */
	public abstract boolean doCascade(CascadingAction action);
	
	/**
	 * Should the given action really, really be cascaded?
	 */
	public boolean reallyDoCascade(CascadingAction action) {
		return doCascade(action);
	}
	
	/**
	 * Do we need to delete orphaned collection elements?
	 */
	public boolean hasOrphanDelete() {
		return false;
	}

	public static final class MultipleCascadeStyle extends CascadeStyle {
		private final CascadeStyle[] styles;
		public MultipleCascadeStyle(CascadeStyle[] styles) {
			this.styles = styles;
		}
		public boolean doCascade(CascadingAction action) {
			for (int i=0; i<styles.length; i++) {
				if ( styles[i].doCascade(action) ) return true;
			}
			return false;
		}
		public boolean reallyDoCascade(CascadingAction action) {
			for (int i=0; i<styles.length; i++) {
				if ( styles[i].reallyDoCascade(action) ) return true;
			}
			return false;
		}
		public boolean hasOrphanDelete() {
			for (int i=0; i<styles.length; i++) {
				if ( styles[i].hasOrphanDelete() ) return true;
			}
			return false;
		}
		public String toString() {
			return ArrayHelper.toString(styles);
		}
	}
	
	/**
	 * save / delete / update / evict / lock / replicate / merge / persist + delete orphans
	 */
	public static final CascadeStyle ALL_DELETE_ORPHAN = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return true;
		}
		public boolean hasOrphanDelete() {
			return true;
		}
		public String toString() {
			return "STYLE_ALL_DELETE_ORPHAN";
		}
	};
	
	/**
	 * save / delete / update / evict / lock / replicate / merge / persist
	 */
	public static final CascadeStyle ALL = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return true;
		}
		public String toString() {
			return "STYLE_ALL";
		}
	};
	
	/**
	 * save / update
	 */
	public static final CascadeStyle UPDATE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.SAVE_UPDATE || action==CascadingAction.SAVE_UPDATE_COPY;
		}
		public String toString() {
			return "STYLE_SAVE_UPDATE";
		}
	};
	
	/**
	 * lock
	 */
	public static final CascadeStyle LOCK = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.LOCK;
		}
		public String toString() {
			return "STYLE_LOCK";
		}
	};
	
	/**
	 * refresh
	 */
	public static final CascadeStyle REFRESH = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.REFRESH;
		}
		public String toString() {
			return "STYLE_REFRESH";
		}
	};
	
	/**
	 * evict
	 */
	public static final CascadeStyle EVICT = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.EVICT;
		}
		public String toString() {
			return "STYLE_EVICT";
		}
	};
	
	/**
	 * replicate
	 */
	public static final CascadeStyle REPLICATE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.REPLICATE;
		}
		public String toString() {
			return "STYLE_REPLICATE";
		}
	};
	/**
	 * merge
	 */
	public static final CascadeStyle MERGE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.MERGE;
		}
		public String toString() {
			return "STYLE_MERGE";
		}
	};
	
	/**
	 * create
	 */
	public static final CascadeStyle PERSIST = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.PERSIST
				|| action==CascadingAction.PERSIST_ON_FLUSH;
		}
		public String toString() {
			return "STYLE_PERSIST";
		}
	};
	
	/**
	 * delete
	 */
	public static final CascadeStyle DELETE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.DELETE;
		}
		public String toString() {
			return "STYLE_DELETE";
		}
	};
	
	/**
	 * delete + delete orphans
	 */
	public static final CascadeStyle DELETE_ORPHAN = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action==CascadingAction.DELETE || action==CascadingAction.SAVE_UPDATE;
		}
		public boolean reallyDoCascade(CascadingAction action) {
			return action==CascadingAction.DELETE;
		}
		public boolean hasOrphanDelete() {
			return true;
		}
		public String toString() {
			return "STYLE_DELETE_ORPHAN";
		}
	};
	
	/**
	 * no cascades
	 */
	public static final CascadeStyle NONE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return false;
		}
		public String toString() {
			return "STYLE_NONE";
		}
	};
	
	CascadeStyle() {}
	
	static final Map STYLES = new HashMap();
	static {
		STYLES.put("all", ALL);
		STYLES.put("all-delete-orphan", ALL_DELETE_ORPHAN);
		STYLES.put("save-update", UPDATE);
		STYLES.put("persist", PERSIST);
		STYLES.put("merge", MERGE);
		STYLES.put("lock", LOCK);
		STYLES.put("refresh", REFRESH);
		STYLES.put("replicate", REPLICATE);
		STYLES.put("evict", EVICT);
		STYLES.put("delete", DELETE);
		STYLES.put("delete-orphan", DELETE_ORPHAN);
		STYLES.put("none", NONE);
	}
	
	public static CascadeStyle getCascadeStyle(String cascade) {
		CascadeStyle style = (CascadeStyle) STYLES.get(cascade);
		if (style==null) {
			throw new MappingException("Unsupported cascade style: " + cascade);
		}
		else {
			return style;
		}	
	}
}
