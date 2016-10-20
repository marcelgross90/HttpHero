package de.marcelgross.httphero;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.marcelgross.httphero.callable.CachedRequestCallable;
import de.marcelgross.httphero.callable.NonCachedRequestCallable;
import de.marcelgross.httphero.callable.RequestCallable;
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
		HttpHeroResponse response;
		if (isCacheEnabled()) {
			response = cachedRequest(request);
		} else {
			response = normalRequest(request);
		}

		if (response == null) {
			listener.onFailure();
		} else {
			listener.onSuccess(response);
		}
	}

	private HttpHeroResponse cachedRequest(Request request) {
		CacheEntry entry = cache.get(request.getUriTemplate());
		if (entry != null && entry.getEtag() != null) {
			request.setRequestHeader(getEtagHeader(entry.getEtag()));
		}
		RequestCallable requestCallable = new CachedRequestCallable(request);

		HttpHeroResponse response = executeRequest(requestCallable);
		if (response == null) {
			return null;
		} else {
			if (response.getStatusCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
				response = new HttpHeroResponse(entry);
			} else {
				if (request.getHttpVerb() == HttpVerb.GET) {
					cache.put(request.getUriTemplate(), new CacheEntry(response));
				} else if (request.getHttpVerb() == HttpVerb.DELETE) {
					cache.remove(request.getUriTemplate());
				}
			}
			return response;
		}
	}

	private HttpHeroResponse normalRequest(Request request) {
		RequestCallable requestCallable = new NonCachedRequestCallable(request);

		return executeRequest(requestCallable);
	}

	private HttpHeroResponse executeRequest(RequestCallable requestCallable) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<HttpHeroResponse> result = executor.submit(requestCallable);

		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		} finally {
			executor.shutdown();
		}
	}

	private boolean isCacheEnabled() {
		return cache != null;
	}
}
