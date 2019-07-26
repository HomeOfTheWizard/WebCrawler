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
		CrawlinResult result = CrawlFromBaseURIAndGetSiteMap();
		
		return result.getResultMap();
	}

	private CrawlinResult CrawlFromBaseURIAndGetSiteMap() {
		
		ForkJoinPool fjPool = ForkJoinPool.commonPool();
		fjPool.invoke(crawler);
		
		CrawlinResult result = crawler.getResults();
		
		return result;
	}

	
}