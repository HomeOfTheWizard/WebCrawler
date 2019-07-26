package oz.webCrawler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class CrawlerAppRunner 
{	
	
    public static void main( String[] args )
    {
        try {
        	String url = args[0];
            new CrawlerAppRunner().run(url);
        }catch(IllegalArgumentException ex) {
        	System.out.println("please enter a correct URL to start crawling");
        	ex.printStackTrace();
        }
    }
    
    public void run(String baseURI) throws IllegalArgumentException{
    	java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
    	
    	Map<URI, Set<URI>> result;
    	
    	WebClientFactory factoryInstance = WebClientFactory.getInstance();
    	Scrapper scrapper = new Scrapper(baseURI, factoryInstance);
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
    	result = crawler.getSiteMap();
    	
		System.out.println("\n\n##############################################\nCrawling result found following pages:");
		result.keySet().stream().sorted().forEach(key -> System.out.println(key));
		System.out.println("\ntotal of:"+ result.keySet().size()+" pages found");
    }
    
    
}
