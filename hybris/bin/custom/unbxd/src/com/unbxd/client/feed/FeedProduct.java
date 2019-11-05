package com.unbxd.client.feed;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class FeedProduct implements InputDocument {
    // Unique Id of the product. Generally corresponds to the SKU.
    private String uniqueId;
    private final Map<String, Object> _attributes;
    private List<Map<String, Object>> _variants;
    private static final String USED_SEPARATOR = Config.getString("solr.indexedproperty.forbidden.char", "_");

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
        if(!(name.startsWith("autosuggest") || name.startsWith("spellcheck"))) {
            String fieldName = StringUtils.substringBefore(name, USED_SEPARATOR);

            Object existingValue = this._attributes.get(fieldName);
            if (existingValue != null) {
                Object derivedValue = addValue(value, existingValue);
                _attributes.put(fieldName, derivedValue);
            } else {
                _attributes.put(fieldName, value);
            }
        }
    }

    public Object addValue(Object newValue,  Object existingValue) {
        Iterator var3;
        Object o;
        if (existingValue == null) {
            if (newValue instanceof Collection) {
                Collection<Object> c = new ArrayList(3);
                var3 = ((Collection) newValue).iterator();

                while (var3.hasNext()) {
                    o = var3.next();
                    c.add(o);
                }

                return c;
            } else {
                return newValue;
            }

        } else {
            Collection<Object> vals = null;
            if (existingValue instanceof Collection) {
                vals = (Collection) existingValue;
            } else {
                vals = new ArrayList(3);
                ((Collection) vals).add(existingValue);
                //return vals;
            }

            if (newValue instanceof Iterable) {
                var3 = ((Iterable) newValue).iterator();

                while (var3.hasNext()) {
                    o = var3.next();
                    ((Collection) vals).add(o);
                }
            } else if (newValue instanceof Object[]) {
                Object[] var7 = (Object[]) ((Object[]) newValue);
                int var9 = var7.length;

                for (int var5 = 0; var5 < var9; ++var5) {
                    Object obj = var7[var5];
                    ((Collection) vals).add(obj);
                }
            } else {
                ((Collection) vals).add(newValue);
            }
            return vals;
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
