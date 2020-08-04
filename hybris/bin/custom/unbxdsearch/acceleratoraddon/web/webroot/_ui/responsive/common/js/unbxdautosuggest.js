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
 
 
  $(document).ready(function () {
    if ("UnbxdHybris" in window && UnbxdHybris.autosuggestConfig) {
      initAutoSuggest.call({ counter:  1 });
    } else {
      console.error("Unbxd Autosuggest config not found");
    }
  });
  
  
  function initAutoSuggest() {
    if ("unbxdAutoSuggestFunction" in window) {
      unbxdAutoSuggestFunction(jQuery, Handlebars);
      if (
        UnbxdHybris.autosuggestConfig.searchSelector || true
      ) {
        $('#js-site-search-input').unbxdautocomplete(
          UnbxdHybris.autosuggestConfig
        );
    } else {
      if (this.counter > 10) {
        console.error("UnbxdAutosuggest SDK not loaded");
      } else {
        setTimeout(
          initAutoSuggest.bind({ counter: this.counter + 1 }),
          250
        );
      }
    }
  }
}