package de.marcelgross.httphero;

import java.util.concurrent.ConcurrentHashMap;

public class BaseCache implements Cache{

	private static BaseCache instance;
	private ConcurrentHashMap<String, CacheEntry> entries;

	public BaseCache() {
		this.entries = new ConcurrentHashMap<>();
	}

	@Override
	public void put(String url, CacheEntry entry){
		this.entries.put(url, entry);
	}

	@Override
	public CacheEntry get(String url) {
		return this.entries.get(url);
	}

	@Override
	public void remove(String url) {
		this.entries.remove(url);
	}

	@Override
	public void clear() {
		this.entries.clear();
	}
}
