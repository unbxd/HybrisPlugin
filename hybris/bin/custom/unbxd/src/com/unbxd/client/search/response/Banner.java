package com.unbxd.client.search.response;

import java.util.LinkedHashMap;

/**
 * Created by unbxd on 8/19/15.
 */
public class Banner {
    public static String imageUrl;
    public static String  landingPageUrl;

    public Banner(LinkedHashMap<String, String> banner) {
        if(banner.containsKey("imageUrl")) {
            this.imageUrl = banner.get("imageUrl");
        }
        else{
            this.imageUrl = null;
        }
        if(banner.containsKey("landingUrl")){
            this.landingPageUrl = banner.get("landingUrl");
        }
        else{
            this.landingPageUrl = null;
        }

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }
}