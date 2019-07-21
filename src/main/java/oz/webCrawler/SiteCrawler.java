package oz.webCrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class SiteCrawler{
	
	public Map<URI, Set<URI>> getSiteMap(String baseUrlStr) {
		URI baseURI;
		try {
			baseURI = ScrappinUtils.getURLIfCorrectlyFormatted(baseUrlStr);
		
		}catch(MalformedURLException | URISyntaxException ex) {
			System.out.println("URL entered: " + baseUrlStr);
			throw new IllegalArgumentException("base URL entered is sintactically not correct!");
		}
		
		return CrawlFromBaseURIAndGetSiteMap(baseURI);
	}

	private Map<URI, Set<URI>> CrawlFromBaseURIAndGetSiteMap(URI baseURI) {
		ConcurrentHashMap<URI, Set<URI>> resultMap = new ConcurrentHashMap<>();
		ConcurrentHashMap<URI, URI> redirectMap = new ConcurrentHashMap<>();
		
		ForkJoinPool fjPool = ForkJoinPool.commonPool();
		CrawlinTask crawler = new CrawlinTask(baseURI, resultMap, redirectMap);
		fjPool.invoke(crawler);
		
		resultMap.keySet().removeAll(redirectMap.keySet());
		
		System.out.println("\n\n##############################################\nCrawling result found following pages:");
		resultMap.keySet().stream().sorted().forEach(key -> System.out.println(key));
		System.out.println("\ntotal of:"+ resultMap.keySet().size()+" pages found");
		
		return resultMap;
	}
}