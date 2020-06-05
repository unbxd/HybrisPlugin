package com.unbxd.client.search.response;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class RangeFacetEntry extends FacetEntry{

    private double from;
    private double to;

    public RangeFacetEntry(double from, double to, int count) {
        super(count);

        this.from = from;
        this.to = to;
    }

    public double getFrom(){
        return this.from;
    }

    public double getTo(){
        return this.to;
    }

}
