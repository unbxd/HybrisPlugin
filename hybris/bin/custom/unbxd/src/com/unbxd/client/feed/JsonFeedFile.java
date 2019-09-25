package com.unbxd.client.feed;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

public class JsonFeedFile {
    private JSONObject feed;

    public JsonFeedFile(Collection<FeedField> fields,
                        Collection<FeedProduct> addedDocs,
                        Collection<FeedProduct> updatedDocs,
                        Collection<String> deletedDocs) {
        feed = new JSONObject();
        JSONObject root = new JSONObject();
        try {
            if (fields.size() > 0 || addedDocs.size() > 0 || updatedDocs.size() > 0 || deletedDocs.size() > 0) {
                JSONObject catalog = new JSONObject();

                if (fields.size() > 0)
                    catalog.put("schema", writeSchema(fields));

                if (addedDocs.size() > 0) {
                    catalog.put("add", writeAdd(addedDocs));
                }

                if (updatedDocs.size() > 0) {
                    catalog.put("update", writeUpdate(updatedDocs));
                }

                if (deletedDocs.size() > 0) {
                    catalog.put("delete", writeDelete(deletedDocs));
                }
                root.put("catalog", catalog);

            }

            feed.put("feed", root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray writeSchema(Collection<FeedField> fields) {
        JSONArray schema = new JSONArray();
        for (FeedField field : fields) {
            JSONObject jsonField = new JSONObject();
            jsonField.put("fieldName", field.getName());
            jsonField.put("dataType", field.getDataType().getValue());
            jsonField.put("multiValued", new Boolean(field.isMultiValued()).toString());
            jsonField.put("autoSuggest", new Boolean(field.isMultiValued()).toString());

            schema.appendElement(jsonField);
        }
        return schema;
    }

    private void writeAttribute(JSONObject item, String field, Object o, boolean associated) {
        Collection<Object> values;
        if (o instanceof Collection) {
            values = (Collection<Object>) o;
        } else {
            values = Arrays.asList(o);
        }

        for (Object value : values) {
            if (value == null) continue;

            String str = value.toString();
            str = str.replaceAll("[\u0000-\u001f]", "");
            str = StringEscapeUtils.escapeXml(str);
            item.put(field + (associated ? "Associated" : ""), str);
        }
    }

    private JSONObject writeAdd(Collection<FeedProduct> addedDocs) {
        JSONObject obj = new JSONObject();
        JSONArray items = new JSONArray();
        for (FeedProduct product : addedDocs) {
            JSONObject item = new JSONObject();

            for (String field : product.getAttributes().keySet()) {
                writeAttribute(item, field, product.get(field), false);
            }

            if (product.getAssociatedProducts() != null && product.getAssociatedProducts().size() > 0) {
                JSONArray associatedProducts = new JSONArray();
                for (Map<String, Object> associatedProduct : product.getAssociatedProducts()) {
                    JSONObject associatedItem = new JSONObject();
                    for (String field : associatedProduct.keySet()) {
                        writeAttribute(associatedItem, field, associatedProduct.get(field), true);
                    }
                    associatedProducts.appendElement(associatedItem);
                }
                item.put("associatedProducts", associatedProducts);
            }
            items.appendElement(item);
        }
        obj.put("items", items);
        return obj;
    }

    private JSONObject writeUpdate(Collection<FeedProduct> updatedDocs) {
        JSONObject obj = new JSONObject();
        JSONArray items = new JSONArray();
        for (FeedProduct product : updatedDocs) {
            JSONObject item = new JSONObject();
            for (String field : product.getAttributes().keySet()) {
                writeAttribute(item, field, product.get(field), false);
            }

            items.appendElement(item);
        }
        obj.put("items", items);
        return obj;
    }

    private JSONObject writeDelete(Collection<String> deletedDocs) {
        JSONObject obj = new JSONObject();
        JSONArray items = new JSONArray();
        for (String uniqueId : deletedDocs) {
            JSONObject item = new JSONObject();
            item.put("uniqueId", uniqueId);
            items.appendElement(item);
        }
        obj.put("items", items);
        return obj;
    }

    public JSONObject getJson() {
        return feed;
    }
}
