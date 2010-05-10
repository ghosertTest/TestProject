//$Id: MapProxy.java,v 1.3 2005/02/12 07:19:45 steveebersole Exp $
package org.hibernate.proxy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Proxy for "dynamic-map" entity representations.
 *
 * @author Gavin King
 */
public class MapProxy implements HibernateProxy, Map, Serializable {

	private MapLazyInitializer li;

	MapProxy(MapLazyInitializer li) {
		this.li = li;
	}

	public Object writeReplace() {
		return this;
	}

	public LazyInitializer getHibernateLazyInitializer() {
		return li;
	}

	public int size() {
		return li.getMap().size();
	}

	public void clear() {
		li.getMap().clear();
	}

	public boolean isEmpty() {
		return li.getMap().isEmpty();
	}

	public boolean containsKey(Object key) {
		return li.getMap().containsKey(key);
	}

	public boolean containsValue(Object value) {
		return li.getMap().containsValue(value);
	}

	public Collection values() {
		return li.getMap().values();
	}

	public void putAll(Map t) {
		li.getMap().putAll(t);
	}

	public Set entrySet() {
		return li.getMap().entrySet();
	}

	public Set keySet() {
		return li.getMap().keySet();
	}

	public Object get(Object key) {
		return li.getMap().get(key);
	}

	public Object remove(Object key) {
		return li.getMap().remove(key);
	}

	public Object put(Object key, Object value) {
		return li.getMap().put(key, value);
	}

}
