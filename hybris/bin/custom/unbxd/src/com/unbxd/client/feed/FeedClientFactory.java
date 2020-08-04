package com.unbxd.client.feed;

public class FeedClientFactory {

    public static FeedClient getFeedClient(String siteKey, String secretKey, String apiKey,String domain){
        return new FeedClient(siteKey, secretKey, apiKey,domain);
    }
}
