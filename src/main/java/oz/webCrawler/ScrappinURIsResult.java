package oz.webCrawler;

import java.net.URI;
import java.util.Set;

public class ScrappinURIsResult extends ScrappinResult<Set<URI>>{
	
	private final Set<URI> result;

	public ScrappinURIsResult(URI destinationURI, Set<URI> result) {
		super(destinationURI);
		this.result = result;
	}

	@Override
	public Set<URI> getResult() {
		return result;
	}
	
}