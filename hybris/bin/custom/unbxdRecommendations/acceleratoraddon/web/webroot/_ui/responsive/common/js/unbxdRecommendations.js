if (unbxdSiteKey && unbxdApiKey) {

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

    function getRecommendations() {
        var x = 0;
        var intervalID = window.setInterval(function () {
            if (typeof _unbxd_getRecommendations === "function") {
                // safe to use the function
                _unbxd_getRecommendations({
                    widgets: widgets,
                    userInfo: {
                        userId: Unbxd.getUserId(),
                        siteKey: unbxdSiteKey,
                        apiKey: unbxdApiKey
                    },
                    pageInfo: pageInfo,
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
                window.clearInterval(intervalID);
            }
            if (++x === 10) {
                window.clearInterval(intervalID);
            }
        }, 1000);
    }

    window.addEventListener("load", getRecommendations);
}
