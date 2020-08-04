/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
 
 
  jQuery(document).ready(function () {
    if ("UnbxdHybris" in window && UnbxdHybris.analyticsEnabled) {
      bindCartAction();
      bindOrderAction();
    }
  });


  function trackEvent(eventName,obj){
    internalTrack.call({
        eventName: eventName,
        data:obj,
        counter:1
    })
  }


  function internalTrack(){
    if ("Unbxd" in window && "track" in Unbxd) {
        console.log("Inside internalTrack");
        Unbxd.track(this.eventName, this.data);
    }else{
        console.log("Into ele loop"+this.counter);
        if (this.counter <10){
            this.counter +=1;
        setTimeout(internalTrack.bind(this),200);
        }else{
            console.error(
                eventName+" event not fired (Analytics sdk not loaded)"
              );
        }
    }
  }

  function triggerProductViewEvent() {
    var analyticsConf = window.unbxdMagentoConfig.analytics;
    if ("productId" in analyticsConf && analyticsConf.productId) {
        trackEvent("product_view", { pid: analyticsConf.productId });
    }
  }

  function bindCartAction() {
    window.mediator.subscribe('trackAddToCart', function(data) {
      if (data.productCode && data.quantity && data.variant) {
          trackEvent("addToCart", { pid: data.productCode, qty: ata.quantity,variantId: data.variant});
      }
      if (data.productCode && data.quantity) {
         trackEvent("addToCart", { pid: data.productCode, qty: ata.quantity});
      }
  });

  window.mediator.subscribe('trackRemoveFromCart', function(data) {
      if (data.productCode && data.initialCartQuantity) {
        trackEvent("cartRemoval", { pid: data.productCode, qty: data.initialCartQuantity});
      }
  });
  }



  function bindOrderAction() {
      try {
        if (
          "UnbxdAnalyticsConf" in window &&
          UnbxdAnalyticsConf.order &&
          UnbxdAnalyticsConf.order.length
        ) {
            window.UnbxdAnalyticsConf.order.forEach(function (item) {
                trackEvent("order", item);
          });
        }
      }catch (E) {
        console.log(e);
      }
  }
