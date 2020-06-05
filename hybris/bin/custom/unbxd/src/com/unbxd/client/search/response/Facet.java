package com.unbxd.client.search.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sourabh
 * Date: 08/07/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class Facet {

    protected String name;
    protected String _type;
    protected Integer position;
    protected String displayName;
    protected List<FacetEntry> _facetEntries;

    protected Facet(String facetName, Map<String, Object> params){
        this.name = facetName;
        this._type = (String) params.get("type");
        if(params.containsKey("position")){
            this.position = (Integer) params.get("position");
        }else {
            this.position = null;
        }
        if(params.containsKey("displayName")){
            this.displayName = (String) params.get("displayName");
        }else {
            this.displayName = null;
        }

        if(params.get("values") instanceof Map){
            Map<String, Object> map = (Map<String, Object>) params.get("values");
            this.generateEntries((List<Object>) map.get("counts"));
        }else{
            this.generateEntries((List<Object>) params.get("values"));
        }
    }

    /**
     * @return Facet name
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return Type of facet
     */
    public String getType(){
        return this._type;
    }

    protected void generateEntries(List<Object> values){
        this._facetEntries = new ArrayList<FacetEntry>();

        String term = null;

        for(int i = 0; i < values.size(); i++){
            if(i % 2 == 0){
                term = (String) values.get(i);
            }else{
                int count = (Integer)values.get(i);
                this._facetEntries.add(new FacetEntry(term, count));
            }
        }
    }

    /**
     * @return List of {@link FacetEntry}
     */
    public List<FacetEntry> getEntries(){
        return this._facetEntries;
    }

    public Integer getPosition(){ return this.position; }

    public String getDisplayName() { return  this.displayName; }

}
