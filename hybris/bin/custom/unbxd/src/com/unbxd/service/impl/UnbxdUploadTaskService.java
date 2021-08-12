package com.unbxd.service.impl;

import javax.annotation.Resource;

import com.unbxd.client.feed.FeedClient;
import com.unbxd.client.feed.FeedClientFactory;
import com.unbxd.client.feed.exceptions.FeedStatusException;
import com.unbxd.client.feed.response.FeedStatusResponse;
import com.unbxd.model.UnbxdSiteConfigModel;
import com.unbxd.model.UnbxdUploadTaskModel;

import de.hybris.platform.servicelayer.model.ModelService;

public class UnbxdUploadTaskService {

    @Resource(name = "modelService")
    private ModelService modelService;


    public void refreshUploadStatus(UnbxdUploadTaskModel uploadTaskObj) {
        try{
            String uploadId = uploadTaskObj.getUploadId();
            boolean isDelta = uploadTaskObj.getIsDelta();
            UnbxdSiteConfigModel siteConfig = uploadTaskObj.getUnbxdSiteConfig();
           
            FeedClient feedClient = FeedClientFactory.getFeedClient(siteConfig.getSiteName(),
            		siteConfig.getSecretKey(),siteConfig.getApiKey(), siteConfig.getDomain());
            FeedStatusResponse feedStatusResponse = null;
            if(isDelta) {
                feedStatusResponse = feedClient.getDeltaStatus(uploadId);
            } else {
                feedStatusResponse = feedClient.getFullStatus(uploadId);
            }
            uploadTaskObj.setStatus(feedStatusResponse.getStatus());
            uploadTaskObj.setTimeStamp(feedStatusResponse.get_timestamp());
            uploadTaskObj.setMessage(feedStatusResponse.getMessage());
            uploadTaskObj.setCode(feedStatusResponse.getCode());
            modelService.save(uploadTaskObj);
        } catch (FeedStatusException  e) {
            e.printStackTrace();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    
}
