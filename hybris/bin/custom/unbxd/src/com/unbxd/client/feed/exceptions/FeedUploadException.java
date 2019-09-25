package com.unbxd.client.feed.exceptions;

public class FeedUploadException extends Exception {

    public FeedUploadException(Throwable t){
        super(t);
    }

    public FeedUploadException(String message){
        super(message);
    }
}
