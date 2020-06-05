$(document).ready(function(){
if(unbxdAutoSuggestSiteKey && unbxdAutoSuggestApiKey && $("#"+unbxdAutoSuggestSearchInputId)) {
    unbxdAutoSuggestFunction(jQuery, Handlebars, {"platform" : "io"});
    $("#"+unbxdAutoSuggestSearchInputId).unbxdautocomplete({
        siteName : unbxdAutoSuggestSiteKey //your site key which can be found on dashboard
        ,APIKey : unbxdAutoSuggestApiKey //your api key which is mailed to during account creation or can be found on account section on the dashboard
        ,resultsClass: "unbxd-as-wrapper"
        ,suggestionsHeader: "Suggestions"
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
            console.log("Simple enter :: do a form submit");
            if(this.input.value.length) {
                this.input.form.submit();
            }
        }
        ,onItemSelect : function(data,original){
                console.log("onItemSelect",arguments);
            if (data.type === "POPULAR_PRODUCTS" || data.type === "POPULAR_PRODUCTS_FILTERED") {
                window.location = arguments[1].productUrl;
            } else {
                window.location = window.location.origin + '/yacceleratorstorefront/en/search/?text=' + encodeURIComponent(data.value);
            }

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
                        , 'vStyle':3
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
}
});