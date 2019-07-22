package oz.webCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scrapper{
	
	WebClient webClient;
	private URI baseURI;

	public Scrapper(String baseURI) {
		this.baseURI = checkIfBaseURIisValid(baseURI);
		
		webClient = new WebClient(BrowserVersion.FIREFOX_60);
		webClient.getOptions().setCssEnabled(false);  
		webClient.getOptions().setJavaScriptEnabled(false);
//		webClient.waitForBackgroundJavaScript(3000);
		webClient.getOptions().setRedirectEnabled(true);
	}
	
	public URI getBaseURI() {
		return baseURI;
	}
	
	public void setBaseURI(URI destinationURI) {
		this.baseURI = destinationURI;
	}
	
	private URI checkIfBaseURIisValid(String baseUrl) {
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
		parentURI = ScrappinUtils.getDestinationURIIfRedirected(parentURI, page);
		Set<URI> resultURISet = getURISetFromPage(page);
		return new ScrappinURIsResult(parentURI, resultURISet);
	}

	
	private Set<URI> getURISetFromPage(HtmlPage page){	
		List<HtmlAnchor> listAnchores = page.getByXPath("//a");
		Set<URI> uriSet = getURISetFromListOfHtmlAnchores(listAnchores);
		return uriSet;
	}
	
	//TODO refactor	
	private Set<URI> getURISetFromListOfHtmlAnchores(List<HtmlAnchor> listAnchores) {
		Set<URI> resultSet = new HashSet<>();
		listAnchores.stream().forEach(anchor -> getInternalURISetFromAnchorList(anchor, resultSet));
		return resultSet;
	}

	//TODO refactor
	private void getInternalURISetFromAnchorList(HtmlAnchor anchor, Set<URI> resultSet) {
		try {
			URI uri = ScrappinUtils.transformIfRelativeToAbsoluteURL(baseURI,anchor.getHrefAttribute());
			if(ScrappinUtils.isInternal(baseURI, uri)) resultSet.add(uri);
		} catch (MalformedURLException | URISyntaxException e) {
			System.out.println("anchor link found: "+anchor.getHrefAttribute()+" on page is syncactically not a valid URL");
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
        	System.out.println("URL: "+ url.toString() +"\nIs returning failing htmp code:" + fhhtpex.getStatusCode() + "\n with message:" + fhhtpex.getMessage());
		}
		return null;
	}
	
	
	@Override
	public Scrapper clone() {
		return new Scrapper(baseURI.toString());
	}

}