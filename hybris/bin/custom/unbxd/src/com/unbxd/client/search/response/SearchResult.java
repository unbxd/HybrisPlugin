package com.unbxd.client.search.response;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult {

    private String _uniqueId;
    private Map<String, Object> _attributes;

    protected SearchResult(Map<String, Object> product){
        this._attributes = product;
        this._uniqueId = (String) _attributes.get("uniqueId");
    }

    /**
     * @return Attributes of the product
     */
    public Map<String, Object> getAttributes(){
        return this._attributes;
    }

    /**
     * @return Unique Id of the product
     */
    public String getUniqueId(){
        return this._uniqueId;
    }

    /**
     * @param fieldName
     * @return Attribute of the product for given field name
     */
    public Object getAttribute(String fieldName){
        return this._attributes.get(fieldName);
    }
}
