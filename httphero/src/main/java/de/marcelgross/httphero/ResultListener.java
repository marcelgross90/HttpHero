package de.marcelgross.httphero;


public interface ResultListener {

	void onSuccess(HttpHeroResponse response);
	void onFailure();
}
