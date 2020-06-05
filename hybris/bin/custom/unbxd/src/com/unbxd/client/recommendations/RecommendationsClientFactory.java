package com.unbxd.client.recommendations;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationsClientFactory {

    public static RecommendationsClient getRecommendationsClient(String siteKey, String apiKey, boolean secure, CloseableHttpClient httpClient){
        return new RecommendationsClient(siteKey, apiKey, secure, httpClient);
    }
}
