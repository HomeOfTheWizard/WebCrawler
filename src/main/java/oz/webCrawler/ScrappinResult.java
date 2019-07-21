package oz.webCrawler;

import java.net.URI;

public abstract class ScrappinResult<T> {
	
	public final URI destinationURI;
	
	public ScrappinResult(URI destinationURI) {
		this.destinationURI = destinationURI;
	}
	
	public abstract T getResult();

}