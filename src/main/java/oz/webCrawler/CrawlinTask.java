package oz.webCrawler;

import java.net.URI;
import java.util.ArrayList;
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
	public CrawlinResult crawlinResult;
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
		
		ScrappinURIsResult scrappinURIsResult = scrappParentURIandGetChildURIs();
		System.out.println("got scrappin result for URI: " + parentURI);
		
		processScrappinResult(scrappinURIsResult);	
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
		if(scrappinURIsResult == null)									{ crawlinResult.putRedirect(parentURI, parentURI); return; }
		if(!scrappinURIsResult.destinationURI.equals(parentURI)) 		crawlinResult.putRedirect(parentURI, scrappinURIsResult.destinationURI);
		if(isFirstCrawl)												scrapper.setBaseURI( scrappinURIsResult.destinationURI );
	}
	
	
	private void treatValidScrappinResult(ScrappinURIsResult scrappinURIsResult) {
		crawlinResult.putResult(scrappinURIsResult.destinationURI, scrappinURIsResult.getResult()); 	
		filterChildURISetFromAlreadyCrawledPages(scrappinURIsResult);
		crawlThroughChildURIs(scrappinURIsResult.getResult());
	}
	

	private void filterChildURISetFromAlreadyCrawledPages(ScrappinURIsResult scrappinURIsResult) {
		scrappinURIsResult.getResult()
		.removeIf( uri -> crawlinResult.isRedirectedAlreadyInResults(uri) || crawlinResult.containsResultForKey(uri) );
		
	}

	
	private void crawlThroughChildURIs(Set<URI> childURISet) {
		List<CrawlinTask> tasksToFork = new ArrayList<>();
		for(URI uri : childURISet) {
			
			if(crawlinResult.tagAsProcessingIfNotAlready(uri)) {
				CrawlinTask task = new CrawlinTask(scrapper.clone(), uri, crawlinResult);
				tasksToFork.add(task);
				task.fork();
			}
		}
		tasksToFork.forEach(task -> task.join());
	}

	
	public CrawlinTask cleanUpResults() {
		this.crawlinResult.getResultMap().keySet().removeAll(this.crawlinResult.getRedirectMap().keySet());
		return this;
		
	}

}