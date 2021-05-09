package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
	private static final String HTTPCLIENTVERSION = "4.5.13";
	
	private String signature;
	private String oauthNonce;
	private long timestamp;

	public InputStream retrieve(String location) throws Exception {
		log.info("Retrieving Weather Data for location: {}...", location);
		configureRequestContext(location);
		return retrieveData(location);

	}

	public void configureRequestContext(String location) throws Exception {
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
		parameters.add("location=" + URLEncoder.encode(location, StandardCharsets.UTF_8.toString()));

		Collections.sort(parameters);

		String params = parameters.stream().collect(Collectors.joining("&"));

		StringBuilder parametersList = new StringBuilder(params);

		String signatureString = "GET&" + URLEncoder.encode(YAHOOWEATHERURL, StandardCharsets.UTF_8.toString()) + "&"
				+ URLEncoder.encode(parametersList.toString(), StandardCharsets.UTF_8.toString());

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

		String authorizationLine = "OAuth " +
            "oauth_consumer_key=\"" + CONSUMERKEY + "\", " +
            "oauth_nonce=\"" + oauthNonce + "\", " +
            "oauth_timestamp=\"" + timestamp + "\", " +
            "oauth_signature_method=\"HMAC-SHA1\", " +
            "oauth_signature=\"" + signature + "\", " +
            "oauth_version=\"1.0\"";
        
        log.info("Authorisation: {}",authorizationLine);
        log.info( "Using Apache HttpClient {}...",HTTPCLIENTVERSION );

        CloseableHttpClient client = HttpClients.custom().build();

        HttpUriRequest request = RequestBuilder.get()
                .setUri(YAHOOWEATHERURL + "?location="+location)
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
		retrieve(location);
	}
	
	public static void main(String...args) {
		try {
			new YahooRetriever().go(args[0]);
		} catch (Exception e) {
			log.info("Connection unsuccessful!!",e);
		}
	}
}