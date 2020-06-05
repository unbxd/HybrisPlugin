package com.unbxd.client.search.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class Facets {

    private List<Facet> _facets;
    private Map<String, Facet> _facetsMap;

    protected Facets(Map<String, Object> params){
        this._facets = new ArrayList<Facet>();
        this._facetsMap = new HashMap<String, Facet>();

        for(String field : params.keySet()){
            Map<String, Object> facetParams = (Map<String, Object>) params.get(field);
            String type = (String) facetParams.get("type");

            Facet facet = type.equals("facet_fields") ? new Facet(field, facetParams) : new RangeFacet(field, facetParams);
            this._facets.add(facet);
            this._facetsMap.put(field, facet);
        }
    }

    /**
     * @return List of {@link Facet}
     */
    public List<Facet> getFacets(){
        return this._facets;
    }

    /**
     * @return Map of field --> {@link Facet}
     */
    public Map<String, Facet> getFacetsAsMap(){
        return this._facetsMap;
    }

    /**
     * @param facetName
     * @return Facet for given field name
     */
    public Facet getFacet(String facetName){
        return this._facetsMap.get(facetName);
    }
}
