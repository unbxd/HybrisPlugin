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
        console.log("Generic page type", '${pageType}');
        console.log("Recommendation widget type : ", '${UnbxdPageType}');
        console.log("CMS Page Id : ", '${cmsPage.uid}');
        console.log("CMS Page Name : ", '${cmsPage.name}');

        var scheme = '${request.secure}' ? 'https:' : 'http:';
        var host = '${header['host']}';
        var contextPath = '${encodedContextPath}';
        var baseUrl = scheme + host + contextPath;

        var categoryPathfull = '${searchPageData.unbxdCategoryPath}';
        if (categoryPathfull) {
            var categoryarray = categoryPathfull.split(">", 4);
            console.log("Category 1", categoryarray[0]);
            console.log("Category 2", categoryarray[1]);
            console.log("Category 3", categoryarray[2]);
            console.log("Category 4", categoryarray[3]);
        }

        var UnbxdRecommendationJSUrl = 'https://d3m8huu8gvuyn3.cloudfront.net/rex_template_content/unbxd_rex_template_sdk.js';

        function initUnbxdRecommendations() {
            (function () {
                var ubx = document.createElement('script');
                ubx.type = 'text/javascript';
                ubx.async = false;
                ubx.src = UnbxdRecommendationJSUrl;
                (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(ubx);
            })();
        }

        window.addEventListener("load", initUnbxdRecommendations);

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
            <c:choose>

                <c:when test="${widgetType == 'WIDGET1'}">
                    <div id="product_recommendations1"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "product_recommendations1"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET2'}">
                    <div id="product_recommendations2"></div>
                    <script type="text/javascript">
                        widgets['widget2'] = {name: "product_recommendations2"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET3'}">
                    <div id="product_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget3'] = {name: "product_recommendations3"};
                    </script>
                </c:when>
            </c:choose>

        </c:when>

        <c:when test="${UnbxdPageType == 'CATEGORY' && pageType == 'CATEGORY'}">

            <script type="text/javascript">
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
            <c:choose>
                <c:when test="${widgetType == 'WIDGET1'}">
                    <div id="category_recommendations1"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "category_recommendations1"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET2'}">
                    <div id="category_recommendations2"></div>
                    <script type="text/javascript">
                        widgets['widget2'] = {name: "category_recommendations2"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET3'}">
                    <div id="category_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget3'] = {name: "category_recommendations3"};
                    </script>
                </c:when>
            </c:choose>
        </c:when>

        <c:when test="${UnbxdPageType == 'HOME' && cmsPage.uid == 'homepage'}">
            <script type="text/javascript">
                widgets = window.widgets || {};
                var pageInfo = {
                    pageType: 'HOME'
                };
            </script>
            <c:choose>

                <c:when test="${widgetType == 'WIDGET1'}">
                    <div id="home_recommendations1"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "home_recommendations1"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET2'}">
                    <div id="home_recommendations2"></div>
                    <script type="text/javascript">
                        widgets['widget2'] = {name: "home_recommendations2"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET3'}">
                    <div id="home_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget3'] = {name: "home_recommendations3"};
                    </script>
                </c:when>
            </c:choose>
        </c:when>

        <c:when test="${UnbxdPageType == 'CART' && pageType == 'CART'}">

            <script type="text/javascript">
                widgets = window.widgets || {};
                console.log('Products in Cart : ', '${productcodes}');
                var productcodes = new Array();
                <c:forEach items="${productcodes}" var="productcode">
                productcodes.push('${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${productcode}');
                </c:forEach>

                var pageInfo = {
                    pageType: 'CART',
                    productIds: productcodes
                };
            </script>
            <c:choose>
                <c:when test="${widgetType == 'WIDGET1'}">
                    <div id="cart_recommendations1"></div>
                    <script type="text/javascript">
                        widgets['widget1'] = {name: "cart_recommendations1"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET2'}">
                    <div id="cart_recommendations2"></div>
                    <script type="text/javascript">
                        widgets['widget2'] = {name: "cart_recommendations2"};
                    </script>
                </c:when>
                <c:when test="${widgetType == 'WIDGET3'}">
                    <div id="cart_recommendations3"></div>
                    <script type="text/javascript">
                        widgets['widget3'] = {name: "cart_recommendations3"};
                    </script>
                </c:when>
            </c:choose>
        </c:when>
    </c:choose>

</c:if>