package oz.webCrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class ScrapperTest{
	
	String baseUrlStr;
	String urlAboutStr;
	String urlRelativeAboutStr;
	String urlPricingStr;
	URI urlPricing;
	URI urlAbout;
	URI urlBase;
	
	
	@Before
	public void before() throws MalformedURLException, URISyntaxException {
		baseUrlStr = "https://babylonhealth.com";
		urlAboutStr = "https://www.babylonhealth.com/about";
		urlPricingStr = "https://www.babylonhealth.com/pricing";
		urlRelativeAboutStr = "/about";
		urlPricing = new URL(urlPricingStr).toURI();
		urlAbout = new URL(urlAboutStr).toURI();
		urlBase = new URL(baseUrlStr).toURI();
		
	}

	@Test
	public void scrapper_GivenABaseURL_CanGetASingleChildUrl() {
		//Given
		Scrapper scrapper = new Scrapper(baseUrlStr);
		
		//when
		ScrappinURIsResult urlList = scrapper.getURIListfromParentURI(urlBase);
		
		//then
		Assertions.assertThat(urlList.getResult()).contains(urlAbout);		
	}
	
	@Test
	public void scrapper_GivenABaseURL_CanGetASingleChildUrl_WhenChildIsRelative() {
		//Given
		Scrapper scrapper = new Scrapper(baseUrlStr);
		
		//when
		ScrappinURIsResult urlList = scrapper.getURIListfromParentURI(urlAbout);
		
		//then
		Assertions.assertThat(urlList.getResult()).contains(urlPricing);		
	}
}