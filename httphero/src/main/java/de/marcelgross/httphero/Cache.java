package de.marcelgross.httphero;

public interface Cache {

	void put(String key, CacheEntry entry);
	CacheEntry get(String key);
	void remove(String key);
	void clear();
}
