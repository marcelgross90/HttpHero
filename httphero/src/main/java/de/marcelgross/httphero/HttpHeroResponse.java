package de.marcelgross.httphero;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.marcelgross.httphero.request.property.CachingInformation;

public class HttpHeroResponse {
	public static final String HEADER_TOTALNUMBEROFRESULTS = "X-totalnumberofresults";
	public static final String HEADER_NUMBEROFRESULTS = "X-numberofresults";
	private static final int MAXIMUM_RESPONSE_SIZE = 1048576;

	private int statusCode;
	private byte[] data;
	private Map<String, List<String>> headers;
	private Map<String, Link> mapRelationTypeToLink;
	private CachingInformation cachingInformation;
	private int numberOfResults;
	private int totalNumberOfResults;
	private String mediaType;
	private String locationUri;

	public HttpHeroResponse(HttpURLConnection con) {
		this.mapRelationTypeToLink = new HashMap<>();
		processResponse(con);
	}

	public HttpHeroResponse(CacheEntry entry) {
		this.mapRelationTypeToLink = new HashMap<>();
		processResponse(entry);
	}

	public HttpHeroResponse() {
		this.statusCode = 500;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public byte[] getData() {
		return data;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public Map<String, Link> getMapRelationTypeToLink() {
		return mapRelationTypeToLink;
	}

	public int getNumberOfResults() {
		return numberOfResults;
	}

	public int getTotalNumberOfResults() {
		return totalNumberOfResults;
	}

	public String getMediaType() {
		return mediaType;
	}

	public String getLocationUri() {
		return locationUri;
	}

	public CachingInformation getCachingInformation() {
		return cachingInformation;
	}

	private void processResponse(final CacheEntry entry) {
		this.statusCode = HttpURLConnection.HTTP_OK;
		this.data = entry.getData();
		this.headers = entry.getResponseHeaders();
		this.cachingInformation = new CachingInformation(entry.getEtag());
		setMapRelationTypeToLink(entry.getResponseHeaders());
		setStandardHeader(entry.getResponseHeaders());
	}

	private void processResponse(final HttpURLConnection con) {
		try {
			readStatusCode(con);

			if (this.statusCode < 400) {
				readHeader(con);
				setStandardHeader(con.getHeaderFields());
				setMapRelationTypeToLink(con.getHeaderFields());
				readCachingInformation(con);
				readInput(con);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void readStatusCode(final HttpURLConnection con) throws Exception {
		this.statusCode = con.getResponseCode();
	}

	private void readHeader(final HttpURLConnection con) throws Exception {
		this.headers = con.getHeaderFields();
	}

	private void readCachingInformation(final HttpURLConnection con) throws Exception {
		this.cachingInformation = new CachingInformation(con.getHeaderField("Etag"));
	}

	private void readInput(final HttpURLConnection con) throws Exception {
		InputStream inputStream = con.getInputStream();
		ByteArrayOutputStream responseData = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];

		int length = 0;
		int bytes = inputStream.read(buffer);

		while (bytes > -1) {
			if (bytes > 0) {
				responseData.write(buffer, 0, bytes);
				length += bytes;

				if (length > MAXIMUM_RESPONSE_SIZE)
					this.data = new byte[0];
			}
			bytes = inputStream.read(buffer);
		}
		this.data = responseData.toByteArray();
	}

	private void setMapRelationTypeToLink(final Map<String, List<String>> headers) {
		final List<String> linkHeaders = headers.get("Link");

		if (linkHeaders != null) {
			for (final String linkHeader : linkHeaders) {
				final Link link = Link.parseFromHttpHeader(linkHeader);
				this.mapRelationTypeToLink.put(link.getRelationType(), link);
			}
		}
	}

	private void setStandardHeader(final Map<String, List<String>> headers) {
		String totalNumberOfResults = extractHeaders(headers, HEADER_TOTALNUMBEROFRESULTS);
		if (!totalNumberOfResults.isEmpty()) {
			this.totalNumberOfResults = Integer.valueOf(totalNumberOfResults);
		}
		String numberOfResults = extractHeaders(headers, HEADER_NUMBEROFRESULTS);
		if (!numberOfResults.isEmpty()) {
			this.numberOfResults = Integer.valueOf(numberOfResults);
		}
		this.mediaType = extractHeaders(headers,"Content-Type");
		this.locationUri = extractHeaders(headers,"Location");
	}

	private String extractHeaders(final Map<String, List<String>> headers, String headerKey) {
		final List<String> headerValues = headers.get(headerKey);

		if (headerValues != null) {
			return headerValues.get(0);
		}
		return "";
	}
}
