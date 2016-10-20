package de.marcelgross.httphero;

public interface HttpHeroResultListener {

	void onSuccess(HttpHeroResponse response);
	void onFailure();
}
