package com.unbxd.client.feed;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

import java.util.*;

public class FeedProduct implements InputDocument {
    // Unique Id of the product. Generally corresponds to the SKU.
    private String uniqueId;
    private final Map<String, Object> _attributes;
    private List<Map<String, Object>> _variants;

    public FeedProduct(){
        _attributes = new HashMap<String, Object>();
        _variants = new ArrayList<Map<String, Object>>();
    }
    /**
     * @param uniqueId
     * @param attributes
     */
    public FeedProduct(String uniqueId, Map<String, Object> attributes){
        if(attributes == null)
            attributes = new HashMap<String, Object>();
        attributes.put("uniqueId", uniqueId);

        _attributes = attributes;
        _variants = new ArrayList<Map<String, Object>>();

        this.uniqueId = uniqueId;
    }

    /**
     * @return Unique Id of the product
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @return Product Attributes
     */
    public Map<String, Object> getAttributes(){
        return _attributes;
    }

    /**
     * @return get list of associated products
     */
    public List<Map<String, Object>> getVariants(){
        return _variants;
    }

    public void addVariant(Map<String, Object> product){
        _variants.add(product);
    }

    @Override
    public void addField(String name, Object value){
        if(!name.equalsIgnoreCase("autosuggest")) {
            _attributes.put(name, value);
        }
    }

    @Override
    public void addField(IndexedProperty var1, Object var2) throws FieldValueProviderException {
        addField(var1.getName(), var2);
    };

    @Override
    public void addField(IndexedProperty var1, Object var2, String var3) throws FieldValueProviderException {
        addField(var1, var2);
    };

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Map<String, Object> get_attributes() {
        return _attributes;
    }

    public List<Map<String, Object>> get_variants() {
        return _variants;
    }

    public void set_variants(List<Map<String, Object>> _variants) {
        this._variants = _variants;
    }

    /**
     *
     * @param key
     * @return Attribute of the product
     */
    public Object get(Object key) {
        return _attributes.get(key);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FeedProduct{");
        sb.append("uniqueId='").append(uniqueId).append('\'');
        sb.append(", _attributes=").append(_attributes);
        sb.append(", _variants=").append(_variants);
        sb.append('}');
        return sb.toString();
    }
}
