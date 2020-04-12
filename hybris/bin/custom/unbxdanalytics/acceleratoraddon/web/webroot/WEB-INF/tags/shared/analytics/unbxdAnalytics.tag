<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${not empty unbxdAnalyticsSiteKey}">
<script type="text/javascript">

/* * * UNBXD ANALYTICS CONFIGURATION * * */
var UnbxdSiteName = "${ycommerce:encodeJavaScript(unbxdAnalyticsSiteKey)}" ; // Replace the value with your Site Key. /* * * DON'T EDIT BELOW THIS LINE * * */
var UnbxdSiteUrl = '//d21gpk1vhmjuf5.cloudfront.net/unbxdAnalytics.js' ;

/* * * UNBXD ANALYTICS * * */

UnbxdAnalyticsConf = window.UnbxdAnalyticsConf || {};

<c:choose>
	<c:when test="${pageType == 'PRODUCT'}">
		UnbxdAnalyticsConf [ 'pid' ] = getPid("${product.code}");

		window.onbeforeunload = sendDwellTime;
		var start = new Date();
    	function sendDwellTime(){
    		var end = new Date();
    		if(window.Unbxd) Unbxd.track('dwellTime', {pid : getPid("${product.code}"), dwellTime : ""+(end - start)})
    	}
    	/*Unbxd.track("product_view", {"pid": getPid("${product.code}")});*/
	</c:when>
	<c:when test="${pageType == 'CATEGORY'}">
		<c:if test="${not empty breadcrumbs}">
			<c:set var="categories" value="" />
			<c:forEach items="${breadcrumbs}" var="breadcrumb">
				<c:set var="categories">${categories} > '${fn:escapeXml(breadcrumb.name)}'</c:set>
			</c:forEach>

		</c:if>
		UnbxdAnalyticsConf["page"] = "${fn:escapeXml(categoryName)}";
		//UnbxdAnalyticsConf["page"] = "${fn:escapeXml(categories)}";
		UnbxdAnalyticsConf["page_type"] = "CATEGORY_PATH";
		/*Unbxd.track('browse_impression', {page : "${fn:escapeXml(categoryName)}", page_type: "CATEGORY_PATH", pids_list : []]})
		//Unbxd.track('browse_impression', {page : "${fn:escapeXml(categoryName)}", page_type: "CATEGORY_PATH", pids_list : ['a001','a002']})
		Unbxd.track('facets', {page : "${fn:escapeXml(categoryName)}", page_type: "CATEGORY_PATH", facets : '<facets>'});
		Unbxd.track('categoryPage', {"page":"", "page_type":"CATEGORY_PATH", "page_name":"${fn:escapeXml(categoryName)}"});*/
	</c:when>
	<c:when  test="${pageType == 'PRODUCTSEARCH'}">
		var facets = {};
		<c:forEach items="${searchPageData.breadcrumbs}" var="breadcrumb">
			facets['${ycommerce:encodeJavaScript(breadcrumb.facetName)}'] = ['${ycommerce:encodeJavaScript(breadcrumb.facetValueName)}'];
		</c:forEach>
		UnbxdAnalyticsConf["query"] = '${searchPageData.freeTextSearch}';
		/*Unbxd.track("search", {"query": '${searchPageData.freeTextSearch}'});
		Unbxd.track('search_impression', {query : '${searchPageData.freeTextSearch}', pids_list : []]});
		Unbxd.track('facets', {query : <query>, facets : facets});*/

	</c:when>
	<c:when test="${pageType == 'ORDERCONFIRMATION'}">
		<c:forEach items="${orderData.entries}" var="entry">
			Unbxd.track ( "order" ,{ "pid" : getPid("${entry.product.code}")} , "qty" : '${entry.quantity}' , "price" : '${entry.product.price.value}' });
			//Unbxd.track ( "order" , { "pid" : getPid("${entry.product.code}")} , "variantId" : 'VID' , "qty" : '${entry.quantity}' , "price" : '${entry.product.price.value}' })
		</c:forEach>
	</c:when>
</c:choose>

( function () {
var ubx = document.createElement ( 'script' ); ubx.type = 'text/javascript' ; ubx.async = true ;
ubx.src = UnbxdSiteUrl;
( document . getElementsByTagName ( 'head' )[ 0 ] || document . getElementsByTagName ( 'body' )[ 0 ]). appendChild ( ubx );
        // added search tracking
        // NOTE: in case, if magento default functionality related to search has been changed, change the selector names

})();

