package oz.webCrawler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class SiteCrawler{
	
	private CrawlinTask crawler;

	public SiteCrawler(CrawlinTask crawlingAction) {
		this.crawler = crawlingAction;
	}
	
	public Map<URI, Set<URI>> getSiteMap() {
		Map<URI, Set<URI>> result = CrawlFromBaseURIAndGetSiteMap();
				
		System.out.println("\n\n##############################################\nCrawling result found following pages:");
		result.keySet().stream().sorted().forEach(key -> System.out.println(key));
		System.out.println("\ntotal of:"+ result.keySet().size()+" pages found");
		
		return result;
	}

	private Map<URI, Set<URI>> CrawlFromBaseURIAndGetSiteMap() {
		
		ForkJoinPool fjPool = ForkJoinPool.commonPool();
		fjPool.invoke(crawler);
		
		CrawlinResult result = crawler.cleanUpResults().crawlinResult;
		
		return result.getResultMap();
	}

	
}