package com.unbxd.client.search.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by unbxd on 8/19/15.
 */

public class Banners {
    protected ArrayList<String> categories;
    protected ArrayList<LinkedHashMap<String, String>> banners;
    protected List<Banner> _banner;

    public Banners(Map<String, Object> params) {
        this._banner = new ArrayList<Banner>();
        this.banners = (ArrayList<LinkedHashMap<String, String>>) params.get("banners");
        if(this.banners.size()>0) {
            for (LinkedHashMap<String, String> aBanner : banners) {
                Banner banner = new Banner(aBanner);
                _banner.add(banner);
            }
        }


        this.categories = (ArrayList<String>) params.get("categories");
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public List<Banner> getBanner() {
        return _banner;
    }
}
