<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<c:if test="${not empty unbxdConfig}">
<script type="text/javascript">
UnbxdSiteName="${ycommerce:encodeJavaScript(unbxdConfig.siteName)}";
UnbxdApiKey="${ycommerce:encodeJavaScript(unbxdConfig.apiKey)}";
UnbxdHybris = window.UnbxdHybris || {};
UnbxdHybris.pageType="${pageType}";
</script>
<c:if test="${unbxdConfig.autosuggestEnabled}">
<script type="text/javascript">
UnbxdHybris.autosuggestConfig = ${unbxdConfig.autosuggestConfig};
UnbxdHybris.searchInputSelector = "${unbxdConfig.searchInputSelector}" || '#js-site-search-input';
</script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.1.2/handlebars.min.js"></script>
<script type="text/javascript" src="https://libraries.unbxdapi.com/unbxdAutosuggest_v2.js"></script>
</c:if>

<c:if test="${unbxdConfig.searchEnabled and pageType eq 'PRODUCTSEARCH'}">
<script type="text/javascript">
UnbxdHybris.listingConfig = ${unbxdConfig.searchConfig};
</script>
<script type="text/javascript" src="https://libraries.unbxdapi.com/search-sdk/v0.0.6/vanillaSearch.js"></script>
</c:if>

<c:if test="${unbxdConfig.categoryEnabled and pageType eq 'CATEGORY'}">
<script type="text/javascript">
UnbxdHybris.listingConfig = ${unbxdConfig.categoryConfig};
<c:if test="${not empty breadcrumbs}">
        <c:set var="categories" value="" />
        <c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="loopIndex">
        <c:if test="${!loopIndex.first}">
            <c:set var="categories">${categories}></c:set>
        </c:if>
        <c:set var="categories">${categories}${fn:escapeXml(breadcrumb.name)}
        </c:set>
        </c:forEach>
</c:if>
UnbxdAnalyticsConf = window.UnbxdAnalyticsConf || {};
UnbxdAnalyticsConf["page"] = "${categories}";
UnbxdAnalyticsConf["page_type"] = "CATEGORY_PATH";
</script>
<script type="text/javascript" src="https://libraries.unbxdapi.com/search-sdk/v0.0.6/vanillaSearch.js"></script>
</c:if>

<c:if test="${unbxdConfig.analyticsEnabled}">
<script type="text/javascript">
UnbxdAnalyticsConf = window.UnbxdAnalyticsConf || {};
<c:if test="${pageType eq 'PRODUCT' and not empty product}">
UnbxdAnalyticsConf.pid = "${product.code}";
</c:if>
<c:if test="${pageType == 'ORDERCONFIRMATION'}">
            UnbxdAnalyticsConf.order = [];
            <c:forEach items="${orderData.entries}" var="entry">
                        UnbxdAnalyticsConf.order.push({
                            "pid": getPid("${entry.product.code}"),
                            "qty": '${entry.quantity}',
                            "price": '${entry.product.price.value}'
                        });
            </c:forEach>
           

</c:if>

</script>
<script type="text/javascript" src="//d21gpk1vhmjuf5.cloudfront.net/unbxdAnalytics.js"></script>
</c:if>

</c:if>