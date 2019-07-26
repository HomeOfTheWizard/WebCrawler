# WebCrawler
A java web crawler for listing all internal domain adress of a web site  
  
A simple pure java batch, taking a main url as input,   
crawls through its internal domains using htmlUnit that simulates a webClient,   
and output the list of internal web pages found. Project is also creating a map of parent->child web pages.  
Can evolve easily to a web api giving the site map through web service.  

### For building the project and create executable jar:  
-download sources  
-run: mvn clean package  

### To run it:  
execute the jar by giving it a base url  
ex:  java -jar target/webCrawler-0.0.1-SNAPSHOT-jar-with-dependencies.jar https://babylonhealth.com  

### To change config of the scrapper (webClient):  
run the jar with a config file in the same root folder containing the webClient configs.  
Example file is in the src/main/ressource  folder.  
### If Not following default params will be used:  
    isCssEnabled : false   
    isJSEnabled : false  
    jsBGTimeout : 0ms  
    isRedirectEnabled : true  
