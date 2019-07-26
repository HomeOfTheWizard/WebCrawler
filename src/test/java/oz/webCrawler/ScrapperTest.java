package oz.webCrawler;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScrapperTest{
	
	String baseUrlStr;
	String urlAboutStr;
	String urlRelativeAboutStr;
	String urlPricingStr;
	URI urlPricing;
	URI urlAbout;
	URI urlBase;
	WebClient mockWebClient;
	HtmlPage mockBaseUrlPage;
	HtmlPage mockAboutUrlPage;
	
	
	@Before
	public void before() throws URISyntaxException, FailingHttpStatusCodeException, IOException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		
		baseUrlStr = "https://babylonhealth.com";
		urlAboutStr = "https://www.babylonhealth.com/about";
		urlPricingStr = "https://www.babylonhealth.com/pricing";
		urlRelativeAboutStr = "/about";
		
		urlPricing = new URL(urlPricingStr).toURI();
		urlAbout = new URL(urlAboutStr).toURI();
		urlBase = new URL(baseUrlStr).toURI();
		
		mockWebClient = new WebClient(BrowserVersion.getDefault());
		mockWebClient.getOptions().setJavaScriptEnabled(false);
		mockWebClient.getOptions().setCssEnabled(false);
		mockWebClient.getOptions().setRedirectEnabled(true);
		
		String path = new File(".").getCanonicalPath();
		mockBaseUrlPage = mockWebClient.getPage("file:\\\\" + path + "\\src\\test\\resources\\Home_Babylon_Health.html");
		mockAboutUrlPage = mockWebClient.getPage("file:\\\\" + path + "\\src\\test\\resources\\About_Babylon_Health.html");
	}
	
	

	@Test
	public void scrapper_GivenABaseURL_CanGetASingleChildUrl() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//Given
		WebClient mockWebClient = Mockito.mock(WebClient.class);
		when(mockWebClient.getPage(urlBase.toURL())).thenReturn(mockBaseUrlPage);
		
		WebClientFactory mockWebClientFactory = Mockito.mock(WebClientFactory.class);
		when(mockWebClientFactory.getWebClient()).thenReturn(mockWebClient);
		
		Scrapper scrapper = new Scrapper(baseUrlStr, mockWebClientFactory);
		
		//when
		ScrappinURIsResult urlList = scrapper.getURIListfromParentURI(urlBase);
		
		//then
		Assertions.assertThat(urlList.getResult()).contains(urlAbout);		
	}
	
	@Test
	public void scrapper_GivenABaseURL_CanGetASingleChildUrl_WhenChildIsRelative() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//Given
		WebClient mockWebClient = Mockito.mock(WebClient.class);
		when(mockWebClient.getPage(urlAbout.toURL())).thenReturn(mockAboutUrlPage);
		
		WebClientFactory mockWebClientFactory = Mockito.mock(WebClientFactory.class);
		when(mockWebClientFactory.getWebClient()).thenReturn(mockWebClient);
		Scrapper scrapper = new Scrapper(baseUrlStr, mockWebClientFactory);
		
		//when
		ScrappinURIsResult urlList = scrapper.getURIListfromParentURI(urlAbout);
		
		//then
		Assertions.assertThat(urlList.getResult()).contains(urlPricing);		
	}
}