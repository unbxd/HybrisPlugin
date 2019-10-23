# Unbxd Product Feed Module For SAP Hybris

This module provide possibility for synchronization product catalog with Unbxd service.

Support Hybris 6.6.\* - 18.08.\*

# Installation Guide

### Install by composer

```
composer require unbxd/magento2-product-feed
php bin/magento module:enable Unbxd_ProductFeed
php bin/magento setup:upgrade
php bin/magento setup:di:compile
php bin/magento cache:flush
```

### Manual installation

1. Download this module [Link](https://github.com/unbxd/HybrisPlugin/archive/master.zip)
3. Unzip module & copy:

    copy <hybris-extension-root>/hybris/bin/custom to <hybris-root>/hybris/bin/custom  
    
4. Enable the Extension:
   add <> in you <hybris-root>/hybris/config/localextensions.xml 
  
5. Configure module in hybris
  add these properties in your local.properties

```
unbxd.sitekey=<Your Unbxd SiteKey>
unbxd.secretkey=<Your Unbxd SecretKey>
unbxd.apikey=<Your Unbxd APIKey>

RUN
ant clean all && ant updatesystem

```
 
