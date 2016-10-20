package de.marcelgross.httphero.callable;


import java.net.HttpURLConnection;
import java.net.URL;

import de.marcelgross.httphero.HttpHeroResponse;
import de.marcelgross.httphero.request.Request;

public class NonCachedRequestCallable extends RequestCallable {

	public NonCachedRequestCallable(Request request) {
		super(request);
	}

	@Override
	public HttpHeroResponse call() throws Exception {
		try {
			final URL uri = new URL(request.getUriTemplate());
			final HttpURLConnection con = (HttpURLConnection) uri.openConnection();

			con.setDoInput(true);

			setHttpVerb(con);
			setAuthentication(con);
			setApiKey(con);
			setMediaTypeDependentOfVerb(con);
			writeRequestBody(con);

			return new HttpHeroResponse(con);
		} catch (final Exception e) {
			android.util.Log.d("HttpHero", e.getMessage());
			return new HttpHeroResponse();
		}
	}
}