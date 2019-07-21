package oz.webCrawler;

import java.net.URI;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ScrappinUtilsTest{

	@Test
	public void crawlerCanIdentifyThatGoogleIsExternalDomain() {
		//Given
		URI baseUrl = URI.create("https://babylonhealth.com");
		URI url = URI.create("https://www.google.com");
		
		//when
		boolean isInternal = ScrappinUtils.isInternal(baseUrl, url);
		
		//then
		Assertions.assertThat(isInternal).isFalse();
	}
	
	@Test
	public void crawlerCanIdentifyThatBabylonHealthIsInternalDomain() {
		//Given
		URI baseUrl = URI.create("https://babylonhealth.com");
		URI url = URI.create("https://onling.babylonhealth.com");
		
		//when
		boolean isInternal = ScrappinUtils.isInternal(baseUrl, url);
		
		//then
		Assertions.assertThat(isInternal).isTrue();
	}
	
	@Test
	public void crawlerCanIdentifyThatRelativeURIIsInternalDomain() {
		//Given
		URI baseUrl = URI.create("https://babylonhealth.com");
		URI url = URI.create("/about");
		
		//when
		boolean isInternal = ScrappinUtils.isInternal(baseUrl, url);
		
		//then
		Assertions.assertThat(isInternal).isTrue();
	}
}