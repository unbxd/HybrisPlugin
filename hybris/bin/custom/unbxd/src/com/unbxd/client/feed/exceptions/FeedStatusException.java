package com.unbxd.client.feed.exceptions;

public class FeedStatusException extends Exception {

    public FeedStatusException(Throwable t){
        super(t);
    }

    public FeedStatusException(String message){
        super(message);
    }
}
