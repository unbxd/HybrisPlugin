"use strict";
import './dot';
import { eventHandlers, setImagesSource, sendWarning } from './handlers';
import { getRatingContent } from './ratings';
import { strikeThrough } from './strikeThrough';
import environment from './environment';
(function (global) {

    /**
     * Global declaration section
     */

    /** Function for fetching api requests */
    function fetchData(url, cb) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && (this.status == 200 || this.status == 204)) {
                // Typical action to be performed when the document is ready:
                cb(null, xhttp.responseText);
            }
            else if (this.readyState == 4 && (this.status != 200 || this.status != 204)) {
                cb('Invalid network request: ' + url);
            }
        };
        xhttp.onerror = function () {
            cb('Failed network request: ' + url);
        }
        xhttp.open("GET", url, true);
        xhttp.send();
    }

    /** This function find the node containing selector passed as param
     *  closest to element passed as param
     *  The purpose of this function is to find the target tile parent w.r.t
     *  event target. This was done to minimize the no of click handlers
     * */
    function getClosestNode(elem, selector) {

        var firstChar = selector.charAt(0);

        // Get closest match
        for ( ; elem && elem !== document; elem = elem.parentNode ) {

            // If selector is a class
            if ( firstChar === '.' ) {
                if ( elem.classList.contains( selector.substr(1) ) ) {
                    return elem;
                }
            }

            // If selector is an ID
            if ( firstChar === '#' ) {
                if ( elem.id === selector.substr(1) ) {
                    return elem;
                }
            }

            // If selector is a data attribute
            if ( firstChar === '[' ) {
                if ( elem.hasAttribute( selector.substr(1, selector.length - 2) ) ) {
                    return elem;
                }
            }

            // If selector is a tag
            if ( elem.tagName.toLowerCase() === selector ) {
                return elem;
            }

        }

        return false;

    };

    var MOBILE = 'mobile';
    var DESKTOP = 'desktop';
    var SMALL = 'small';
    var LARGE = 'large';

    function getDeviceType() {
        console.log("screen.width: ", window.screen.width);
        console.log("screen.height: ", window.screen.height);
        const mediaQueryList = window.matchMedia("(orientation: portrait)");
        if(mediaQueryList.matches) {
            console.log('portrait mode')
            if (window.screen.width <= 667) {
                return MOBILE;
            } else {
                return DESKTOP;
            }
        } else {
            console.log('landscape mode')
            if (window.screen.height <=667) {
                return MOBILE;
            } else {
                return DESKTOP;
            }
        }
    }

    function getBrowserSize() {
        console.log("window.innerWidth: ", window.innerWidth);
        console.log("window.innerHeight: ", window.innerHeight);
        if (window.innerWidth <= 667) {
            return SMALL;
        } else {
            return LARGE;
        }
    }


    /** Global variables */
        // the domain url
        // var platformDomain = 'http://localhost:4201/';
        // console.log(process.env.NODE_ENV);
    var platformDomain = environment[process.env.NODE_ENV].url;
    // var platformDomain = 'https://console-lohika.0126-int-use2.unbxd.io/v2.0/';
    // Constants
    var HOME_PAGE = 'home';
    var PRODUCT_PAGE = 'product';
    var CATEGORY_PAGE = 'category';
    var CART_PAGE = 'cart';
    var allowedPageTypes = [HOME_PAGE, PRODUCT_PAGE, CATEGORY_PAGE, CART_PAGE];

    var widgetIdMap = {};
    widgetIdMap[HOME_PAGE] = {
        'widget1': 'unbxd_rex_' + HOME_PAGE + '_w1',
        'widget2': 'unbxd_rex_' + HOME_PAGE + '_w2',
        'widget3': 'unbxd_rex_' + HOME_PAGE + '_w3'
    };
    widgetIdMap[PRODUCT_PAGE] = {
        'widget1': 'unbxd_rex_' + PRODUCT_PAGE + '_w1',
        'widget2': 'unbxd_rex_' + PRODUCT_PAGE + '_w2',
        'widget3': 'unbxd_rex_' + PRODUCT_PAGE + '_w3'
    };
    widgetIdMap[CATEGORY_PAGE] = {
        'widget1': 'unbxd_rex_' + CATEGORY_PAGE + '_w1',
        'widget2': 'unbxd_rex_' + CATEGORY_PAGE + '_w2',
        'widget3': 'unbxd_rex_' + CATEGORY_PAGE + '_w3'
    };
    widgetIdMap[CART_PAGE] = {
        'widget1': 'unbxd_rex_' + CART_PAGE + '_w1',
        'widget2': 'unbxd_rex_' + CART_PAGE + '_w2',
        'widget3': 'unbxd_rex_' + CART_PAGE + '_w3'
    }

    // Declaration of template containers
    var widget1;
    var widget2;
    var widget3;

    // Horizontal template config containers
    var horizontalConfig;
    var horizontalAssets;

    // Vertical template config containers
    var verticalConfig;
    var verticalAssets;
    // Setting constant values for margin between slider items and the DOM id for the slider
    var margin = 10;
    /** End of Global variables */

    /**
     * End of Global declaration section
     */

    /** Scripts and styles that are appended to the DOM */

    /** Adding event handlers for the horizontal slider to the DOM */


    global._unbxd_recsSliderScrollNext = eventHandlers._unbxd_recsSliderScrollNext;
    global._unbxd_recsSliderScrollPrev = eventHandlers._unbxd_recsSliderScrollPrev;
    global._unbxd_recsSliderSideScroll = eventHandlers._unbxd_recsSliderSideScroll;

    global._unbxd_recsSliderScrollBottom = eventHandlers._unbxd_recsSliderScrollBottom;
    global._unbxd_recsSliderScrollTop = eventHandlers._unbxd_recsSliderScrollTop;
    global._unbxd_recsSliderVerticalScroll = eventHandlers._unbxd_recsSliderVerticalScroll;




    /** End of Scripts and styles that are appended to the DOM */


        // Configuration object for vertical/horizontal sliders
    var sliderConfig = {
            horizontal: {
                containerId: " #_unbxd_recs-slider-container",
                sliderItemClassSelector: " ._unbxd_recs-slider__item",
                dimension: "width",
                offsetDimension: "offsetWidth",
                buttonClassSelector: "._unbxd_recs-slider-btn",
                prevButtonClass: "_unbxd_rex-slider--prev",
                nextButtonClass: "_unbxd_rex-slider--next",
                headingContainerId: " #_unbxd_recs-slider-heading",
                sliderContentClass: "_unbxd_recs-slider__content"
            },
            vertical: {
                containerId: " #_unbxd_recs-vertical-slider-container",
                sliderItemClassSelector: " ._unbxd_recs-vertical-slider__item",
                dimension: "height",
                offsetDimension: "offsetHeight",
                buttonClassSelector: "._unbxd_recs-vertical-slider-btn",
                prevButtonClass: "_unbxd_rex-vertical-slider--top",
                nextButtonClass: "_unbxd_rex-vertical-slider--bottom",
                headingContainerId: " #_unbxd_recs-vertical-slider-heading",
                sliderContentClass: "_unbxd_recs-vertical-slider__content"
            }
        }

    function missingValueError(valueKey, contentObject) {
        throw new Error('Error: ' + valueKey + ' not found in ' + JSON.stringify(contentObject));
    }

    function handleHorizontalWidgetClicks(parentId, clickHandler, recommendations){
        var hzRegex = /hz-item/;
        if (hzRegex.test(parentId)) {
            var arrayIndex = parentId.split("-")[2]; // fixed id of form hz-slider-0
            clickHandler(recommendations[arrayIndex]);
        }
    }

    function handleVerticalWidgetClicks(parent1Id, parent2Id, clickHandler, recommendationsModified){
        var vtRegex = /[0-9]-vt-level2-/;
        if (vtRegex.test(parent1Id)) {
            var parent1ArrayIndex = parent1Id.split("-")[3]; // fixed id of form *-vt-slider-0
            var parent2ArrayIndex = parent2Id.split("-")[3];
            clickHandler(recommendationsModified[parent2ArrayIndex][parent1ArrayIndex]);
        }
    }

    function handleSizeCalculations(targetDOMElementId, options) {
        var rexConsoleConfigs = options.rexConsoleConfigs;
        var recommendations = options.recommendations;
        var clickHandler = options.clickHandler;
        var itemsToShow = options.itemsToShow;
        var maxProducts = options.maxProducts;
        var assets = options.assets;
        var sliderType = options.sliderType;
        var sliderClass = options.sliderClass;
        var recommendationsModified = options.recommendationsModified;
        var sliderContent = sliderConfig[sliderType]
        var domSelector = "#" + targetDOMElementId + sliderContent.containerId;
        var sliderContainer = document.querySelector(domSelector);
        var widgetWidth = options.widgetWidth;
        var ratingFeature = rexConsoleConfigs.products.ratings_feature || rexConsoleConfigs.products.ratingsFeature;

        if (!sliderContainer) {
            return sendWarning('The slider container id was not found. Script can not continue');
        }

        var sliderItemSelector = "#" + targetDOMElementId + sliderContent.sliderItemClassSelector;
        var sliderItems = document.querySelectorAll(sliderItemSelector);

        if (!sliderItems.length) {
            return sendWarning('Found 0 nodes with class : ' + sliderContent.sliderItemClassSelector);
        }

        var productFields = rexConsoleConfigs.products.fields || missingValueError('products.fields', rexConsoleConfigs);

        productFields = productFields.sort(function(b,a){
            a.sequence = a.sequence || a.sequence_number;
            b.sequence = b.sequence || b.sequence_number;
            if(a.sequence  < b.sequence){
                return 1;
            }
            return -1;
        });

        var dimension = sliderContent.dimension;

        if (clickHandler) {
            if (sliderContent.dimension == "width") {
                sliderContainer.addEventListener("click", function (event) {
                    if (event.target.className == "_unbxd_recs-slider__item"){
                        handleHorizontalWidgetClicks(event.target.id, clickHandler, recommendations);
                    }
                    else{
                        var el = getClosestNode(event.target,"._unbxd_recs-slider__item")
                        handleHorizontalWidgetClicks(el.id, clickHandler, recommendations);
                    }
                });
            }
            else {
                sliderContainer.addEventListener("click", function (event) {
                    if(event.target.className == "_unbxd_recs-vertical-slider__item"){
                        var parentId = event.target.parentElement.id;
                        handleVerticalWidgetClicks(event.target.id, parentId, clickHandler, recommendationsModified);
                    }
                    else{
                        var el = getClosestNode(event.target,"._unbxd_recs-vertical-slider__item");
                        var parentId = el.parentElement.id;
                        handleVerticalWidgetClicks(el.id, parentId, clickHandler, recommendationsModified);
                    }
                });
            }
        }

        for (var i = 0; i < sliderItems.length; i++) {
            var fragment = document.createDocumentFragment();
            for (var j = 0; j < productFields.length; j++) {
                var styles = productFields[j].styles || missingValueError('styles', productFields[j]);
                var productAttributeKey = productFields[j].unbxdDimensionKey || productFields[j].catalogKey || missingValueError('unbxdDimensionKey or catalogKey', productFields[j]);
                var cssArr = Object.keys(styles);
                if(!recommendations[i][productAttributeKey]){
                    productAttributeKey = productFields[j].catalogKey;
                }
                // appending fields to slider item
                // field appending doesn't applies to imageUrl
                if (productAttributeKey != "imageUrl") {
                    var newnode = document.createElement("p");
                    var dimension = recommendations[i][productAttributeKey];
                    newnode.className = sliderContent.sliderContentClass;
                    if(rexConsoleConfigs.products.strike_price_feature && productAttributeKey == rexConsoleConfigs.products.strike_price_feature.new.field){
                        if(rexConsoleConfigs.products.strike_price_feature.enabled){
                            newnode.innerHTML = strikeThrough(recommendations[i], rexConsoleConfigs, domSelector);
                        }
                        else{
                            newnode.innerHTML = rexConsoleConfigs.products.currency+ dimension;
                        }
                    }
                    else if(ratingFeature &&
                        ratingFeature.enabled &&
                        productFields[j].unbxdDimensionKey &&
                        productFields[j].unbxdDimensionKey.toLowerCase() == "rating" ){
                        var ratingContentData = getRatingContent(recommendations[i], ratingFeature, domSelector, productAttributeKey);
                        if(ratingContentData){
                            newnode.innerHTML = ratingContentData;
                        }
                    }
                    else {
                        if (!dimension) {
                            newnode.innerHTML = "";
                        }
                        else {
                            newnode.innerHTML = dimension;
                        }
                    }

                    if (newnode.innerHTML) {
                        for(var k=0; k< cssArr.length; k++){
                            newnode.style[cssArr[k]] = styles[cssArr[k]];
                        }
                        fragment.appendChild(newnode);
                    }
                }
            }

            sliderItems[i].appendChild(fragment);
        }

        // Setting width of each slider item and
        // setting width of the visible slider
        var recsSliderSelector = "#" + targetDOMElementId + " ." + sliderClass;
        var recsSlider = document.querySelector(recsSliderSelector);
        if (!recsSlider) {
            return sendWarning('Slider Parent id was not found in the DOM');
        }

        var maxprodLimit = maxProducts;
        if (recommendations.length < maxProducts) {
            maxprodLimit = recommendations.length
        }

        function incrementCounter() {
            counter++;
            if (counter === len) {
                if (sliderContent.dimension == "width") {

                    setTimeout(function () {
                        var sliderParentContainer = document.querySelector("#" + targetDOMElementId + " .unbxd-recs-slider");
                        var sliderRootContainer = sliderParentContainer.parentElement;
                        sliderParentContainer.style.width = widgetWidth || "initial";
                        if (sliderRootContainer.clientWidth < sliderParentContainer.clientWidth) {
                            sliderParentContainer.style.width = sliderRootContainer.clientWidth + "px";
                        }
                        sliderContainer.style.width = sliderContainer[sliderContent.offsetDimension] + "px";
                        var hzSliderWidth = (sliderContainer[sliderContent.offsetDimension] - (itemsToShow * margin)) / itemsToShow;
                        for (i = 0; i < sliderItems.length; i++) {
                            sliderItems[i].style.width = hzSliderWidth + "px";
                            recsSlider.style.width = (maxprodLimit * hzSliderWidth) + (maxprodLimit) * margin + "px";
                        }
                        var opaqueElSelector = document.querySelector("#"+targetDOMElementId + " ._unxbd_slider_hide");
                        opaqueElSelector.classList.remove("_unxbd_slider_hide");

                    }, 0);

                }
                else {

                    setTimeout(function () {
                        var sliderParentContainer = document.querySelector("#" + targetDOMElementId + " ._unbxd_vertical-recs-slider");
                        var sliderRootContainer = sliderParentContainer.parentElement;
                        // if root container width is less than configuration width, then
                        // the container inherits root container width 
                        sliderParentContainer.style.width = widgetWidth || "initial";
                        if(sliderRootContainer.clientWidth < sliderParentContainer.clientWidth){
                            sliderParentContainer.style.width = sliderRootContainer.clientWidth + "px";
                        }
                        for (i = 0; i < sliderItems.length; i++) {
                            sliderItems[i].style.width = sliderParentContainer.clientWidth - 2 * margin + "px";
                        }
                        recsSlider.style.width = (sliderParentContainer.clientWidth) * recommendationsModified.length + "px";
                        var opaqueElSelector = document.querySelector("#"+targetDOMElementId + " ._unxbd_slider_hide");
                        opaqueElSelector.classList.remove("_unxbd_slider_hide");
                    }, 0);

                }

            }
        }

        var imgs = document.images,
            len = imgs.length,
            counter = 0;

        [].forEach.call(imgs, function (img) {
            if (img.complete)
                incrementCounter();
            else
                img.addEventListener('load', incrementCounter, false);
        });


        /** Setting styles for carousel buttons */
        // the navigation button need to be hidden in case the total no of items to be shown
        // are less than the no of items to be shown at in one slide 
        if (recommendations.length <= itemsToShow) {
            var navigationButtonSelector = "#" + targetDOMElementId + " " + sliderContent.buttonClassSelector;
            var navigationButtons = document.querySelectorAll(navigationButtonSelector);
            if (!navigationButtons || !navigationButtons.length) {
                return sendWarning(sliderContent.buttonClassSelector + 'class not found on navigation buttons');
            }
            for (var i = 0; i < navigationButtons.length; i++) {
                navigationButtons[i].style.display = 'none';
            }
        }

        // the previous button for the slider needs to be disabled initially
        var prevSliderButtonSelector = "#" + targetDOMElementId + " ." + sliderContent.prevButtonClass;
        var prevSliderButton = document.querySelector(prevSliderButtonSelector);

        if (!prevSliderButton) {
            return sendWarning(sliderContent.prevButtonClass + ' class was not found on the navigation buttons');
        }
        prevSliderButton.disabled = true;

        /** Setting images value */
        var imgArr = [];
        var classMap = {
            "next_arrow": sliderContent.nextButtonClass,
            "prev_arrow": sliderContent.prevButtonClass,
            "empty_rating": "_unbxd_rex-empty-star",
            "half_rating": "_unbxd_rex-half-star",
            "full_rating": "_unbxd_rex-full-star"
        }
        for (i = 0; i < assets.length; i++) {
            var horizontalAssetItem = assets[i];
            imgArr.push(
                {
                    classname: classMap[horizontalAssetItem.tag],
                    url: horizontalAssetItem.src
                }
            );
        }
        setImagesSource(imgArr, targetDOMElementId);

        /** Setting images value end*/

        /** Setting styles for heading */

        var headingSelector = "#" + targetDOMElementId + sliderContent.headingContainerId;
        var styleConfig = rexConsoleConfigs.header;
        var headingEl = document.querySelector(headingSelector);
        if (headingEl.innerHTML == "null" || headingEl.innerHTML == "undefined") {
            headingEl.style.display = "none";
        }
        else {
            headingEl.style.textAlign = styleConfig.alignment;
            headingEl.style.fontSize = styleConfig.text.size.value + styleConfig.text.size.unit;
            headingEl.style.fontWeight = styleConfig.text.style;
            headingEl.style.color = styleConfig.text.colour;
        }

        /** End of Setting styles for heading */
    }

    /** exporting a global function to initialize recs slider */
    global._unbxd_generateRexContent = function (options) {
        // console.log(options)
        /** Template rendering logic */
        var template = options.template || missingValueError('template', options);
        var targetDOMElementId = options.targetDOMElementId || missingValueError('targetDOMElementId', options);
        var recommendations = options.recommendations || missingValueError('recommendations', options);
        var heading = options.heading || missingValueError('heading', options);
        var rexConsoleConfigs = options.rexConsoleConfigs || missingValueError('rexConsoleConfigs', options);
        var itemsToShow = rexConsoleConfigs.products.visible || missingValueError('products.visible', rexConsoleConfigs);
        var maxProducts = rexConsoleConfigs.products.max || missingValueError('products.max', rexConsoleConfigs.products);
        var clickHandler = options.clickHandler;
        var dataParser = options.dataParser;
        var isVertical = options.isVertical;
        var compressedStyle = rexConsoleConfigs.css || missingValueError('css',rexConsoleConfigs);
        var recommendationsModified = null;
        var widgetWidthData = rexConsoleConfigs.widget.width || missingValueError('products.widget.width', rexConsoleConfigs.widget);
        // var widgetWidthData = verticalConfig.width;
        var widgetWidth = "";
        if (widgetWidthData.value && widgetWidthData.value != 0) {
            widgetWidth = widgetWidthData.value + widgetWidthData.unit;
        }

        var renderFn = doT.template(template);
        var renderTargetEl = document.getElementById(targetDOMElementId);

        // console.log(screen.width)
        // console.log(window.innerWidth);
        var device = getDeviceType();
        var browserSize = getBrowserSize();
        if (device === MOBILE || browserSize === SMALL) {
            const itemsToShowOnMobile = rexConsoleConfigs.products.visibleOnMobile;
            itemsToShow = itemsToShowOnMobile ? itemsToShowOnMobile : 2;
        }

        if (!renderTargetEl) {
            return sendWarning('The target element id <' + targetDOMElementId + '> is not present in DOM. Execution can not continue');
        }

        if (maxProducts < recommendations.length) {
            recommendations = recommendations.splice(0, maxProducts);
        }

        if (isVertical) {
            recommendationsModified = [];
            for (var i = 0; i < recommendations.length; i++) {
                if (i % (itemsToShow) === 0) {
                    var slicedItems = recommendations.slice(i, i + itemsToShow);
                    recommendationsModified.push(slicedItems);
                }
            }
        }

        var templateData = {
            recommendations: recommendationsModified || recommendations,
            heading: heading
        }

        /* Callback to make any modification to data and pass on the modified data to renderFn  */
        if (dataParser && typeof(dataParser) === "function") {
            templateData = dataParser(templateData)
        }

        document.getElementById(targetDOMElementId).innerHTML = renderFn(templateData);

        /** Dynamically adjusting width based on no of items to be shown */
        var sliderOptionsConfig = {
            rexConsoleConfigs: rexConsoleConfigs,
            recommendations: recommendations,
            recommendationsModified: recommendationsModified,
            clickHandler: clickHandler,
            itemsToShow: itemsToShow,
            maxProducts: maxProducts,
            assets: options.assets,
            sliderType: isVertical ? "vertical" : "horizontal",
            sliderClass: isVertical ? "_unbxd_recs-vertical-slider" : "_unbxd_recs-slider",
            widgetWidth: widgetWidth
        }

        // no of items to be shown
        if (isVertical) {
            global._unbxd_recsItemToScrollVt = itemsToShow;
        }
        else {
            global._unbxd_recsItemToScrollHz = itemsToShow;
        }


        /** Attaching styles for the slider */
        var eventHandlerStyle = document.createElement('style');
        eventHandlerStyle.type = 'text/css';
        // innerHTML needs to stay as es5 since it will be embedded directly to client's browser
        eventHandlerStyle.innerHTML = compressedStyle;
        document.head.appendChild(eventHandlerStyle);

        handleSizeCalculations(targetDOMElementId, sliderOptionsConfig);
    }


    /** The initialization function that has to be exposed to the merchandiser website
     *  it takes context object from the client html
     *  and makes a call to the recommender proxy
     *  and updates the dom as per the response
     */
    global._unbxd_getRecommendations = function (context) {
        // Get widget id
        function getWidgetId(pageType, widgetKey, widgetDetails) {
            console.log(pageType, widgetKey, widgetDetails)
            var widgetIdLocal;
            if (widgetDetails) {
                return widgetDetails[widgetKey] ? widgetDetails[widgetKey].name : null;
            } else {
                widgetIdLocal = widgetIdMap[pageType.toLowerCase()][widgetKey];
                // Check if widget exists in the page
                if (document.getElementById(widgetIdLocal)) {
                    return widgetIdLocal;
                } else {
                    return null;
                }
            }
            return null;
        }

        function getPageDetails(pageInfo) {
            if (!pageInfo || !pageInfo.pageType) {
                throw new Error("Page type info missing");
            }
            var pageTypeLocal = pageInfo.pageType;
            if (allowedPageTypes.indexOf(pageTypeLocal.toLowerCase()) == -1) {
                throw new Error("Invalid value for page type");
            }
            return pageTypeLocal;
        }

        function getClickHandler(context) {
            return context.itemClickHandler;
        }

        function getDataParserHandler(context) {
            return context.dataParser;
        }

        function getUrlEncodedParam(paramName, paramValue) {
            return "" + paramName + "=" + encodeURIComponent(paramValue);
        }

        function getProductIdsAsUrlParams(productIdsList) {
            var queryStringLocal = '';
            if (productIdsList instanceof Array) {
                productIdsList.forEach(function (item) {
                    queryStringLocal += '&' + getUrlEncodedParam('id', item);
                });
            } else {
                queryStringLocal += '&' + getUrlEncodedParam('id', productIdsList);
            }
            return queryStringLocal;
        }

        // getting page info
        var pageType = getPageDetails(context.pageInfo);

        // get widget if
        var widgets = context.widgets;
        widget1 = getWidgetId(pageType, 'widget1', widgets);
        widget2 = getWidgetId(pageType, 'widget2', widgets);
        widget3 = getWidgetId(pageType, 'widget3', widgets);
        if (!widget1 && !widget2 && !widget3) {
            throw new Error('No widget id provided');
        }
        var itemClickHandler = getClickHandler(context);
        var dataParser = getDataParserHandler(context);

        // getting userId, siteKey and apiKey
        var userInfo = context.userInfo;
        if (!userInfo) {
            throw new Error("User info missing")
        }

        var userId = userInfo.userId;
        var siteKey = userInfo.siteKey;
        var apiKey = userInfo.apiKey;

        var requestUrl = platformDomain + apiKey + "/" + siteKey + '/items?&template=true&pageType=';

        if (!userId) {
            throw new Error("user id is missing");
        }

        if (!siteKey) {
            throw new Error("site Key is missing");
        }

        if (!apiKey) {
            throw new Error("api key is missing");
        }

        requestUrl += encodeURIComponent(pageType);
        var pageInfo = context.pageInfo;
        switch (pageType.toLowerCase()) {
            case PRODUCT_PAGE:
            case CART_PAGE:
                if (!pageInfo.productIds) {
                    throw new Error("product id is missing for page type:" + pageType);
                }
                requestUrl += getProductIdsAsUrlParams(pageInfo.productIds);
                break;
            case CATEGORY_PAGE:
                var catlevel1Name = pageInfo.catlevel1Name;
                if (!catlevel1Name) {
                    throw new Error("catlevel1Name is mandatory for page type:" + pageType);
                }
                var catlevel2Name = pageInfo.catlevel2Name;
                var catlevel3Name = pageInfo.catlevel3Name;
                var catlevel4Name = pageInfo.catlevel4Name;
                var categoryUrl = "&" + getUrlEncodedParam("catlevel1Name", catlevel1Name);
                if (catlevel2Name) {
                    categoryUrl += "&" + getUrlEncodedParam("catlevel2Name", catlevel2Name);
                    if (catlevel3Name) {
                        categoryUrl += "&" + getUrlEncodedParam("catlevel3Name=", catlevel3Name);
                        if (catlevel4Name) {
                            categoryUrl += "&" + getUrlEncodedParam("catlevel4Name=", catlevel4Name);
                        }
                    }
                }
                requestUrl += categoryUrl;
                break;
            case HOME_PAGE:
                break;
            default:
                throw new Error("Invalid page type: " + pageType);
        }

        requestUrl += "&uid=" + userId;

        function renderWidgetDataHorizontal(widget, recommendations, heading) {
            var maxProducts = horizontalConfig.products.max || horizontalConfig.products.max_products;
            var targetDOMElementId = widget;
            var clickHandler = itemClickHandler;
            if (recommendations.length) {
                if (maxProducts < recommendations.length) {
                    recommendations = recommendations.splice(0, maxProducts);
                }
                var options = {
                    template: horizontalTemplate,
                    targetDOMElementId: targetDOMElementId,
                    recommendations: recommendations,
                    heading: heading,
                    rexConsoleConfigs: horizontalConfig,
                    assets: horizontalAssets,
                    maxProducts: maxProducts,
                    clickHandler: clickHandler,
                    dataParser: dataParser,
                    sliderClass: "_unbxd_recs-slider",
                    compressedStyle: compressedStyle
                }
                _unbxd_generateRexContent(options);
            }
        }


        function renderWidgetDataVertical(widget, recommendations, heading) {
            var maxProducts = verticalConfig.products.max || verticalConfig.products.max_products;
            var targetDOMElementId = widget;
            var clickHandler = itemClickHandler;
            if (recommendations.length) {
                if (maxProducts < recommendations.length) {
                    recommendations = recommendations.splice(0, maxProducts);
                }

                var options = {
                    template: verticalTemplate,
                    targetDOMElementId: targetDOMElementId,
                    recommendations: recommendations,
                    heading: heading,
                    rexConsoleConfigs: verticalConfig,
                    assets: verticalAssets,
                    maxProducts: maxProducts,
                    clickHandler: clickHandler,
                    dataParser: dataParser,
                    isVertical: true,
                    sliderClass: "_unbxd_recs-vertical-slider",
                    compressedStyle: compressedStyleVertical

                }
                _unbxd_generateRexContent(options);
            }
        }

        function handleWidgetRenderingVertical() {
            if (widget3) {
                var widget3Data = recommendationsResponse.widget3;
                var widget3Heading = widget3Data.widgetTitle;
                var widget3Recommendations = widget3Data.recommendations;
                renderWidgetDataVertical(widget3, widget3Recommendations, widget3Heading);
            }
        }

        function handleWidgetRendering() {
            if (widget1) {
                var widget1Data = recommendationsResponse.widget1;
                var widget1Heading = widget1Data.widgetTitle;
                var widget1Recommendations = widget1Data.recommendations;
                renderWidgetDataHorizontal(widget1, widget1Recommendations, widget1Heading);
            }
            if (widget2) {
                var widget2Data = recommendationsResponse.widget2;
                var widget2Heading = widget2Data.widgetTitle;
                var widget2Recommendations = widget2Data.recommendations;
                renderWidgetDataHorizontal(widget2, widget2Recommendations, widget2Heading);
            }

        }

        function horizontalTemplateHandler(err, res) {
            if (err) {
                throw new Error('Failed to fetch templates');
            }
            // populating the template string
            horizontalTemplate = res;
            handleWidgetRendering();
        }

        function verticalTemplateHandler(err, res) {
            if (err) {
                throw new Error('Failed to fetch templates');
            }
            // populating the template string
            verticalTemplate = res;
            handleWidgetRenderingVertical();
        }

        /** Fetch recommendations response */
            // to store recommendations response
        var recommendationsResponse;
        // to store template string
        var horizontalTemplate;
        // to store vertical template string
        var verticalTemplate;
        var compressedStyle;
        var compressedStyleVertical;
        fetchData(requestUrl, function (err, res) {
            // fetching data specific to a page type
            if (err) {
                throw new Error('Failed to fetch recommendations');
            }
            recommendationsResponse = JSON.parse(res);

            // horizontal templates configuration
            horizontalTemplate = recommendationsResponse.template.horizontal;
            if(horizontalTemplate){
                horizontalConfig = horizontalTemplate.conf;
                horizontalAssets = horizontalConfig.assets;
                var templateUrlHorizontal = horizontalTemplate.scriptUrl;
                if(templateUrlHorizontal){

                    /** Fetch template layout string */
                    fetchData(templateUrlHorizontal, horizontalTemplateHandler);
                }
                else{
                    console.warn("script url not found for horizontal template")
                }
            }
            // vertical templates configuration
            verticalTemplate = recommendationsResponse.template.vertical;
            if(verticalTemplate){
                verticalConfig = verticalTemplate.conf;
                verticalAssets = verticalConfig.assets;
                var templateUrlVertical = verticalTemplate.scriptUrl;
                if(templateUrlVertical){
                    /** Fetch vertical template layout string */
                    fetchData(templateUrlVertical, verticalTemplateHandler);
                }
                else{
                    console.warn("script url not found for vertical template")
                }
            }
        });
    }
})(window);