package com.unbxd.client.autosuggest.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoSuggestException extends Exception {

    public AutoSuggestException(String message) {
        super(message);
    }

    public AutoSuggestException(Throwable cause) {
        super(cause);
    }
}
