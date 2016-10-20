package de.marcelgross.httphero.request.property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CachingInformation {

	private String etag;

	public CachingInformation() {
	}

	public CachingInformation(String etag) {
		this.etag = etag;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public static Map<String, List<String>> getEtagHeader(String etag) {
		Map<String, List<String>> cacheHeader = new HashMap<>();
		List<String> headerValue = new LinkedList<>();
		headerValue.add(etag);
		cacheHeader.put("If-None-Match", headerValue);
		return cacheHeader;
	}
}
