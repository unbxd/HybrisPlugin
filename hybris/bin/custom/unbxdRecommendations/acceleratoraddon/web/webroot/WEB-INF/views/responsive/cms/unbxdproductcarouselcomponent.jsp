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

        console.log("Recommendation widget type : ", '${pageType}');
        console.log("CMS Page Id : ", '${cmsPage.uid}');
        console.log("CMS Page Name : ", '${cmsPage.name}');

        var scheme = '${request.secure}' ? 'https:' : 'http:';
        var host = '${header['host']}';
        var contextPath = '${encodedContextPath}';
        var baseUrl = scheme+host+contextPath;

        var categoryPathfull = '${searchPageData.unbxdCategoryPath}';
        if(categoryPathfull) {
            var categoryarray = categoryPathfull.split(">", 4);
            console.log("Category 1", categoryarray[0]);
            console.log("Category 2", categoryarray[1]);
            console.log("Category 3", categoryarray[2]);
            console.log("Category 4", categoryarray[3]);
        }

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
    <c:when test="${pageType == 'PRODUCT' && cmsPage.uid == 'productDetails'}">
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
                            window.location = baseUrl + product.url;
                        },
                        dataParser: function (templateData) {
                            // modify the data received from recommendation API in case required.
                            for (i = 0; i < templateData.recommendations.length; i++) {
                                templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                            }
                            return templateData;
                        }
                    });
                }, 1000);
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:when>

    <c:when test="${pageType == 'CATEGORY' && cmsPage.uid == 'productGrid'}">

        <div id="category_recommendations1"></div>

        <script type="text/javascript">
            function getRecommendations() {
                if(categoryarray[0] === 'categories'){
                    window.setTimeout(function () {
                        _unbxd_getRecommendations({
                            widgets: {
                                widget1: {
                                    name: "category_recommendations1"
                                }
                            },
                            userInfo: {
                                userId: Unbxd.getUserId(),
                                siteKey: unbxdSiteKey,
                                apiKey: unbxdApiKey
                            },
                            pageInfo: {
                                pageType: 'CATEGORY',
                                <%--productIds: [getPid("${product.code}")]--%>
                                //productIds: ['${fn:escapeXml(unbxdCatalog.id)}/${fn:escapeXml(unbxdCatalog.version)}/${product.code}']
                                //productIds: ['${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${product.code}']
                                catlevel1Name: categoryarray[0],
                                catlevel2Name: categoryarray[1],
                                catlevel3Name: categoryarray[2]
                            },
                            itemClickHandler: function (product) {
                                //do what you want to do with product that has been clicked here
                                console.log(JSON.stringify(product));
                                console.log(product.url);
                                window.location = baseUrl + product.url;
                            },
                            dataParser: function (templateData) {
                                // modify the data received from recommendation API in case required.
                                for (i = 0; i < templateData.recommendations.length; i++) {
                                    templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                                }
                                return templateData;
                            }
                        });
                    }, 1000);}
                if(categoryarray[0] === 'brands'){
                    window.setTimeout(function () {
                        _unbxd_getRecommendations({
                            widgets: {
                                widget1: {
                                    name: "category_recommendations1"
                                }
                            },
                            userInfo: {
                                userId: Unbxd.getUserId(),
                                siteKey: unbxdSiteKey,
                                apiKey: unbxdApiKey
                            },
                            pageInfo: {
                                pageType: 'BRAND',
                                <%--productIds: [getPid("${product.code}")]--%>
                                //productIds: ['${fn:escapeXml(unbxdCatalog.id)}/${fn:escapeXml(unbxdCatalog.version)}/${product.code}']
                                //productIds: ['${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${product.code}']
                                brand: categoryarray[1]
                            },
                            itemClickHandler: function (product) {
                                //do what you want to do with product that has been clicked here
                                console.log(JSON.stringify(product));
                                console.log(product.url);
                                window.location = baseUrl + product.url;
                            },
                            dataParser: function (templateData) {
                                // modify the data received from recommendation API in case required.
                                for (i = 0; i < templateData.recommendations.length; i++) {
                                    templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                                }
                                return templateData;
                            }
                        });
                    }, 1000);}
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:when>

    <c:when test="${pageType == 'HOME' && cmsPage.uid == 'homepage'}">

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
                            console.log(JSON.stringify(product));
                            console.log(product.url);
                            window.location = baseUrl + product.url;
                        },
                        dataParser: function (templateData) {
                            // modify the data received from recommendation API in case required.
                            for (i = 0; i < templateData.recommendations.length; i++) {
                                templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                            }
                            return templateData;
                        }
                    });
                }, 1000);
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:when>

    <c:when test="${pageType == 'CATEGORY' && cmsPage.uid == 'cartPage'}">

        <div id="cart_recommendations1"></div>

        <script type="text/javascript">
            console.log('Products in Cart : ','${productcodes}');
            var productcodes = new Array();
            <c:forEach items="${productcodes}" var="productcode">
            productcodes.push('${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_${productcode}');
            </c:forEach>

            function getRecommendations() {
                window.setTimeout(function () {
                    _unbxd_getRecommendations({
                        widgets: {
                            widget1: {
                                name: "cart_recommendations1"
                            }
                        },
                        userInfo: {
                            userId: Unbxd.getUserId(),
                            siteKey: unbxdSiteKey,
                            apiKey: unbxdApiKey
                        },
                        pageInfo: {
                            pageType: 'CART',
                            productIds: productcodes
                        },
                        itemClickHandler: function (product) {
                            //do what you want to do with product that has been clicked here
                            console.log(JSON.stringify(product));
                            console.log(product.url);
                            window.location = baseUrl + product.url;
                        },
                        dataParser: function (templateData) {
                            // modify the data received from recommendation API in case required.
                            for (i = 0; i < templateData.recommendations.length; i++) {
                                templateData.recommendations[i]['imageUrl'] = templateData.recommendations[i]['img-300Wx300H'];
                            }
                            return templateData;
                        }
                    });
                }, 1000);
            }

            function getPid(productCode) {
                return '${fn:escapeXml(unbxdCatalog.id)}_${fn:escapeXml(unbxdCatalog.version)}_' + productCode;
            }

            if (unbxdSiteKey && unbxdApiKey) {
                window.addEventListener("load", getRecommendations);
            }
        </script>
    </c:when>
</c:choose>
