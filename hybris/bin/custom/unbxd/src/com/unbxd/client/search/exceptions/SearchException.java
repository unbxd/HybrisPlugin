package com.unbxd.client.search.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchException extends Exception{

    public SearchException(String message) {
        super(message);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }
}
