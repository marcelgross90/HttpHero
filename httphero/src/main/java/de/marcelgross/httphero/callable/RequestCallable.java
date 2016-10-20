package de.marcelgross.httphero.callable;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import de.marcelgross.httphero.HttpHeroResponse;
import de.marcelgross.httphero.request.Request;
import de.marcelgross.httphero.request.property.ApiKey;
import de.marcelgross.httphero.request.property.Authentication;
import de.marcelgross.httphero.request.property.HttpVerb;

public abstract class RequestCallable  implements Callable<HttpHeroResponse> {

	Request request;

	public RequestCallable(Request request) {
		this.request = request;
	}

	public void setHttpVerb(final HttpURLConnection con) throws Exception {
		final HttpVerb httpVerb = request.getHttpVerb();
		con.setRequestMethod(httpVerb.name());
	}

	public void setApiKey(final HttpURLConnection con) {
		final ApiKey apiKey = request.getApiKey();

		if (apiKey != null) {
			con.setRequestProperty(apiKey.getApiKeyHeader(), apiKey.getApiKeyValue());
		}
	}

	public void setAuthentication(final HttpURLConnection con) {
		final Authentication auth = request.getAuthentication();

		if (auth != null) {
			con.setRequestProperty("Authorization", auth.authenticationHeader());
		}
	}

	public void setMediaTypeDependentOfVerb(final HttpURLConnection connection) {
		final HttpVerb httpVerb = request.getHttpVerb();
		final String mediaType = request.getMediaType();

		if (httpVerb.equals(HttpVerb.GET)) {
			connection.setRequestProperty("Accept", mediaType);
		} else {
			connection.setRequestProperty("Content-Type", mediaType);
		}
	}

	public void setRequestHeader(final HttpURLConnection con) {
		Map<String, List<String>> headers = request.getRequestHeader();

		if (headers.size() > 0) {
			for (String headerKey : headers.keySet()) {
				for (String headerValue : headers.get(headerKey)) {
					con.setRequestProperty(headerKey, headerValue);
				}
			}
		}
	}

	public void writeRequestBody(final HttpURLConnection connection) throws Exception {
		if (request.getRequestBody() != null) {
			connection.setDoOutput(true);
			DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream());
			dataStream.write(request.getRequestBody());
			dataStream.flush();
		}
	}
}
