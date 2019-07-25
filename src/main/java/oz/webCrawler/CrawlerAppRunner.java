package oz.webCrawler;

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
    	WebClientFactory factoryInstance = WebClientFactory.INSTANCE;
    	Scrapper scrapper = new Scrapper(baseURI, factoryInstance);
    	CrawlinTask crawlingTask = new CrawlinTask(scrapper);
    	SiteCrawler crawler = new SiteCrawler(crawlingTask);
    	crawler.getSiteMap();
    }
    
    
}
