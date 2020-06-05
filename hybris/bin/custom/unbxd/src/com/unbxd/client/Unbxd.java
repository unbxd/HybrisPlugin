package com.unbxd.client;

import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.feed.FeedClientFactory;
import com.unbxd.client.search.SearchClient;
import com.unbxd.client.search.SearchClientFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.TimeUnit;

public class Unbxd {

    private static boolean _configured = false;

    private static String siteKey;
    private static String apiKey;
    private static String secretKey;
    private static boolean secure = false;
    private static CloseableHttpClient httpClient;

    private Unbxd(){}

    /**
     * Configure Unbxd Client. This method should be called while initializing you application.
     * If you don't know the configuration details please get in touch with support@unbxd.com
     *
     * @param siteKey The Unique Identifier for Site created on Unbxd Platform
     * @param apiKey API key for calling read APIs
     * @param secretKey API key for calling Feed APIs
     */
    public static void configure(String siteKey, String apiKey, String secretKey){
        Unbxd.siteKey = siteKey;
        Unbxd.apiKey = apiKey;
        Unbxd.secretKey = secretKey;
        Unbxd.httpClient = HttpClientManager.getHttpClient(ConnectionManager.getConnectionManager());

        _configured = true;
    }

    /**
     * Configure Unbxd Client. This method should be called while initializing you application.
     * If you don't know the configuration details please get in touch with support@unbxd.com
     *
     * @param siteKey The Unique Identifier for Site created on Unbxd Platform
     * @param apiKey API key for calling read APIs
     * @param secretKey API key for calling Feed APIs
     * @param secure True to use HTTPS while making REST API calls
     */
    public static void configure(String siteKey, String apiKey, String secretKey, boolean secure){
        Unbxd.configure(siteKey, apiKey, secretKey);
        Unbxd.secure = secure;
    }

    /**
     * Should return a new Feed Client
     * @return {@link FeedClient}
     * @throws ConfigException
     */
    public static FeedClient getFeedClient() throws ConfigException {
        if(!_configured)
            throw new ConfigException("Please configure first with Unbxd.configure()");
        return FeedClientFactory.getFeedClient(siteKey, secretKey, secure);
    }

    public static SearchClient getSearchClient() throws ConfigException {
        if(!_configured)
            throw new ConfigException("Please configure first with Unbxd.configure()");
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(1, TimeUnit.MINUTES).setConnectionManager(ConnectionManager.getConnectionManager()).build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient1 = builder.build();
        return SearchClientFactory.getSearchClient(siteKey, apiKey, secure, httpClient);
    }
}
