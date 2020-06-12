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
    </script>

</c:if>

<script type="text/javascript">

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

    if (unbxdSiteKey && unbxdApiKey) {
        window.addEventListener("load", initUnbxdRecommendations);
    }

</script>

<c:choose>
    <c:when test="${pageType == 'PRODUCT'}">

        <div id="product_recommendations1"></div>

        <script type="text/javascript">
            function getRecommendations() {
                window.setTimeout(function () {
                    _unbxd_getRecommendations({
                        widgets: {
                            widget1: {
                                name: "product_recommendations1"
                            }
                        },
                        userInfo: {
                            userId: Unbxd.getUserId(),
                            siteKey: unbxdSiteKey,
                            apiKey: unbxdApiKey
                        },
                        pageInfo: {
                            pageType: 'PRODUCT',
                            <%--productIds: [getPid("${product.code}")]--%>
                            //productIds: ['${fn:escapeXml(unbxdCatalog.id)}/${fn:escapeXml(unbxdCatalog.version)}/${product.code}']
                            productIds: ['${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${product.code}']
                        },
                        itemClickHandler: function (product) {
                            //do what you want to do with product that has been clicked here
                            console.log(JSON.stringify(product));
                            console.log(product.url);
                            window.location = 'https://34.226.87.236:9002/yacceleratorstorefront/en' + product.url;
                        },
                        dataParser: function (templateData) {
                            // modify the data received from recommendation API in case required.
                            for (i = 0; i < templateData.recommendations.length; i++) {
                                templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                            }
                            return templateData;
                        }
                    });
                }, 3000);
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:when>

    <c:otherwise>

        <div id="home_recommendations1"></div>

        <script type="text/javascript">
            function getRecommendations() {
                window.setTimeout(function () {
                    _unbxd_getRecommendations({
                        widgets: {
                            widget1: {
                                name: "home_recommendations1"
                            }
                        },
                        userInfo: {
                            userId: Unbxd.getUserId(),
                            siteKey: unbxdSiteKey,
                            apiKey: unbxdApiKey
                        },
                        pageInfo: {
                            pageType: 'HOME'
                        },
                        itemClickHandler: function (product) {
                            //do what you want to do with product that has been clicked here
                            alert(JSON.stringify(product));
                        },
                        dataParser: function (templateData) {
                            // modify the data received from recommendation API in case required.
                            for (i = 0; i < templateData.recommendations.length; i++) {
                                templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                            }
                            return templateData;
                        }
                    });
                }, 3000);
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:otherwise>
</c:choose>
