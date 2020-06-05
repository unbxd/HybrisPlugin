package com.unbxd.client.search;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchClientFactory {

    public static SearchClient getSearchClient(String siteKey, String apiKey, boolean secure, CloseableHttpClient httpClient){
        return new SearchClient(siteKey, apiKey, secure, httpClient);
    }
}