function getPid(productCode) {
    return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
}
function trackAddToCartWithVariant_unbxd(productCode, quantityAdded, variant) {
	Unbxd.track( "addToCart" , { "pid" : getPid(productCode) , "variantId" : variant, "qty" : quantityAdded })
}

function trackAddToCart_unbxd(productCode, quantityAdded) {
	Unbxd.track( "addToCart" , { "pid" : getPid(productCode) , "qty" : quantityAdded })
}

function trackAddToCartWithVariantRequestId_unbxd(productCode, quantityAdded, variant) {
	Unbxd.track( "addToCart" , { "pid" : getPid(productCode) , "variantId" : variant, "qty" : quantityAdded , "requestId" : requestId })
}

function trackAddToCartRequestId_unbxd(productCode, quantityAdded,requestId) {
	Unbxd.track( "addToCart" , { "pid" : getPid(productCode) , "qty" : quantityAdded , "requestId" : requestId })
}

function trackUpdateCart_unbxd(productCode, initialQuantity, newQuantity) {
}

function trackRemoveFromCart_unbxd(productCode, initialQuantity) {
	Unbxd.track ( "cartRemoval" , { "pid" : getPid(productCode) , "qty" : initialQuantity })
}

window.mediator.subscribe('trackAddToCart', function(data) {
	if (data.productCode && data.quantity && data.variant)
	{
		trackAddToCartWithVariant_unbxd(data.productCode, data.initialCartQuantity, data.newCartQuantity);
	}
	if (data.productCode && data.quantity) {
		trackAddToCart_unbxd(data.productCode, data.quantity);
	}
});

window.mediator.subscribe('trackUpdateCart', function(data) {
	if (data.productCode && data.initialCartQuantity && data.newCartQuantity)
	{
		trackUpdateCart_unbxd(data.productCode, data.initialCartQuantity, data.newCartQuantity);
	}
});

window.mediator.subscribe('trackRemoveFromCart', function(data) {
	if (data.productCode && data.initialCartQuantity)
	{
		trackRemoveFromCart_unbxd(data.productCode, data.initialCartQuantity);
	}
});

window.onload = function() {
            var searchInput = document.getElementById("js-site-search-input"),
                searchHitButton = document.getElementsByClassName("js_search_button"),
                unbxdAttributeName = 'unbxdattr';
            if (searchInput) {
                searchInput.setAttribute(unbxdAttributeName, 'sq');
            }
            if (searchHitButton && searchHitButton.length) {
                searchHitButton[0].setAttribute(unbxdAttributeName, 'sq_bt');
            }
        };
</script>
</c:if>

