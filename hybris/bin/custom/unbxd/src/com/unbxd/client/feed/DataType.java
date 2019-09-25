package com.unbxd.client.feed;

public enum DataType {
    TEXT("text"),
    DECIMAL("decimal"),
    NUMBER("number"),
    LONG_TEXT("longText"),
    LINK("link"),
    DATE("date"),
    BOOL("bool"),
    GEO_LAT_LONG("latLongType");

    private String value;

    private DataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DataType fromString(String value){
        for(DataType type : DataType.values()) {
            if(type.getValue().equals(value))
                return type;
        }

        return null;
    }
}
