<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<spring:htmlEscape defaultHtmlEscape="true"/>

<c:if test="${not empty unbxdSiteKey && not empty unbxdApiKey}">

    <script type="text/javascript">

        var unbxdSiteKey = '${ycommerce:encodeJavaScript(unbxdSiteKey)}';
        var unbxdApiKey = '${ycommerce:encodeJavaScript(unbxdApiKey)}';
        var scheme = '${request.secure}' ? 'https:' : 'http:';
        var host = '${header['host']}';
        var contextPath = '${encodedContextPath}';
        var baseUrl = scheme + host + contextPath;

    </script>

    <c:choose>
        <c:when test="${UnbxdPageType == 'PRODUCT' && pageType == 'PRODUCT'}">

            <script type="text/javascript">
                widgets = window.widgets || {};
                var pageInfo = {
                    pageType: 'PRODUCT',
                    productIds: ['${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${product.code}']
                };
            </script>
        </c:when>

        <c:when test="${UnbxdPageType == 'CATEGORY' && pageType == 'CATEGORY'}">

            <script type="text/javascript">

                var categoryPathfull = '${searchPageData.unbxdCategoryPath}';
                if (categoryPathfull) {
                    var categoryarray = categoryPathfull.split(">", 4);
                }

                widgets = window.widgets || {};
                if (categoryarray[0] === 'brands') {
                    var pageInfo = {
                        pageType: 'BRAND',
                        brand: categoryarray[1]
                    }
                } else {
                    var pageInfo = {
                        pageType: 'CATEGORY',
                        catlevel1Name: categoryarray[0],
                        catlevel2Name: categoryarray[1],
                        catlevel3Name: categoryarray[2]
                    };
                }

            </script>
        </c:when>

        <c:when test="${UnbxdPageType == 'HOME' && cmsPage.uid == 'homepage'}">
            <script type="text/javascript">
                widgets = window.widgets || {};
                var pageInfo = {
                    pageType: 'HOME'
                };
            </script>
        </c:when>

        <c:when test="${UnbxdPageType == 'CART' && pageType == 'CART'}">

            <script type="text/javascript">
                widgets = window.widgets || {};
                var productcodes = new Array();
                <c:forEach items="${productcodes}" var="productcode">
                productcodes.push('${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${productcode}');
                </c:forEach>

                var pageInfo = {
                    pageType: 'CART',
                    productIds: productcodes
                };
            </script>
        </c:when>
    </c:choose>
    
    	<c:choose>

                <c:when test="${widgetType == 'WIDGET1'}">
                    <div id="unbxd_recommendations1"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "unbxd_recommendations1"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET2'}">
                    <div id="unbxd_recommendations2"></div>
                    <script type="text/javascript">
                        widgets['widget2'] = {name: "unbxd_recommendations2"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET3'}">
                    <div id="unbxd_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget3'] = {name: "unbxd_recommendations3"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'ALLWIDGETS'}">
                    <div id="unbxd_recommendations1"></div>
                    <div id="unbxd_recommendations2"></div>
                    <div id="unbxd_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "unbxd_recommendations1"};
                        widgets['widget2'] = {name: "unbxd_recommendations2"};
						widgets['widget3'] = {name: "unbxd_recommendations3"};

                    </script>
                </c:when>
            </c:choose>

</c:if>
