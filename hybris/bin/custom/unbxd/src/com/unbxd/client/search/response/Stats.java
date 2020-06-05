package com.unbxd.client.search.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Stats {

    private Map<String, Stat> _stats;

    protected Stats(Map<String, Object> params){
        this._stats = new HashMap<String, Stat>();
        for(String field : params.keySet()){
            if(params.get(field) != null)
                this._stats.put(field, new Stat((Map<String, Object>) params.get(field)));
        }
    }

    /**
     * @return Map of Field --> {@link Stat}
     */
    public Map<String, Stat> getStats(){
        return this._stats;
    }

    /**
     * @param fieldName
     * @return Stat for the field name
     */
    public Stat getStat(String fieldName){
        return this._stats.get(fieldName);
    }

}
