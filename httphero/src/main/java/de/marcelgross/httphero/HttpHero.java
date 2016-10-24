package de.marcelgross.httphero;

import java.net.HttpURLConnection;

import de.marcelgross.httphero.callable.CachedRequestAsyncTask;
import de.marcelgross.httphero.callable.NonCachedRequestAsyncTask;
import de.marcelgross.httphero.request.Request;
import de.marcelgross.httphero.request.property.HttpVerb;

import static de.marcelgross.httphero.request.property.CachingInformation.getEtagHeader;

public class HttpHero {

	private static HttpHero instance;
	private Cache cache;

	public static HttpHero getInstance(Cache cache) {
		if (instance == null) {
			instance = new HttpHero(cache);
		}
		return instance;
	}

	public static HttpHero getInstance() {
		return getInstance(null);
	}

	private HttpHero(Cache cache) {
		this.cache = cache;
	}

	private HttpHero() {
		this(null);
	}

	public void performRequest(Request request, HttpHeroResultListener listener) {
		if (isCacheEnabled()) {
			cachedRequest(request, listener);
		} else {
			normalRequest(request, listener);
		}
	}

	private void cachedRequest(final Request request, final HttpHeroResultListener listener) {
		final CacheEntry entry = cache.get(request.getUriTemplate());
		if (entry != null && entry.getEtag() != null) {
			request.setRequestHeader(getEtagHeader(entry.getEtag()));
		}
		new CachedRequestAsyncTask(new ResultListener() {
			@Override
			public void onSuccess(HttpHeroResponse response) {
				if (response.getStatusCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
					response = new HttpHeroResponse(entry);
				} else {
					if (request.getHttpVerb() == HttpVerb.GET) {
						cache.put(request.getUriTemplate(), new CacheEntry(response));
					} else if (request.getHttpVerb() == HttpVerb.DELETE) {
						cache.remove(request.getUriTemplate());
					}
				}
				listener.onSuccess(response);
			}

			@Override
			public void onFailure() {
				listener.onFailure();
			}
		}).execute(request);
	}

	private void normalRequest(Request request, final HttpHeroResultListener listener) {
		new NonCachedRequestAsyncTask(new ResultListener() {
			@Override
			public void onSuccess(HttpHeroResponse response) {
				listener.onSuccess(response);
			}

			@Override
			public void onFailure() {
				listener.onFailure();
			}
		}).execute(request);
	}

	private boolean isCacheEnabled() {
		return cache != null;
	}
}