<c:if test="${not empty unbxdAutoSuggestSiteKey && not empty unbxdAutoSuggestApiKey}">
<link rel="stylesheet" href="//d21gpk1vhmjuf5.cloudfront.net/jquery-unbxdautosuggest.css">
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/handlebars.js/2.0.0/handlebars.min.js"></script>
<script type="text/javascript">
var unbxdAutoSuggestSiteKey = '${ycommerce:encodeJavaScript(unbxdAutoSuggestSiteKey)}';
var unbxdAutoSuggestApiKey = '${ycommerce:encodeJavaScript(unbxdAutoSuggestApiKey)}';
unbxdAutoSuggestFunction(jQuery, Handlebars, {"platform" : "io"});
if($("#"+unbxdAutoSuggestSearchInputId)) {
    $("#"+unbxdAutoSuggestSearchInputId).unbxdautocomplete({
        siteName : unbxdAutoSuggestSiteKey //your site key which can be found on dashboard
        ,APIKey : unbxdAutoSuggestApiKey //your api key which is mailed to during account creation or can be found on account section on the dashboard
        ,minChars : 2
        ,maxSuggestions: 10
        ,delay : 100
        ,loadingClass : 'unbxd-as-loading'
        ,mainWidth : 0
        ,sideWidth : 180
        ,zIndex : 0
        ,position : 'absolute'
        ,template : "1column"
        ,mainTpl: ['inFields', 'keywordSuggestions', 'topQueries', 'popularProducts']
        ,sideTpl: []
        ,sideContentOn : "right"
        ,showCarts : false
        ,cartType : "separate"
        ,onSimpleEnter : function(){
            console.log("Simple enter :: do a form submit")
            //this.input.form.submit();
        }
        ,onItemSelect : function(data,original){
                console.log("onItemSelect",arguments);
        }
        ,onCartClick : function(data,original){
                console.log("addtocart", arguments);
                return true;
        }
        ,inFields:{
                count: 2
                ,fields:{
                        'brand':3
                        ,'category':3
                        ,'color':3
                }
                ,header: ''
                ,tpl: ''
        }
        ,topQueries:{
                count: 2
                ,header: ''
                ,tpl: ''
        }
        ,keywordSuggestions:{
                count: 2
                ,header: ''
                ,tpl: ''
        }
        ,suggestionsHeader: ''
        ,popularProducts:{
                count: 2
                ,price: true
                ,priceFunctionOrKey : "price"
                ,image: true
                ,imageUrlOrFunction: "imageUrl"
                ,currency : "$"
                ,header: ''
                ,tpl: ''
        }
        ,filtered : false
        ,platform: "io"
    });

    window.searchobj = new Unbxd.setSearch({
                siteName: unbxdAutoSuggestSiteKey,
                APIKey: unbxdAutoSuggestApiKey,
                type: 'search',
                getCategoryId: '',
                inputSelector: '#'+unbxdAutoSuggestSearchInputId,
                searchButtonSelector: '#js_search_button',
                spellCheck: '#did_you_mean',
                spellCheckTemp: 'Did you mean : {{suggestion}} ?',
                searchQueryDisplay: '#search_title',
                searchQueryDisplayTemp: 'Showing results for {{query}} - {{start}}-{{end}} of {{numberOfProducts}} Results',
                pageSize: 24,
                noEncoding: true,
                platform:"io",
                fields : [ "vPriceValue", "priceValue", "uniqueId", "name", "brandName", "url", "img-300Wx300H","allCategories","gender","categoryName"],
                searchResultSetTemp: {
                    "grid": ['{{#products}}<li><a href="{{url}}" id="pdt-{{uniqueId}}" class="result-item" unbxdParam_sku="{{uniqueId}}" unbxdParam_pRank="{{unbxdprank}}" unbxdAttr="product">', '<div class="result-image-container">', '<span class="result-image-horizontal-holder">', '<img src="https://34.226.87.236:9002/yacceleratorstorefront{{{[img-300Wx300H]}}}" alt="{{{name}}}">', '</span>', '</div>', '<div class="result-brand">{{{brandName}}}</div>', '<div class="result-title">{{{name}}}</div>', '<div class="result-price">', '${{priceValue}}', '</div>', '</a></li>{{/products}}'].join(''),

                    "list": ['{{#products}}<li><a href="{{url}}" id="pdt-{{uniqueId}}" class="result-item" unbxdParam_sku="{{uniqueId}}" unbxdParam_pRank="{{unbxdprank}}" unbxdAttr="product">', '<div class="result-image-container">', '<span class="result-image-horizontal-holder">', '<img src="https://34.226.87.236:9002/yacceleratorstorefront{{{[img-300Wx300H]}}}" alt="{{{name}}}">', '</span>', '</div>', '<div class="result-brand">{{{brandName}}}</div>', '<div class="result-title">{{{name}}}</div>', '<div class="result-price">', '${{priceValue}}', '</div>', '</a></li>{{/products}}'].join('')
                },
                searchResultContainer: '.search-grid-page-result-grid-component',
                isClickNScroll: false,
                clickNScrollSelector: '',
                isAutoScroll: true,
                isPagination: false,
                paginationContainerSelector: '.result-pagination',
                viewTypes: ['grid'],
                viewTypeContainerSelector: '',
                facetTemp: ['<div class="facet-block">', '<div class="facet-title">PRICE</div>', '<div class="facet-values">', '<div id="slider-range-max">', '</div>', '<div id="amount" class="clearfix"><div class="lt"></div><div class="rt"></div></div>', '</div>', '</div>', '{{#facets}}', '<div class="facet-block">', '<div class="facet-title">{{name}}</div>', '<div class="facet-values">', '<ul>', '{{#selected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<input type="checkbox" checked class="filter-checkbox" unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<label for="{{../facet_name}}_{{value}}">', '{{prepareFacetValue value}} ({{count}})', '</label>', '</li>', '{{/selected}}', '{{#unselected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<input type="checkbox" class="filter-checkbox" unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<label for="{{../facet_name}}_{{value}}">', '{{prepareFacetValue value}} ({{count}})', '</label>', '</li>', '{{/unselected}}', '</ul>', '</div>', '</div>{{/facets}}', '{{#rangefacets}}<div class="facet-block">', '<div class="facet-title">{{name}}</div>', '<div class="facet-values">', '<ul>', '{{#selected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}" unbxdParam_facetType="{{../type}}">', '<input type="checkbox" checked class="filter-checkbox" unbxdParam_facetName="{{../facet_name}}" ', ' unbxdParam_facetType="{{../type}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<label for="{{../facet_name}}_{{value}}">', '{{prepareFacetValue begin}} - {{prepareFacetValue end}} ({{count}})</label></li>', '{{/selected}}', ' {{#unselected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}" unbxdParam_facetType="{{../type}}">', '<input type="checkbox" class="filter-checkbox" unbxdParam_facetName="{{../facet_name}}" ', ' unbxdParam_facetType="{{../type}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<label for="{{../facet_name}}_{{value}}">', '{{prepareFacetValue begin}} - {{prepareFacetValue end}} ({{count}})</label></li>', '{{/unselected}}', '</ul>', '</div>', '</div>', '{{/rangefacets}}'].join(''),
                facetContainerSelector: "#facets_container",
                facetCheckBoxSelector: "input[type='checkbox']",
                facetElementSelector: "label",
                facetOnSelect: function(el) {},
                facetOnDeselect: function(el) {},
                facetMultiSelect: true,
                selectedFacetTemp: [
                    'Selected:', '{{#each filters}}', '{{#each this}}', '<div class="selected-facet clearfix">', '<div class="selected-facet-name lt">{{{prepareFacetValue @key}}}</div>', '<div class="selected-facet-delete rt" unbxdParam_facetName="{{this}}" unbxdParam_facetValue="{{@key}}">&times;</div>', '</div>', '{{/each}}', '{{/each}}', '{{#each ranges}}', '{{#each this}}', '<div class="selected-facet clearfix">', '<div class="selected-facet-name lt">{{{prepareFacetValue @key}}}</div>', '<div class="selected-facet-delete rt" unbxdParam_facetName="{{this}}" unbxdParam_facetValue="{{@key}}">&times;</div>', '</div>', '{{/each}}', '{{/each}}'
                ].join(''),
                selectedFacetContainerSelector: "#selected_facets",
                clearSelectedFacetsSelector: "#clear_all_selected_facets",
                removeSelectedFacetSelector: ".selected-facet-delete",
                selectedFacetHolderSelector: "",
                loaderSelector: "" //".result-loader"
                    ,
                onFacetLoad: function() {
                    console.log('onFacetLoad', arguments, this);
                },
                sanitizeQueryString: function(q) {
                    return q;
                },
                loaderSelector: ".result-loader",
                getFacetStats: "price",
                processFacetStats: function(obj) {
                    var divs = $("#amount div");

                    jQuery("#slider-range-max").slider({
                        range: !0,
                        animate: !0,
                        min: obj.price.min,
                        max: obj.price.max,
                        values: [obj.price.values.min, obj.price.values.max],
                        create: function() {
                            jQuery("#amount").val("$" + obj.price.values.min + '  -  ' + "$" + obj.price.values.max);
                            divs.eq(0).html("$" + obj.price.values.min);
                            divs.eq(1).html("$" + obj.price.values.max);
                        },
                        slide: function(b, c) {
                            jQuery("#amount").val("$" + c.values[0] + '  -  ' + "$" + c.values[1]);
                            divs.eq(0).html("$" + c.values[0]);
                            divs.eq(1).html("$" + c.values[1]);
                        },
                        change: function(b, c) {
                            searchobj
                                .clearRangeFiltes()
                                .addRangeFilter("price", c.values[0], c.values[1])
                                .setPage(1)
                                .callResults(searchobj.paintResultSet, true);
                        }
                    });
                },
                setDefaultFilters: function() {},
                onIntialResultLoad: function() {
                    console.log('onIntialResultLoad', arguments, this);
                },
                onPageLoad: function() {
                    console.log('onPageLoad', arguments, this);
                },
                deferInitRender: [],
                bannerSelector: '.banner',
                sortContainerSelector: '.result-sort-options',
                pageSizeContainerSelector: '.result-page-size-options'
            });
}
</script>
</c:if>