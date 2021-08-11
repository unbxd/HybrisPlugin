package com.unbxd.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.List;

public class CacheClearEvent extends AbstractEvent implements ClusterAwareEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> regionNames;

   

    public List<String> getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(List<String> regionNames) {
        this.regionNames = regionNames;
    }

    public CacheClearEvent( List<String> regionNames) {
        this.regionNames = regionNames;
    }

    @Override
    public boolean publish(int sourceNodeID, int targetNodeID) {
        return true;
    }
}
