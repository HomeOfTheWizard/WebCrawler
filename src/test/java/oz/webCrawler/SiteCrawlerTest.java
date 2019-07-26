package oz.webCrawler;


import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;


public class SiteCrawlerTest{
	
	String baseUrlStr;
	URI urlBaseDest;
	URI urlAbout;
	URI urlPricing;
	URI urlBase;
	
	@Before
	public void before(){
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		baseUrlStr = "https://babylonhealth.com";
		urlBase = URI.create(baseUrlStr);
		urlBaseDest = URI.create("https://www.babylonhealth.com/");
		urlAbout = URI.create("https://www.babylonhealth.com/about");
		urlPricing = URI.create("https://www.babylonhealth.com/pricing");
	}

	@Test
	public void crawlerCanGetUrlListFromBaseUrl() throws URISyntaxException, FailingHttpStatusCodeException, IOException{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		// Given Crawler with Given
		Set<URI> resultIncludePricingURI = new HashSet<URI>();
		resultIncludePricingURI.add(urlPricing);
		ScrappinURIsResult mockResultIncludePricingURI = new ScrappinURIsResult(urlBase, resultIncludePricingURI);
		ScrappinURIsResult mockResultForPricingIncludeNothingURI = new ScrappinURIsResult(urlPricing, new HashSet<URI>());
		
		// Scrapper 
		Scrapper mockScrapper = Mockito.mock(Scrapper.class);
		when(mockScrapper.getBaseURI()).thenReturn(urlBase);
		when(mockScrapper.clone()).thenReturn(mockScrapper);
		when(mockScrapper.getURIListfromParentURI(urlBase)).thenReturn(mockResultIncludePricingURI);
		when(mockScrapper.getURIListfromParentURI( urlPricing )).thenReturn(mockResultForPricingIncludeNothingURI);
		
		// CrawlinTask 
    	CrawlinTask crawlingTask = new CrawlinTask(mockScrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		// When does
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		// Then
		Assertions.assertThat(urlMap.get(urlBase)).isNotEmpty().contains(urlPricing);
	}
	
	@Test
	public void crawlerCanGetUrlListFromChildUrl() throws MalformedURLException, URISyntaxException{
		// Given Crawler with Given
		Set<URI> resultIncludePricingURI = new HashSet<URI>();
		resultIncludePricingURI.add(urlPricing);
		Set<URI> resultIncludeAboutURI = new HashSet<URI>();
		resultIncludeAboutURI.add(urlAbout);
		
		ScrappinURIsResult mockResultIncludePricingURI = new ScrappinURIsResult(urlBase, resultIncludePricingURI);
		ScrappinURIsResult mockResultForPricingIncludeAboutURI = new ScrappinURIsResult(urlPricing, resultIncludeAboutURI);
		ScrappinURIsResult mockResultForAboutIncludeNothing = new ScrappinURIsResult(urlAbout, new HashSet<URI>());
		
		// Scrapper 
		Scrapper mockScrapper = Mockito.mock(Scrapper.class);
		when(mockScrapper.getBaseURI()).thenReturn(urlBase);
		when(mockScrapper.clone()).thenReturn(mockScrapper);
		when(mockScrapper.getURIListfromParentURI(urlBase)).thenReturn(mockResultIncludePricingURI);
		when(mockScrapper.getURIListfromParentURI(urlPricing)).thenReturn(mockResultForPricingIncludeAboutURI);
		when(mockScrapper.getURIListfromParentURI(urlAbout)).thenReturn(mockResultForAboutIncludeNothing);
		
		// CrawlinTask 
    	CrawlinTask crawlingTask = new CrawlinTask(mockScrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
		
		// When does
		Map<URI, Set<URI>> urlMap = crawler.getSiteMap();
		
		// Then
		Assertions.assertThat(urlMap.get(urlPricing)).isNotEmpty().contains(urlAbout);
	}
	
	
	
}