# HttpHero
A lightweight, asynchronous, Http Library which simplifies android HTTP Communication.

It caches the GET-Requests to save some network traffic.

### Include into your Project (AndroidStudio) 

```
    File -> New -> New Module -> Import .JAR/.AAR Package
```

### Simple GET Request (no Cache):

```java
	        Request.Builder builder = new Request.Builder();
            builder.setUriTemplate("https://www.google.de/");
            builder.setMediaType("application/json");
            
            Request request = builder.get();
            HttpHero httpHero = HttpHero.getInstance();
            httpHero.performRequest(request, new HttpHeroResultListener() {
            	@Override
            	public void onSuccess(HttpHeroResponse httpHeroResponse) {
            		//todo Process with the Response
            	}
            
            	@Override
            	public void onFailure() {
            		Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            	}
            });
```

### Simple GET Request:

```java
	        Request.Builder builder = new Request.Builder();
            builder.setUriTemplate("https://www.google.de/");
            builder.setMediaType("application/json");
            
            Request request = builder.get();
            Cache cache = new BaseCache();
            HttpHero httpHero = HttpHero.getInstance(cache);
            httpHero.performRequest(request, new HttpHeroResultListener() {
            	@Override
            	public void onSuccess(HttpHeroResponse httpHeroResponse) {
            		//todo Process with the Response
            	}
            
            	@Override
            	public void onFailure() {
            		Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            	}
            });
```

### Custom Cache-Implementation

If you wish to implement your own cache, you can do so by implement the interface Cache.
