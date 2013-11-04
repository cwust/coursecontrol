package br.com.wust.coursecontrol.convention;

import java.util.HashMap;
import java.util.Map;

public class BidirectionalMap <K, V> {
	private Map<K, V> map;
	private Map<V, K> inverseMap;
	
	public BidirectionalMap() {
		map = new HashMap<K, V>();
		inverseMap = new HashMap<V, K>();
	}
	
	public void put(K key, V value) {
		if (key != null && value != null) {
			map.put(key, value);
			inverseMap.put(value, key);
		}
	}
	
	public V get(K key) {
		return map.get(key);
	}

	public K inverseGet(V value) {
		return inverseMap.get(value);
	}
}
