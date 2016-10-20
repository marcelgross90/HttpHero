package de.marcelgross.httphero.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.marcelgross.httphero.request.property.ApiKey;
import de.marcelgross.httphero.request.property.Authentication;
import de.marcelgross.httphero.request.property.HttpVerb;

public class Request {

	private Authentication authentication;
	private ApiKey apiKey;
	private String uriTemplate;
	private String mediaType;
	private HttpVerb httpVerb;
	private byte[] requestBody;
	private Map<String, List<String>> requestHeader;

	public Authentication getAuthentication() {
		return authentication;
	}

	public ApiKey getApiKey() {
		return apiKey;
	}

	public String getUriTemplate() {
		return uriTemplate;
	}

	public String getMediaType() {
		return mediaType;
	}

	public HttpVerb getHttpVerb() {
		return httpVerb;
	}

	public byte[] getRequestBody() {
		return requestBody;
	}

	public Map<String, List<String>> getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Map<String, List<String>> requestHeader) {
		this.requestHeader = requestHeader;
	}

	private Request(Builder builder) {
		this.requestHeader = new HashMap<>();
		this.authentication = builder.authentication;
		this.apiKey = builder.apiKey;
		this.uriTemplate = builder.uriTemplate;
		this.mediaType = builder.mediaType;
		this.httpVerb = builder.httpVerb;
		this.requestBody = builder.requestBody;
	}

	public static class Builder {
		private Authentication authentication;
		private ApiKey apiKey;
		private String uriTemplate;
		private String mediaType;
		private HttpVerb httpVerb;
		private byte[] requestBody;

		public Builder() {
		}

		public Builder setAuthentication(Authentication authentication) {
			this.authentication = authentication;
			return this;
		}

		public Builder setApiKey(ApiKey apiKey) {
			this.apiKey = apiKey;
			return this;
		}

		public Builder setUriTemplate(String uriTemplate) {
			this.uriTemplate = uriTemplate;
			return this;
		}

		public Builder setMediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}

		public Request get() {
			this.httpVerb = HttpVerb.GET;
			return new Request(this);
		}

		public Request post(byte[] requestBody) {
			this.httpVerb = HttpVerb.POST;
			this.requestBody = requestBody;
			return new Request(this);
		}

		public Request put(byte[] requestBody) {
			this.httpVerb = HttpVerb.PUT;
			this.requestBody = requestBody;
			return new Request(this);
		}

		public Request delete() {
			this.httpVerb = HttpVerb.DELETE;
			return new Request(this);
		}
	}
}
