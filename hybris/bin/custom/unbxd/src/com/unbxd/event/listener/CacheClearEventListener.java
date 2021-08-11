package com.unbxd.event.listener;

import java.util.List;

import javax.annotation.Resource;


import com.unbxd.event.CacheClearEvent;

import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public class CacheClearEventListener extends AbstractEventListener<CacheClearEvent> {


    @Resource
    private CacheConfiguration cacheConfiguration;

    @Override
    protected void onEvent(CacheClearEvent cacheClearEvent) {
        cacheClearEvent.getRegionNames().forEach(name -> {
            List<CacheRegion> cacheRegions = cacheConfiguration.getRegions();
            cacheRegions.forEach(cacheRegion -> {
                if(name.equalsIgnoreCase(cacheRegion.getName()))
                    cacheRegion.clearCache();
            });
        });
    }
}
