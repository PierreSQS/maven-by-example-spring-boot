package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;

import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Condition;
import org.sonatype.mavenbook.model.Atmosphere;
import org.sonatype.mavenbook.model.Wind;
import org.sonatype.mavenbook.model.Weather;

@Slf4j
public class YahooParser {


    public Weather parse(InputStream inputStream) throws Exception {
	Weather weather = new Weather();
	
	log.info( "Creating XML Reader" );
	SAXReader xmlReader = createXmlReader();
	Document doc = xmlReader.read( inputStream );
	
	log.info( "Parsing XML Response" );
	Location location = new Location();
	location.setCity( doc.valueOf("/rss/channel/yweather:location/@city") );
	location.setRegion( doc.valueOf("/rss/channel/yweather:location/@region") );
	location.setCountry( doc.valueOf("/rss/channel/yweather:location/@country") );
	log.info("*************** the location: {} ************", location.getCity());
	weather.setLocation( location );

	Condition condition = new Condition();
	condition.setText( doc.valueOf("/rss/channel/item/yweather:condition/@text") );
	condition.setTemp( doc.valueOf("/rss/channel/item/yweather:condition/@temp") );
	condition.setCode( doc.valueOf("/rss/channel/item/yweather:condition/@code") );
	condition.setDate( doc.valueOf("/rss/channel/item/yweather:condition/@date") );
	condition.setWeather( weather );
	weather.setCondition( condition );

	Atmosphere atmosphere = new Atmosphere();
	atmosphere.setHumidity( doc.valueOf("/rss/channel/yweather:atmosphere/@humidity") );
	atmosphere.setVisibility( doc.valueOf("/rss/channel/yweather:atmosphere/@visibility") );
	atmosphere.setPressure( doc.valueOf("/rss/channel/yweather:atmosphere/@pressure") );
	atmosphere.setRising( doc.valueOf("/rss/channel/yweather:atmosphere/@rising") );
	atmosphere.setWeather( weather );
	weather.setAtmosphere( atmosphere );

	Wind wind = new Wind();
	wind.setChill( doc.valueOf("/rss/channel/yweather:wind/@chill") );
	wind.setDirection( doc.valueOf("/rss/channel/yweather:wind/@direction") );
	wind.setSpeed( doc.valueOf("/rss/channel/yweather:wind/@speed") );
	wind.setWeather( weather );
	weather.setWind( wind );

	weather.setDate( new Date() );
	
	return weather;
    }
    
    private SAXReader createXmlReader() {
	Map<String,String> uris = new HashMap<>();
        uris.put( "yweather", "http://xml.weather.yahoo.com/ns/rss/1.0" );
        
        DocumentFactory factory = new DocumentFactory();
        factory.setXPathNamespaceURIs( uris );
        
	SAXReader xmlReader = new SAXReader();
	xmlReader.setDocumentFactory( factory );
	return xmlReader;
    }
}
