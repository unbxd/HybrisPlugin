package com.unbxd.client.search.response;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class FacetEntry {

    private String term;
    private int count;

    protected FacetEntry(int count) {
        this.count = count;
    }

    public FacetEntry(String term, int count) {
        this.term = term;
        this.count = count;
    }

    public String getTerm(){
        return this.term;
    }

    public int getCount(){
        return this.count;
    }
}
