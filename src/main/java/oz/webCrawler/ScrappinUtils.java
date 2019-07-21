package oz.webCrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScrappinUtils{
	
	public static URI transformIfRelativeToAbsoluteURL(URI baseUrl, String url) throws MalformedURLException, URISyntaxException {
		URI uri = URI.create(url);
		
		if(!uri.isAbsolute()) {
				if(!url.isEmpty() && url != null && url.charAt(0) != '/')
					url = '/' + url;
				URI resultUrl = new URL(baseUrl.toURL().getProtocol(),baseUrl.getHost(), url).toURI();
				return resultUrl;
		}
		return getURLIfCorrectlyFormatted(url);
	}
	
	public static URI getURLIfCorrectlyFormatted(String url) throws MalformedURLException, URISyntaxException {
		return new URL(url).toURI();
	}

	public static boolean isInternal(URI baseURI, URI uri) {
		if(!uri.isAbsolute())	return true; 
		if(uri.getHost()==null) return false;
		if(uri.getHost().contains(baseURI.getHost()))	return true;
		return false;
	}
	
	public static URI getDestinationURIIfRedirected(URI parentURI, HtmlPage page) {
		try {
			if(!page.getUrl().toURI().equals(parentURI))
				parentURI = page.getUrl().toURI(); 
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return parentURI;
	}
}