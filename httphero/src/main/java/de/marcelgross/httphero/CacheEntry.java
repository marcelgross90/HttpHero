package de.marcelgross.httphero;

import java.util.List;
import java.util.Map;

import de.marcelgross.httphero.request.property.CachingInformation;

public class CacheEntry {
	private byte[] data;
	private String etag;
	private long serverDate;
	private long lastModified;
	private long ttl;
	private long softTtl;
	private Map<String, List<String>> responseHeaders;


	public CacheEntry() {
		android.util.Log.d("HttpHero", "saved in Cache");
	}
	public CacheEntry(byte[] data, String etag, long serverDate, long lastModified, long ttl, long softTtl, Map<String, List<String>> responseHeaders) {
		this.data = data;
		this.etag = etag;
		this.serverDate = serverDate;
		this.lastModified = lastModified;
		this.ttl = ttl;
		this.softTtl = softTtl;
		this.responseHeaders = responseHeaders;
	}

	public CacheEntry(HttpHeroResponse response) {
		android.util.Log.d("HttpHero", "saved in Cache");
		this.data = response.getData();
		this.responseHeaders = response.getHeaders();
		CachingInformation cachingInformation = response.getCachingInformation();
		if (cachingInformation != null) {
			this.etag = cachingInformation.getEtag();
		}
	}

	public byte[] getData() {
		return data;
	}

	public String getEtag() {
		return etag;
	}

	public long getServerDate() {
		return serverDate;
	}

	public long getLastModified() {
		return lastModified;
	}

	public long getTtl() {
		return ttl;
	}

	public long getSoftTtl() {
		return softTtl;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}
}
