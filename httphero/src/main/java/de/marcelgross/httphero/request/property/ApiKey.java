package de.marcelgross.httphero.request.property;

public class ApiKey {
	private final String apiKeyHeader;

	private final String apiKeyValue;

	public ApiKey(final String apiKeyHeader, final String apiKeyValue) {
		this.apiKeyHeader = apiKeyHeader;
		this.apiKeyValue = apiKeyValue;
	}

	public String getApiKeyHeader() {
		return this.apiKeyHeader;
	}

	public String getApiKeyValue() {
		return this.apiKeyValue;
	}
}