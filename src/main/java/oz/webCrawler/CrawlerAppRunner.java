package oz.webCrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
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
    	Scrapper scrapper = new Scrapper(baseURI);
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
    	crawler.getSiteMap();
    }
    
    
}
