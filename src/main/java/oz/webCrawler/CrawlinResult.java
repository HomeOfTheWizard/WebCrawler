package oz.webCrawler;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CrawlinResult{
	
	private ConcurrentHashMap<URI, Set<URI>> resultMap;
	private ConcurrentHashMap<URI, URI> redirectMap;

	public CrawlinResult() {
		this.resultMap = new ConcurrentHashMap<>();
		this.redirectMap = new ConcurrentHashMap<>();
	}

	public ConcurrentHashMap<URI, Set<URI>> getResultMap() {
		return resultMap;
	}
	
	public ConcurrentHashMap<URI, URI> getRedirectMap() {
		return redirectMap;
	}
	
}