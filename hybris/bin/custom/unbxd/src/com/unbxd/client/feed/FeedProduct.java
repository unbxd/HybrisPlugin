package com.unbxd.client.feed;

import java.util.*;

public class FeedProduct {
    // Unique Id of the product. Generally corresponds to the SKU.
    private String uniqueId;
    private final Map<String, Object> _attributes;
    private List<Map<String, Object>> _variants;

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
