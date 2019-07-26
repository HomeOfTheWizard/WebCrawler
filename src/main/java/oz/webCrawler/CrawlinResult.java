package oz.webCrawler;

import java.net.URI;
import java.util.HashSet;
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
	
	public void putResult(URI parent, Set<URI> childURIs) {
		this.resultMap.put(parent, childURIs);
	}
	
	public void putRedirect(URI parent, URI destination) {
		this.redirectMap.put(parent, destination);
	}
	
	public boolean tagAsProcessingIfNotAlready(URI parent){
		return this.resultMap.putIfAbsent(parent, new HashSet<URI>()) == null;
	}
	
	public URI getDestinationIfRedirected( URI uri) {
		return redirectMap.containsKey(uri) ? redirectMap.get(uri) : uri;
	}
	
	public boolean isRedirectedAlreadyInResults(URI uri) {
		return resultMap.containsKey(getDestinationIfRedirected(uri)) || redirectMap.containsValue(uri);
	}
	
	public boolean containsResultForKey(URI uri) {
		return resultMap.containsKey(uri);
	}
	
	public CrawlinResult cleanUpResults() {
		resultMap.keySet().removeAll(redirectMap.keySet());
		return this;
	}
	
}