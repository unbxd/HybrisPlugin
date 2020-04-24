<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<script type="text/javascript">
    var initUnbxdAnalyticsNow = false;
    var unbxdAnalyticsEnabled = false;
</script>
<c:if test="${not empty unbxdAnalyticsSiteKey}">
    <script type="text/javascript">
        initUnbxdAnalyticsNow = true;
        unbxdAnalyticsEnabled = true;
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
        /*Unbxd.track('browse_impression', {page : "${fn:escapeXml(categoryName)}", page_type: "CATEGORY_PATH", pids_list : []})
		//Unbxd.track('browse_impression', {page : "${fn:escapeXml(categoryName)}", page_type: "CATEGORY_PATH", pids_list : []})
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
		Unbxd.track('search_impression', {query : '${searchPageData.freeTextSearch}', pids_list : []});
		Unbxd.track('facets', {query : <query>, facets : facets});*/

        </c:when>
        <c:when test="${pageType == 'ORDERCONFIRMATION'}">
        <c:forEach items="${orderData.entries}" var="entry">
        Unbxd.track ( "order" ,{ "pid" : getPid("${entry.product.code}")} , "qty" : '${entry.quantity}' , "price" : '${entry.product.price.value}' });
        //Unbxd.track ( "order" , { "pid" : getPid("${entry.product.code}")} , "variantId" : 'VID' , "qty" : '${entry.quantity}' , "price" : '${entry.product.price.value}' })
        </c:forEach>
        </c:when>
        </c:choose>

        function initUnbxdAnalytics() {
            ( function () {
                var ubx = document.createElement ( 'script' ); ubx.type = 'text/javascript' ; ubx.async = true ;
                ubx.src = UnbxdSiteUrl;
                ( document . getElementsByTagName ( 'head' )[ 0 ] || document . getElementsByTagName ( 'body' )[ 0 ]). appendChild ( ubx );
                // added search tracking
                // NOTE: in case, if magento default functionality related to search has been changed, change the selector names

            })();
        }

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

        function searchHitTrack() {
            var searchInput = document.getElementById("js-site-search-input"),
                searchHitButton = document.getElementsByClassName("js_search_button"),
                unbxdAttributeName = 'unbxdattr';
            if (searchInput) {
                searchInput.setAttribute(unbxdAttributeName, 'sq');
            }
            if (searchHitButton && searchHitButton.length) {
                searchHitButton[0].setAttribute(unbxdAttributeName, 'sq_bt');
            }
        }
        window.addEventListener("load", searchHitTrack);
    </script>
</c:if>

<c:if test="${not empty unbxdAutoSuggestSiteKey && not empty unbxdAutoSuggestApiKey}">
    <script type="text/javascript">
        var unbxdAutoSuggestSiteKey = '${ycommerce:encodeJavaScript(unbxdAutoSuggestSiteKey)}';
        var unbxdAutoSuggestApiKey = '${ycommerce:encodeJavaScript(unbxdAutoSuggestApiKey)}';
        var unbxdAutoSuggestSearchInputId = '${ycommerce:encodeJavaScript(unbxdAutoSuggestSearchInputId)}';
        <%--var catalogVersion = '${ycommerce:encodeJavaScript(unbxdCatalog.version)}';--%>
        <%--var catalogId = '${ycommerce:encodeJavaScript(unbxdCatalog.id)}';--%>
    </script>
    <script type="text/javascript">
        if(true) { //initUnbxdAnalyticsNow) {
            window.addEventListener("load", initUnbxdAnalytics);
        }
    </script>
    <c:choose>
        <c:when  test="${pageType == 'PRODUCTSEARCH'}">
            <style>.main__inner-wrapper { display:none; }</style>
            <script type="text/javascript">
                initUnbxdAnalyticsNow = false;
                function triggerSearch() {
                    (function () {
                        var ubx = document.createElement('script');
                        ubx.type = 'text/javascript';
                        ubx.async = true;
                        ubx.src = '/yacceleratorstorefront/_ui/addons/unbxdanalytics/shared/common/js/unbxd-trigger-search.js';
                        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(ubx);
                        // added search tracking
                        // NOTE: in case, if magento default functionality related to search has been changed, change the selector names

                    })();
                }
                window.addEventListener("load", triggerSearch);
                var CSRFToken = '${ycommerce:encodeJavaScript(CSRFToken.token)}';
            </script>
        </c:when>
        <c:when  test="${pageType == 'CATEGORY'}">
            <style>.main__inner-wrapper { display:none; }</style>
            <script type="text/javascript">
                initUnbxdAnalyticsNow = false;
                function triggerCategoryBrowse() {
                    (function () {
                        var ubx = document.createElement('script');
                        ubx.type = 'text/javascript';
                        ubx.async = true;
                        ubx.src = '/yacceleratorstorefront/_ui/addons/unbxdanalytics/shared/common/js/unbxd-trigger-category-browse.js';
                        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(ubx);
                        // added search tracking
                        // NOTE: in case, if magento default functionality related to search has been changed, change the selector names

                    })();
                }
                window.addEventListener("load", triggerCategoryBrowse);
                var CSRFToken = '${ycommerce:encodeJavaScript(CSRFToken.token)}';
                var unbxdCategoryId = '${ycommerce:encodeJavaScript(searchPageData.unbxdCategoryPath)}';
                var categoryName = '${ycommerce:encodeJavaScript(categoryName)}';
            </script>
        </c:when>
    </c:choose>
</c:if>

