package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Slf4j
public class YahooRetriever {

	private static final String APPID = "C6HZsD30";
	private static final String CONSUMERKEY = "dj0yJmk9MThGTERmc2VNSUxCJmQ9WVdrOVF6WklXbk5FTXpBbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTky";
	private static final String CONSUMERSECRET = "725732a7344b12c56f4742514342c17ee7d851ca";
	private static final String YAHOOWEATHERURL = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
	
	private String signature;
	private String oauthNonce;
	private long timestamp;

	public InputStream retrieve(String location) throws Exception {
		String[] cityAndCountry = location.split("/");
		log.info("Retrieving Weather Data in "+cityAndCountry[0]);
		configureRequestContext();
		return retrieveData(location);

	}

	public void configureRequestContext() throws Exception {
		timestamp = new Date().getTime() / 1000;
		byte[] nonce = new byte[32];
		Random rand = new Random();
		rand.nextBytes(nonce);
		oauthNonce = new String(nonce).replaceAll("\\W", "");

		List<String> parameters = new ArrayList<>();
		parameters.add("oauth_consumer_key=" + CONSUMERKEY);
		parameters.add("oauth_nonce=" + oauthNonce);
		parameters.add("oauth_signature_method=HMAC-SHA1");
		parameters.add("oauth_timestamp=" + timestamp);
		parameters.add("oauth_version=1.0");
		// Make sure value is encoded
		parameters.add("location=" + URLEncoder.encode("sunnyvale,ca", "UTF-8"));

		Collections.sort(parameters);

		String params = parameters.stream().collect(Collectors.joining("&"));

		StringBuilder parametersList = new StringBuilder(params);

		String signatureString = "GET&" + URLEncoder.encode(YAHOOWEATHERURL, "UTF-8") + "&"
				+ URLEncoder.encode(parametersList.toString(), "UTF-8");

		signature = null;
		try {
			SecretKeySpec signingKey = new SecretKeySpec((CONSUMERSECRET + "&").getBytes(), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
			Encoder encoder = Base64.getEncoder();
			signature = encoder.encodeToString(rawHMAC);
		} catch (Exception e) {
			log.error("###### Unable to append signature ######");
			System.exit(0);
		}
	}
	
	public InputStream retrieveData(String location) throws Exception {
		String[] cityAndContry = location.split(",");
		
		String authorizationLine = "OAuth " +
            "oauth_consumer_key=\"" + CONSUMERKEY + "\", " +
            "oauth_nonce=\"" + oauthNonce + "\", " +
            "oauth_timestamp=\"" + timestamp + "\", " +
            "oauth_signature_method=\"HMAC-SHA1\", " +
            "oauth_signature=\"" + signature + "\", " +
            "oauth_version=\"1.0\"";
        
        log.info("Autorisation: {}",authorizationLine);
        log.info( "Trying HttpClient 4.3" );

        CloseableHttpClient client = HttpClients.custom().build();

        // (1) Use the new Builder API (from v4.3)
        HttpUriRequest request = RequestBuilder.get()
                // weird for me!!! what is going on here????
        		//.setUri("url + \"?location=sunnyvale,ca")
                .setUri(YAHOOWEATHERURL + "?location="+cityAndContry[0]+","+cityAndContry[1])
                // (2) Use the included enum
                .setHeader(HttpHeaders.AUTHORIZATION, authorizationLine)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/xml")
                // (3) Or your own
                .setHeader("Yahoo-App-Id", APPID)
                .build();        
        

        CloseableHttpResponse response = client.execute(request);
        log.info( "request sent!" );
        
        int statusCode = response.getStatusLine().getStatusCode();
        
        log.info( "status code: "+statusCode );

        return response.getEntity().getContent();
        
	}
	
	public void go(String location) throws Exception {
		configureRequestContext();
		retrieveData(location);
	}
	
	public static void main(String...args) {
		try {
			new YahooRetriever().go(args[0]);
		} catch (Exception e) {
			log.info("Connection unsuccessful!!",e);
		}
	}
}