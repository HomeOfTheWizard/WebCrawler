package oz.webCrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class SiteCrawlerTest{
	
	String baseUrlStr;
	URI urlAbout;
	URI urlPricing;
	URI urlBase;
	
	@Before
	public void before() throws MalformedURLException, URISyntaxException {
		baseUrlStr = "https://babylonhealth.com";
		urlBase = new URL(baseUrlStr).toURI();
		urlAbout = URI.create("https://www.babylonhealth.com/about");
		urlPricing = URI.create("https://www.babylonhealth.com/pricing");
	}

	@Test
	public void crawlerCanGetUrlListFromBaseUrl() throws MalformedURLException, URISyntaxException{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		//Given
		Scrapper scrapper = new Scrapper(baseUrlStr);
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		//when
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		//then
		Assertions.assertThat(urlMap.get(urlBase)).isNotEmpty().contains(urlPricing);
	}
	
	@Test
	public void crawlerCanGetUrlListFromChildUrl() throws MalformedURLException, URISyntaxException{
		//Given
		Scrapper scrapper = new Scrapper(baseUrlStr);
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		//when
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		//then
		Assertions.assertThat(urlMap.keySet()).isNotNull().isNotEmpty().contains(urlAbout);
	}
	
	
	
}