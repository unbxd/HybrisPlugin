package com.unbxd.client.recommendations.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 09/07/14
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationsException extends Exception{

    public RecommendationsException(String message) {
        super(message);
    }

    public RecommendationsException(Throwable cause) {
        super(cause);
    }
}
