package com.unbxd.client.search.response;

/**
 * Created by Sunil on 12/15/16.
 */
public class RedirectData {
    private String type; // URL Rediect or Navigation
    private String value; // URL or Navigation Node Id

    public RedirectData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
