package oz.webCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class Scrapper{
	
	private WebClient webClient;
	private URI baseURI;
	private WebClientFactory factory;

	public Scrapper(String baseURI, WebClientFactory factory) {
		this.baseURI = checkIfBaseURIisValid(baseURI);
		this.factory = factory;
		this.webClient = factory.getWebClient();
	}
	
	public URI getBaseURI() {
		return baseURI;
	}
	
	public void setBaseURI(URI destinationURI) {
		this.baseURI = destinationURI;
	}
	
	
	static private URI checkIfBaseURIisValid(String baseUrl) {
		URI baseURI = null;
		try {
			baseURI = ScrappinUtils.getURLIfCorrectlyFormatted(baseUrl);
		} catch (MalformedURLException | URISyntaxException e) {
			throw new IllegalArgumentException();
		}
		return baseURI;
	}
	
	
	//@Nullable
	public ScrappinURIsResult getURIListfromParentURI(URI parentURI){
		System.out.println("scrappin URI: " + parentURI);
		
		HtmlPage page = getPage(parentURI);
		if(page!=null) 	
			return getURIListFromPage(page, parentURI);
		else 			
			return null;
	}

	
	private ScrappinURIsResult getURIListFromPage(HtmlPage page, URI parentURI) {
		URI destinationURI = ScrappinUtils.getDestinationURIIfRedirected(parentURI, page);
		Set<URI> resultURISet = getURISetFromPage(page);
		
		return new ScrappinURIsResult(destinationURI, resultURISet);
	}

	
	private Set<URI> getURISetFromPage(HtmlPage page){	
		List<HtmlAnchor> listAnchores = page.getByXPath("//a");
		Set<URI> uriSet = getURISetFromListOfHtmlAnchores(listAnchores);
		return uriSet;
	}
	
	
	private Set<URI> getURISetFromListOfHtmlAnchores(List<HtmlAnchor> listAnchores) {
		return listAnchores.stream()
			.map(anchor -> transformAnchorToURL(anchor))
			.filter(uri -> uri != null)
			.filter(uri -> ScrappinUtils.isInternal(baseURI, uri))
			.collect(Collectors.toSet());
	}

	private URI transformAnchorToURL(HtmlAnchor anchor) {
		try {
			return ScrappinUtils.transformIfRelativeToAbsoluteURL(baseURI,anchor.getHrefAttribute());
		} catch (MalformedURLException | URISyntaxException e) {
			System.out.println("anchor link found: "+anchor.getHrefAttribute()+" on page is syncactically not a valid URL");
			return null;
		}
	}

	//@Nullable
	private HtmlPage getPage(URI url) {
		try {
			HtmlPage page = webClient.getPage(url.toURL());
			return page;
		} catch (IOException ex ) {
			System.out.println("URL: "+ url.toString() +"\nIO exception Occured with message: " + ex.getMessage()); 
        } catch (ClassCastException ccex) {
        	System.out.println("URL: "+ url.toString() +"\nIs not pointing to an html page. " + ccex.getMessage());
        } catch (FailingHttpStatusCodeException fhhtpex) {
        	System.out.println("URL: "+ url.toString() +"\nIs returning failing html code:" + fhhtpex.getStatusCode() + "\n with message:" + fhhtpex.getMessage());
		}
		return null;
	}
	
	
	@Override
	public Scrapper clone() {
		return new Scrapper(baseURI.toString(), factory);
	}

}