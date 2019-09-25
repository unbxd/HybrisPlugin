package com.unbxd.client.feed;

public class FeedClientFactory {

    public static FeedClient getFeedClient(String siteKey, String secretKey, boolean secure){
        return new FeedClient(siteKey, secretKey, secure);
    }
}
