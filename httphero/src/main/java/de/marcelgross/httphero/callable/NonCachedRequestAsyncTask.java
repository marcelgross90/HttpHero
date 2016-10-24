package de.marcelgross.httphero.callable;


import java.net.HttpURLConnection;
import java.net.URL;

import de.marcelgross.httphero.HttpHeroResponse;
import de.marcelgross.httphero.ResultListener;
import de.marcelgross.httphero.request.Request;

public class NonCachedRequestAsyncTask extends RequestAsyncTask {

	public NonCachedRequestAsyncTask(ResultListener listener) {
		super(listener);
	}

	@Override
	protected HttpHeroResponse doInBackground(Request... requests) {
		Request request = requests[0];
		try {
			final URL uri = new URL(request.getUriTemplate());
			final HttpURLConnection con = (HttpURLConnection) uri.openConnection();

			con.setDoInput(true);

			setHttpVerb(con, request);
			setAuthentication(con, request);
			setApiKey(con, request);
			setMediaTypeDependentOfVerb(con, request);
			writeRequestBody(con, request);

			return new HttpHeroResponse(con);
		} catch (final Exception e) {
			android.util.Log.d("HttpHero", e.getMessage());
			return new HttpHeroResponse();
		}
	}

	@Override
	protected void onPostExecute(HttpHeroResponse response) {
		super.onPostExecute(response);
		if (response.getStatusCode() == 500) {
			listener.onFailure();
		} else {
			listener.onSuccess(response);
		}
	}
}