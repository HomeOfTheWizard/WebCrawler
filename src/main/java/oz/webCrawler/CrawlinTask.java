package oz.webCrawler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

public class CrawlinTask extends RecursiveAction{

	/**
	 *	Generated serial ID 
	 */
	private static final long serialVersionUID = 209963736630273702L;
	
	private URI parentURI;
	private boolean isFirstCrawl;
	private CrawlinResult crawlinResult;
	private Scrapper scrapper;
	
	public CrawlinTask( Scrapper scrapper, URI parentURI, CrawlinResult crawlinResult) {
		this.scrapper = scrapper;
		this.parentURI = parentURI;
		this.isFirstCrawl = false;
		this.crawlinResult = crawlinResult;
	}
	
	public CrawlinTask( Scrapper scrapper ) {
		this.scrapper = scrapper;
		this.parentURI = scrapper.getBaseURI();
		this.isFirstCrawl = true;
		this.crawlinResult = new CrawlinResult();
	}

	
	@Override
	protected void compute() {
		if( crawlinResult.getResultMap().putIfAbsent(parentURI, new HashSet<URI>()) == null ) {
		
			ScrappinURIsResult scrappinURIsResult = scrappParentURIandGetChildURIs();
			System.out.println("got scrappin result for URI: " + parentURI);
			
			processScrappinResult(scrappinURIsResult);	
		}
	}
	
	
	private ScrappinURIsResult scrappParentURIandGetChildURIs() {
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
		if(scrappinURIsResult == null)									{ crawlinResult.getRedirectMap().put(parentURI, parentURI); return; }
		if(!scrappinURIsResult.destinationURI.equals(parentURI)) 		crawlinResult.getRedirectMap().put(parentURI, scrappinURIsResult.destinationURI);
		if(isFirstCrawl)												scrapper.setBaseURI( scrappinURIsResult.destinationURI );
	}
	
	
	private void treatValidScrappinResult(ScrappinURIsResult scrappinURIsResult) {
		crawlinResult.getResultMap().put(scrappinURIsResult.destinationURI, scrappinURIsResult.getResult()); 	
		filterChildURISetFromAlreadyCrawledPages(scrappinURIsResult);
		crawlThroughChildURIs(scrappinURIsResult.getResult());
	}
	

	private void filterChildURISetFromAlreadyCrawledPages(ScrappinURIsResult scrappinURIsResult) {
		scrappinURIsResult.getResult()
		.removeIf( uri -> crawlinResult.getResultMap().containsKey(getDestinationIfRedirected(uri)) || crawlinResult.getResultMap().containsKey(uri) || crawlinResult.getRedirectMap().containsValue(uri));
		
	}

	
	private URI getDestinationIfRedirected( URI uri) {
		return crawlinResult.getRedirectMap().containsKey(uri) ? crawlinResult.getRedirectMap().get(uri) : uri;
	}

	
	private void crawlThroughChildURIs(Set<URI> childURISet) {
		List<CrawlinTask> tasksToFork = new ArrayList<>();
		for(URI uri : childURISet) {
			CrawlinTask task = new CrawlinTask(scrapper.clone(), uri, crawlinResult);
			tasksToFork.add(task);
			task.fork();
		}
		tasksToFork.forEach(task -> task.join());
	}

	
	public CrawlinTask decorateResults() {
		this.crawlinResult.getResultMap().keySet().removeAll(this.crawlinResult.getRedirectMap().keySet());
		return this;
		
	}

	
	public CrawlinResult getCrawlinResult() {
		return this.crawlinResult;
	}
}