package com.unbxd.client.search.response;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Stat {

    private double _min;
    private double _max;
    private int _count;
    private double _sum;
    private double _mean;
    private Map<String, Object> facets;

    protected Stat(Map<String, Object> params){
        this._min = ((Number)params.get("min")).doubleValue();
        this._max = ((Number)params.get("max")).doubleValue();
        this._count = ((Number)params.get("count")).intValue();
        this._sum = ((Number)params.get("sum")).doubleValue();
        this._mean = ((Number)params.get("mean")).doubleValue();
        this.facets = (Map<String, Object>) params.get("facets");
    }

    public int getCount(){
        return this._count;
    }

    public double getMin(){
        return this._min;
    }

    public double getMax(){
        return this._max;
    }

    public double getSum(){
        return this._sum;
    }

    public double getMean(){
        return this._mean;
    }

    public Map<String, Object> getFacets() {
        return facets;
    }
}
