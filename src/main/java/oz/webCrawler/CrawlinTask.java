package oz.webCrawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

public class CrawlinTask extends RecursiveAction{

	/**
	 *	Generated serial ID 
	 */
	private static final long serialVersionUID = 209963736630273702L;
	
	public URI baseURI;
	private URI parentURI;
	private ConcurrentHashMap<URI, Set<URI>> resultMap;
	private ConcurrentHashMap<URI, URI> redirectMap;
	private boolean isFirstCrawl;
	
	public CrawlinTask(URI baseURI, URI parentURI, ConcurrentHashMap<URI, Set<URI>> map, ConcurrentHashMap<URI, URI> redirectMap) {
		this.baseURI = baseURI;
		this.resultMap = map;
		this.parentURI = parentURI;
		this.isFirstCrawl = false;
		this.redirectMap = redirectMap;
	}

	public CrawlinTask(URI baseURI, ConcurrentHashMap<URI, Set<URI>> map, ConcurrentHashMap<URI, URI> redirectMap) {
		this.baseURI = baseURI;
		this.parentURI = baseURI;
		this.resultMap = map;
		this.isFirstCrawl = true;
		this.redirectMap = redirectMap;
	}

	@Override
	protected void compute() {
		resultMap.putIfAbsent(parentURI, new HashSet<URI>());
		
		ScrappinURIsResult scrappinURIsResult = scrappParentURIandGetChildURIs();
		System.out.println("got scrappin result for URI: " + parentURI);
		
		processScrappinResult(scrappinURIsResult);	
	}
	
	
	private ScrappinURIsResult scrappParentURIandGetChildURIs() {
		Scrapper scrapper = new Scrapper(baseURI);
		ScrappinURIsResult resultURIs = scrapper.getURIListfromParentURI(parentURI);
		return resultURIs;
	}
	
	
	private void processScrappinResult(ScrappinURIsResult scrappinURIsResult) {
		tagIfRedirectedOrFailed(scrappinURIsResult);
		
		if(scrappinURIsResult != null)
		{
			treatValidScrappinResult(scrappinURIsResult);
		}
	}

	
	private void tagIfRedirectedOrFailed(ScrappinURIsResult scrappinURIsResult) {
		if(scrappinURIsResult == null)									{ redirectMap.put(parentURI, parentURI); return; }
		if(!scrappinURIsResult.destinationURI.equals(parentURI)) 		redirectMap.put(parentURI, scrappinURIsResult.destinationURI);
		if(isFirstCrawl)												baseURI = scrappinURIsResult.destinationURI;
	}
	
	
	private void treatValidScrappinResult(ScrappinURIsResult scrappinURIsResult) {
		resultMap.put(scrappinURIsResult.destinationURI, scrappinURIsResult.getResult()); 	
		filterChildURISetFromAlreadyCrawledPages(scrappinURIsResult);
		crawlThroughChildURIs(scrappinURIsResult.getResult());
	}
	

	private void filterChildURISetFromAlreadyCrawledPages(ScrappinURIsResult scrappinURIsResult) {
		scrappinURIsResult.getResult()
		.removeIf( uri -> resultMap.containsKey(getDestinationIfRedirected(uri)) || resultMap.containsKey(uri) );
		
	}

	
	private URI getDestinationIfRedirected( URI uri) {
		return redirectMap.containsKey(uri) ? redirectMap.get(uri) : uri;
	}

	
	private void crawlThroughChildURIs(Set<URI> childURISet) {
		List<CrawlinTask> tasksToFork = new ArrayList<>();
		for(URI uri : childURISet) {
			CrawlinTask task = new CrawlinTask(baseURI, uri, resultMap, redirectMap);
			tasksToFork.add(task);
			task.fork();
		}
		tasksToFork.forEach(task -> task.join());
	}
}