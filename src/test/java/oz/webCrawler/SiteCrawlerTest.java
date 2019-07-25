package oz.webCrawler;


import java.io.IOException;
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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;


public class SiteCrawlerTest{
	
	String baseUrlStr;
	URI urlBaseDest;
	URI urlAbout;
	URI urlPricing;
	URI urlBase;
	
	@Before
	public void before(){
		baseUrlStr = "https://babylonhealth.com";
		urlBaseDest = URI.create("https://www.babylonhealth.com/");
		urlAbout = URI.create("https://www.babylonhealth.com/about");
		urlPricing = URI.create("https://www.babylonhealth.com/pricing");
	}

	@Test
	public void crawlerCanGetUrlListFromBaseUrl() throws URISyntaxException, FailingHttpStatusCodeException, IOException{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		Scrapper scrapper = new Scrapper(baseUrlStr, WebClientFactory.getInstance());
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		//when
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		//then
		Assertions.assertThat(urlMap.get(urlBaseDest)).isNotEmpty().contains(urlPricing);
	}
	
	@Test
	public void crawlerCanGetUrlListFromChildUrl() throws MalformedURLException, URISyntaxException{
		//Given
		Scrapper scrapper = new Scrapper(baseUrlStr, WebClientFactory.getInstance());
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		//when
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		//then
		Assertions.assertThat(urlMap.keySet()).isNotNull().isNotEmpty().contains(urlAbout);
	}
	
	
	
}