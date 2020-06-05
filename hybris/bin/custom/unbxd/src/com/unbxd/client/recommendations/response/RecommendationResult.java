package com.unbxd.client.recommendations.response;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationResult {

    private String _uniqueId;
    private Map<String, Object> _attributes;

    protected RecommendationResult(Map<String,Object> params) {
        this._attributes = params;
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
