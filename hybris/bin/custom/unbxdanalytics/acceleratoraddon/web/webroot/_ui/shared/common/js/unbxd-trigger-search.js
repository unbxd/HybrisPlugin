Handlebars.registerHelper('isdefined', function (value) {
    return value !== undefined;
});
$(document).ready(function(){
    if(unbxdAutoSuggestSiteKey && unbxdAutoSuggestApiKey && $("#"+unbxdAutoSuggestSearchInputId)) {
        $(".main__inner-wrapper").html("<div class=\"row\">\n" +
            "        <div class=\"col-xs-3\">\n" +
            "            <div class=\"yCmsContentSlot search-list-page-left-refinements-slot\">\n" +
            "                <div class=\"yCmsComponent search-list-page-left-refinements-component\">\n" +
            "                    <div id=\"selected-product-facet\" class=\"hidden-sm hidden-xs product__facet js-product-facet\">\n" +
            "                    </div>\n" +
            "                    <div id=\"product-facet\" class=\"hidden-sm hidden-xs product__facet js-product-facet\">\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"col-sm-12 col-md-9\">\n" +
            "            <div class=\"yCmsContentSlot search-list-page-right-result-list-slot\">\n" +
            "                <div class=\"yCmsComponent search-list-page-right-result-list-component\">\n" +
            "                    <div class=\"product__list--wrapper\">\n" +
            "                        <div class=\"header-container clearfix display-none\">\n" +
            "                            <div class=\"header row clearfix\">\n" +
            "                                <div class=\"header-search\">\n" +
            "                                    <div class=\"search-input-button-holder clearfix\">\n" +
            "                                        <form method=\"GET\" action=\"\">\n" +
            "                                            <input type=\"text\" class=\"search-input lt\" id=\"search_input\" unbxdattr=\"sq\" name=\"q\" autoComplete=\"off\" placeholder=\"Search...\" />\n" +
            "                                            <button type=\"submit\" class=\"search-button lt\" id=\"search_button\" unbxdattr=\"sq_bt\"></button>\n" +
            "                                        </form>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div  class=\"results\">\n" +
            "                            <h1 id=\"search_title\"></h1>\n" +
            "                        </div>\n" +
            "                        <div class=\"pagination-bar top\">\n" +
            "                            <div class=\"pagination-toolbar\">\n" +
            "                                <div class=\"helper clearfix hidden-md hidden-lg\"></div>\n" +
            "                                <div class=\"sort-refine-bar\">\n" +
            "                                    <div class=\"row\">\n" +
            "                                        <div class=\"col-xs-12 col-sm-2   col-md-2\">\n" +
            "                                            <div class=\"form-group\">\n" +
            "                                                <label class=\"control-label \" for=\"sortForm1\">Sort by:</label>\n" +
            "                                                <form class=\"sortForm\" id=\"sortForm1\" name=\"sortForm1\" method=\"get\" action=\"#\">\n" +
            "\n" +
            "                                                </form>\n" +
            "                                            </div>\n" +
            "                                        </div>\n" +
            "                                        <div class=\"col-xs-12 col-sm-2 col-md-2\">\n" +
            "                                            <div class=\"form-group\">\n" +
            "                                                <label class=\"control-label \" for=\"limitForm1\">Results per page:</label>\n" +
            "                                                <form class=\"limitForm\" id=\"limitForm1\" name=\"limitForm1\" method=\"get\" action=\"#\">\n" +
            "\n" +
            "                                                </form>\n" +
            "                                            </div>\n" +
            "                                        </div>\n" +
            "                                        <div class=\"col-xs-12 col-sm-2 col-md-2\">\n" +
            "                                            <div class=\"search-header-options\">\n" +
            "                                                <div class=\"change-view\">\n" +
            "                                                    <label class=\"control-label \" >View by:</label>\n" +
            "                                                    <div class=\"view-types\">\n" +
            "                                                        <span class=\"list-view\">\n" +
            "                                                            List View\n" +
            "                                                        </span>\n" +
            "                                                        <span class=\"grid-view\">\n" +
            "                                                            Grid View\n" +
            "                                                        </span>\n" +
            "                                                    </div>\n" +
            "                                                </div>\n" +
            "                                            </div>\n" +
            "                                        </div>\n" +
            "\n" +
            "                                        <div class=\"col-xs-12 col-sm-6 col-md-5 pagination-wrap\">\n" +
            "                                            <ul class=\"pagination\">\n" +
            "\n" +
            "                                            </ul>\n" +
            "                                        </div>\n" +
            "\n" +
            "                                        <div class=\"col-xs-12 col-sm-2 col-md-4 hidden-md hidden-lg\">\n" +
            "                                            <button class=\"btn btn-default js-show-facets\" data-select-refinements-title=\"Select Refinements\">\n" +
            "                                                Refine</button>\n" +
            "                                        </div>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                            <div class=\"row\">\n" +
            "                                <div class=\"col-xs-12\">\n" +
            "                                    <div class=\"pagination-bar-results\"></div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <ul class=\"product__listing product__list\"></ul>\n" +
            "                        <div id=\"addToCartTitle\" class=\"display-none\">\n" +
            "                            <div class=\"add-to-cart-header\">\n" +
            "                                <div class=\"headline\">\n" +
            "                                    <span class=\"headline-text\">Added to Your Shopping Cart</span>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"pagination-bar bottom\">\n" +
            "                            <div class=\"pagination-toolbar\">\n" +
            "                                <div class=\"helper clearfix hidden-md hidden-lg\"></div>\n" +
            "                                <div class=\"sort-refine-bar\">\n" +
            "                                    <div class=\"row\">\n" +
            "                                        <div class=\"col-xs-12 col-sm-4 col-md-4\">\n" +
            "                                            <div class=\"form-group\">\n" +
            "                                                <label class=\"control-label \" for=\"sortForm2\">Sort by:</label>\n" +
            "                                                <form  class=\"sortForm\" id=\"sortForm2\" name=\"sortForm2\" method=\"get\" action=\"#\">\n" +
            "\n" +
            "                                                </form>\n" +
            "                                            </div>\n" +
            "                                            <div class=\"form-group\">\n" +
            "                                                <label class=\"control-label \" for=\"limitForm2\">Results per page:</label>\n" +
            "                                                <form class=\"limitForm\" id=\"limitForm2\" name=\"limitForm2\" method=\"get\" action=\"#\">\n" +
            "\n" +
            "                                                </form>\n" +
            "                                            </div>\n" +
            "                                            <div class=\"search-header-options\">\n" +
            "                                                <div class=\"change-view\">\n" +
            "                                                    <label class=\"control-label \" >View by:</label>\n" +
            "                                                    <div class=\"view-types\">\n" +
            "                                                        <span class=\"list-view\">\n" +
            "                                                            List View\n" +
            "                                                        </span>\n" +
            "                                                        <span class=\"grid-view\">\n" +
            "                                                            Grid View\n" +
            "                                                        </span>\n" +
            "                                                    </div>\n" +
            "                                                </div>\n" +
            "                                            </div>\n" +
            "                                        </div>\n" +
            "\n" +
            "                                        <div class=\"col-xs-12 col-sm-6 col-md-5 pagination-wrap\">\n" +
            "                                            <ul class=\"pagination\">\n" +
            "\n" +
            "                                            </ul>\n" +
            "                                        </div>\n" +
            "\n" +
            "                                        <div class=\"col-xs-12 col-sm-2 col-md-4 hidden-md hidden-lg\">\n" +
            "                                            <button class=\"btn btn-default js-show-facets\" data-select-refinements-title=\"Select Refinements\">\n" +
            "                                                Refine</button>\n" +
            "                                        </div>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                            <div class=\"row\">\n" +
            "                                <div class=\"col-xs-12\">\n" +
            "                                    <div class=\"pagination-bar-results\"></div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>").hide();
        window.searchobj = new window.Unbxd.setSearch({
            siteName: unbxdAutoSuggestSiteKey,
            APIKey: unbxdAutoSuggestApiKey,
            type: 'search',
            getCategoryId: '',
            inputSelector: '#'+unbxdAutoSuggestSearchInputId,
            searchButtonSelector: '#search_button',
            spellCheck: '.pagination-bar-results',
            spellCheckTemp: '{{numberOfProducts}} Products found',
            searchQueryDisplay: '#search_title,.pagination-bar-results',
            searchQueryDisplayTemp: 'You Searched for "{{query}}" - {{numberOfProducts}} Products found',
            pageSize: 24,
            noEncoding: true,
            facetMultilevel: true,
            facetMultilevelName: 'CATEGORY',
            isSwatches: true,
            swatchesSelector: ".swatch-box",
            variants: true,
            variantsCount: 10,
            searchQueryParam: "text",
            searchResultSetTemp: {
                "grid": ['{{#each (productVariant products)}}<li class="product-item"><a href="/yacceleratorstorefront{{url}}" title="{{{name}}}" id="{{sku}}" class="thumb" unbxdParam_sku="{{uniqueId}}" unbxdParam_pRank="{{unbxdprank}}" unbxdAttr="product">',
                    '<img id="img-{{uniqueId}}" src="{{{imageUrl.[0]}}}" alt="{{{name}}}" title="{{{name}}}"></a>',
                    '<div class="details">',
                    '<a class="name" href="/yacceleratorstorefront{{url}}">{{name}}</a>',
                    '{{#isSwatches}}',
                    '<div class="swatch-container" style="white-space: initial;">',
                    '{{#each variants}}',
                    '<div class="swatch-box" unbxdparam_sku="{{../uniqueId}}" unbxdparam_swatchImage="{{getSwatchImage swatch_click_image}}" style="width:auto;    display: inline-block;margin:10px;margin-bottom:10px;background-color:#5c656a;vertical-align: middle;color: white;padding: 5px;background-image:url(\'{{swatch_background_image}}\'),background-color:url(\'{{swatch_background_color}}\');">',
                    '{{vStyle}}</div>',
                    '{{/each}}',
                    '{{/isSwatches}}',
                    '</div>',
                    '<div class="price">',
                    '{{#if (isdefined priceValue) }}',
                    '&pound;{{priceValue}}',
                    '{{else}}',
                    '&pound;0.00',
                    '{{/if}}',
                    '</div>',
                    '<div class="addtocart">',
                    '<div class="actions-container-for-SearchResultsGrid pickup-in-store-available"><div class="SearchResultsGrid-ListPickUpInStoreAction" data-index="1">',
                    '<button class="btn btn-default btn-block js-pickup-in-store-button glyphicon glyphicon-map-marker" id="product_{{code}}" type="button submit" data-productcart="{{#if (isdefined priceValue) }}&pound;{{priceValue}}{{else}}&pound;0.00{{/if}}" data-productcart-variants="{}" data-img-html="<img src=&quot;{{{imageUrl.[0]}}}&quot;/>" data-productname-html="{{{name}}}"  data-cartpage="false" data-entrynumber="{{@index}}" data-actionurl="/yacceleratorstorefront{{url}}" data-value="1"></button>',
                    '</div>',
                    '<div class="SearchResultsGrid-ListAddToCartAction" data-index="2">',
                    '<form id="addToCartForm{{code}}{{@index}}" class="add_to_cart_form" action="/yacceleratorstorefront/en/cart/add" method="post">',
                    '<input type="hidden" name="productCodePost" value="{{code}}">',
                    '<input type="hidden" name="productNamePost" value="{{{name}}}">',
                    '<input type="hidden" name="productPostPrice" value="{{#if (isdefined priceValue) }}{{priceValue}}{{else}}0.00{{/if}}">',
                    '<button type="submit" class="btn btn-primary btn-block glyphicon glyphicon-shopping-cart js-enable-btn"></button>',
                    '<div>',
                    '<input type="hidden" name="CSRFToken" value="82f83bca-d8a3-42ee-8151-6ad25df65c23">',
                    '</div></form>',
                    '</div>',
                    '<div class="SearchResultsGrid-ListOrderFormAction" data-index="3"></div>',
                    '</li>{{/each}}'].join(''),
                "list": ['{{#products}}<li class="product__list--item">',
                    '<a href="/yacceleratorstorefront{{url}}" id="{sku}}" class="product__list--thumb" unbxdParam_sku="{{uniqueId}}" unbxdParam_pRank="{{unbxdprank}}" unbxdAttr="product">',
                    '<img src="{{{imageUrl.[0]}}}" alt="{{{name}}} title="{{{name}}}">',
                    '</a>',
                    '<a href="/yacceleratorstorefront{{url}}" id="{sku}}" class="product__list--name"  unbxdParam_sku="{{uniqueId}}" unbxdParam_pRank="{{unbxdprank}}" unbxdAttr="product">', '{{{name}}}', '</a>',
                    '<div class="product__list--price-panel"><div class="product__listing--price">', '{{#if (isdefined priceValue) }}', '&pound;{{priceValue}}', '{{else}}', '&pound;0.00', '{{/if}}', '</div></div>',
                    '<div class="product__listing--description">', '{{{summary}}}', '</div>',
                    '<div class="addtocart">\n' + '<div id="actions-container-for-SearchResultsList" class="row">',
                    '<div class="SearchResultsList-ListPickUpInStoreAction" data-index="1">',
                    '<button class="btn btn-default btn-block js-pickup-in-store-button glyphicon glyphicon-map-marker" id="product_{{code}}" type="button submit" data-productcart="{{#if (isdefined priceValue) }}&pound;{{priceValue}}{{else}}&pound;0.00{{/if}}" data-productcart-variants="{}" data-img-html="<img src=&quot;{{{imageUrl.[0]}}}&quot;/>" data-productname-html="{{{name}}}"  data-cartpage="false" data-entrynumber="{{@index}}" data-actionurl="/yacceleratorstorefront{{url}}" data-value="1"></button>',
                    '</div>',
                    '<div class="SearchResultsList-ListAddToCartAction" data-index="2">',
                    '<form id="addToCartForm{{code}}{{@index}}" class="add_to_cart_form" action="/yacceleratorstorefront/en/cart/add" method="post">',
                    '<input type="hidden" name="productCodePost" value="{{code}}">',
                    '<input type="hidden" name="productNamePost" value="{{{name}}}">',
                    '<input type="hidden" name="productPostPrice" value="{{#if (isdefined priceValue) }}{{priceValue}}{{else}}0.00{{/if}}">',
                    '<button type="submit" class="btn btn-primary btn-block glyphicon glyphicon-shopping-cart js-enable-btn"></button>',
                    '<div>',
                    '<input type="hidden" name="CSRFToken" value="">',
                    '</div></form>',
                    '</div>',
                    '<div class="SearchResultsList-ListOrderFormAction" data-index="3"></div>',
                    '</li>{{/products}}'].join('')
            },
            searchResultContainer: '.product__listing',
            isClickNScroll: false,
            clickNScrollSelector: '',
            isAutoScroll: false,
            isPagination: true,
            paginationContainerSelector: '.pagination',
            paginationTemp: [
                '{{#if hasPrev}}',
                '<li class="pagination-prev"><a unbxdaction="prev" href="javascript:void(0)" rel="next" class="glyphicon glyphicon-chevron-right"></a></li>',
                '{{else}}',
                '<li class="pagination-prev disabled"><span class="glyphicon glyphicon-chevron-left"></span></li>',
                '{{/if}}',
                '{{#pages}}',
                '{{#if current}}',
                '<li class="active"><span>{{page}}<span class="sr-only">(current)</span></span></li>',
                '{{else}}',
                '<li><a class="" href="javascript:void(0)" unbxdaction="{{page}}">{{page}}</a></li>',
                '{{/if}}',
                '{{/pages}}',
                '{{#if hasNext}}',
                '<li class="pagination-next"><a unbxdaction="next" href="javascript:void(0)" rel="next" class="glyphicon glyphicon-chevron-right"></a></li>',
                '{{else}}',
                '<li class="pagination-next disabled"><span class="glyphicon glyphicon-chevron-right"></span></li>',
                '{{/if}}'
            ].join(''),
            viewTypes: ['grid', 'list'],
            viewTypeContainerSelector: '.view-types',
            viewTypeContainerTemp: [
                '{{#options}}',
                '{{log this}}',
                '<span class="view-type-wrapper">',
                '<a class="{{#if selected}}active{{/if}}" unbxdviewtype="{{value}}" href="#">',
                '{{#ifGrid this}}',
                '<i class="glyphicon glyphicon-th"></i>',

                '{{else}}',

                '<i class="glyphicon glyphicon-th-list"></i>',
                '{{/ifGrid}}',
                '</a>',
                '</span>&nbsp;',
                '{{/options}}'
            ].join(''),
            sortContainerSelector: '.sortForm',
            sortOptions: [{
                name: 'Relevance'
            },{
                name: 'Name (ascending)',
                field: 'name',
                order: 'asc'
            },{
                name: 'Name (descending)',
                field: 'name',
                order: 'desc'
            },{
                name: 'Price (highest first)',
                field: 'price',
                order: 'desc'
            },{
                name: 'Price (lowest first)',
                field: 'price',
                order: 'asc'
            }],
            sortContainerType : 'select',
            sortContainerTemp: [
                '<select name="sort" class="form-control">',
                '{{#options}}',
                '{{#if selected}}',
                '<option value="{{field}}-{{order}}" unbxdsortField="{{field}}" unbxdsortValue="{{order}}" selected>{{name}}</option>',
                '{{else}}',
                '<option value="{{field}}-{{order}}" unbxdsortField="{{field}}" unbxdsortValue="{{order}}">{{name}}</option>',
                '{{/if}}',
                '{{/options}}',
                '<input type="hidden" name="q" value="{{query}}:relevance">',
                '</select>'
            ].join(''),
            facetTemp: ['{{#facets}}', '<div class="facet js-facet">', '<div class="facet__name js-facet-name"><span class="glyphicon facet__arrow"></span>{{name}}</div>', '<div class="facet__values js-facet-values">', '<ul class="facet__list">',
                '{{#selected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<label for="{{../facet_name}}_{{value}}"><input type="checkbox" checked class="facet__list__checkbox js-facet-checkbox sr-only" unbxdParam_facetName="{{../facet_name}}"  unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<span class="facet__list__label"><span class="facet__list__mark"></span><span class="facet__list__text">', '{{prepareFacetValue value}} <span class="facet__value__count">({{count}})</span>', '</span></span>','</label>', '</li>', '{{/selected}}',
                '{{#unselected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<label for="{{../facet_name}}_{{value}}"><input type="checkbox" class="facet__list__checkbox js-facet-checkbox sr-only" unbxdParam_facetName="{{../facet_name}}"  unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<span class="facet__list__label"><span class="facet__list__mark"></span><span class="facet__list__text">', '{{prepareFacetValue value}} <span class="facet__value__count">({{count}})</span>', '</span></span></label>', '</li>', '{{/unselected}}', '</ul>', '</div>', '</div>{{/facets}}',
                '{{#rangefacets}}<div class="facet js-facet">', '<div class="facet__name js-facet-name"><span class="glyphicon facet__arrow"></span>{{name}}</div>', '<div class="facet__values js-facet-values">', '<ul class="facet__list">',
                '{{#selected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<label for="{{../facet_name}}_{{value}}"><input type="checkbox" checked class="facet__list__checkbox js-facet-checkbox sr-only" unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetType="{{../type}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<span class="facet__list__label"><span class="facet__list__mark"></span><span class="facet__list__text">', '&pound;{{prepareFacetValue begin}} - &pound;{{prepareFacetValue end}} <span class="facet__value__count">({{count}})</span>', '</span></span>','</label>', '</li>', '{{/selected}}',
                '{{#unselected}}', '<li unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetValue="{{value}}">', '<label for="{{../facet_name}}_{{value}}"><input type="checkbox" class="facet__list__checkbox js-facet-checkbox sr-only" unbxdParam_facetName="{{../facet_name}}" unbxdParam_facetType="{{../type}}" unbxdParam_facetValue="{{value}}" id="{{../facet_name}}_{{value}}">', '<span class="facet__list__label"><span class="facet__list__mark"></span><span class="facet__list__text">', '&pound;{{prepareFacetValue begin}} - &pound;{{prepareFacetValue end}} <span class="facet__value__count">({{count}})</span>', '</span></span></label>', '</li>', '{{/unselected}}', '</ul>', '</div>', '</div>{{/rangefacets}}',
            ].join(''),
            facetContainerSelector: "#product-facet",
            facetCheckBoxSelector: ".facet__list__checkbox.js-facet-checkbox",
            facetElementSelector: "label",
            facetOnSelect: function (el) { },
            facetOnDeselect: function (el) { },
            facetMultiSelect: true,
            selectedFacetTemp: [
                '<div class="facet js-facet"><div class="facet__name js-facet-name"><span class="glyphicon facet__arrow"></span>Applied Facets</div><div class="facet__values js-facet-values"><ul class="facet__list">',
                '{{#each filters}}', '{{#each this}}', '<li>', '<a class="selected-facet-delete" href="javascript:void(0)" unbxdParam_facetName="{{this}}" unbxdParam_facetValue="{{@key}}">{{{prepareFacetValue @key}}}<span class="glyphicon glyphicon-remove"></span></a></li>',  '{{/each}}', '{{/each}}',
                '{{#each ranges}}', '{{#each this}}', '<li>', '<a class="selected-facet-delete" href="javascript:void(0)" unbxdParam_facetName="{{this}}" unbxdParam_facetValue="{{@key}}">&pound;{{{prepareFacetValue @key}}}<span class="glyphicon glyphicon-remove"></span></a></li>', '{{/each}}', '{{/each}}',
                '</ul>','</div>','</div>'
            ].join(''),
            selectedFacetContainerSelector: "#selected-product-facet",
            clearSelectedFacetsSelector: "#clear_all_selected_facets",
            removeSelectedFacetSelector: ".selected-facet-delete",
            selectedFacetHolderSelector: "",
            onFacetLoad: function () {
                console.log('onFacetLoad', arguments, this);
            },
            sanitizeQueryString: function (q) {
                return q;
            },
            loaderSelector: ".result-loader",
            getFacetStats: "priceValue",
            setDefaultFilters: function () {
                //this.addFilter('catalogVersion', catalogVersion);
                //this.addFilter('catalogId', catalogId);
            },
            'template-features': {
                "autoScroll": false,
                "clickScroll": false,
                "gridAndListViewBoth": true,
                "listViewOnly": false,
                "isPagination": true
            },
            onIntialResultLoad: function () {
                //var $t = jQuery(this);
                var selectedView = jQuery(this.options.viewTypeContainerSelector).find(".active").attr("unbxdviewtype");
                console.log("selectedView",selectedView);
                jQuery(this.options.searchResultContainer).removeClass("product__list").removeClass("product__grid").addClass("product__"+selectedView);
                jQuery(".main__inner-wrapper").show();
            },
            onPageLoad: function () {
                var selectedView = jQuery(this.options.viewTypeContainerSelector).find(".active").attr("unbxdviewtype");
                console.log("selectedView",selectedView);
                jQuery(this.options.searchResultContainer).removeClass("product__list").removeClass("product__grid").addClass("product__"+selectedView);
                jQuery(".main__inner-wrapper").show();
            },
            deferInitRender: [],
            bannerSelector: '.banner',
            pageSizeContainerSelector: '.limitForm',
            pageSizeContainerType: 'select',
            pageSizeContainerTemp: ['<select name="limit" class="form-control">',
                '{{#options}}',
                '{{#if selected}}',
                '<option value="{{value}}" unbxdpageSize="{{value}}" selected>{{name}}</option>',
                '{{else}}',
                '<option value="{{value}}" unbxdpageSize="{{value}}">{{name}}</option>',
                '{{/if}}',
                '{{/options}}',
                '</select>'].join(''),
            mappedFields: {
                "imageUrl":"img-300Wx300H",
                "productUrl":"url",
                "title":"name",
                "description":"description",
                "price":"priceValue",
                "categoryPath":"categoryPath",
                "variantFields":{
                    "imageUrl":"vImg-300Wx300H",
                    "productUrl":"vUrl",
                    "title":"vName",
                    "price":"vPriceValue",
                    "groupBy":"vStyle",
                    "swatchFields": {
                        "swatch_background_image":"variant_overhead_swatch",
                        "swatch_background_color":"vStyle",
                        "swatch_click_image":"variant_image_array"
                    }
                }
            },
            searchEndPoint: "https://search.unbxd.io",
            onNoResult: function(){
                jQuery(".main__inner-wrapper").html("\t\t\t\t<div class=\"yCmsContentSlot side-content-slot cms_disp-img_slot searchEmptyPageTop\">\n" +
                    "</div><div class=\"search-empty\">\n" +
                    "\t\t<div class=\"headline\">\n" +
                    "\t\t\t0 items found for keyword <strong>"+ $(this.options.inputSelector).val() +"</strong></div>\n" +
                    "\t\t<a class=\"btn btn-default  js-shopping-button\" href=\"/yacceleratorstorefront/en/\">\n" +
                    "\t\t\tContinue Shopping</a>\n" +
                    "\t</div>\n" +
                    "\t\n" +
                    "\t<div class=\"yCmsContentSlot searchEmptyPageMiddle\">\n" +
                    "</div>").show();
            }
        });
    }
});



