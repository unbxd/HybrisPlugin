/******/ (function(modules) { // webpackBootstrap
    /******/ 	// The module cache
    /******/ 	var installedModules = {};
    /******/
    /******/ 	// The require function
    /******/ 	function __webpack_require__(moduleId) {
        /******/
        /******/ 		// Check if module is in cache
        /******/ 		if(installedModules[moduleId]) {
            /******/ 			return installedModules[moduleId].exports;
            /******/ 		}
        /******/ 		// Create a new module (and put it into the cache)
        /******/ 		var module = installedModules[moduleId] = {
            /******/ 			i: moduleId,
            /******/ 			l: false,
            /******/ 			exports: {}
            /******/ 		};
        /******/
        /******/ 		// Execute the module function
        /******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
        /******/
        /******/ 		// Flag the module as loaded
        /******/ 		module.l = true;
        /******/
        /******/ 		// Return the exports of the module
        /******/ 		return module.exports;
        /******/ 	}
    /******/
    /******/
    /******/ 	// expose the modules object (__webpack_modules__)
    /******/ 	__webpack_require__.m = modules;
    /******/
    /******/ 	// expose the module cache
    /******/ 	__webpack_require__.c = installedModules;
    /******/
    /******/ 	// define getter function for harmony exports
    /******/ 	__webpack_require__.d = function(exports, name, getter) {
        /******/ 		if(!__webpack_require__.o(exports, name)) {
            /******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
            /******/ 		}
        /******/ 	};
    /******/
    /******/ 	// define __esModule on exports
    /******/ 	__webpack_require__.r = function(exports) {
        /******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
            /******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
            /******/ 		}
        /******/ 		Object.defineProperty(exports, '__esModule', { value: true });
        /******/ 	};
    /******/
    /******/ 	// create a fake namespace object
    /******/ 	// mode & 1: value is a module id, require it
    /******/ 	// mode & 2: merge all properties of value into the ns
    /******/ 	// mode & 4: return value when already ns object
    /******/ 	// mode & 8|1: behave like require
    /******/ 	__webpack_require__.t = function(value, mode) {
        /******/ 		if(mode & 1) value = __webpack_require__(value);
        /******/ 		if(mode & 8) return value;
        /******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
        /******/ 		var ns = Object.create(null);
        /******/ 		__webpack_require__.r(ns);
        /******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
        /******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
        /******/ 		return ns;
        /******/ 	};
    /******/
    /******/ 	// getDefaultExport function for compatibility with non-harmony modules
    /******/ 	__webpack_require__.n = function(module) {
        /******/ 		var getter = module && module.__esModule ?
            /******/ 			function getDefault() { return module['default']; } :
            /******/ 			function getModuleExports() { return module; };
        /******/ 		__webpack_require__.d(getter, 'a', getter);
        /******/ 		return getter;
        /******/ 	};
    /******/
    /******/ 	// Object.prototype.hasOwnProperty.call
    /******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
    /******/
    /******/ 	// __webpack_public_path__
    /******/ 	__webpack_require__.p = "build/";
    /******/
    /******/
    /******/ 	// Load entry module and return exports
    /******/ 	return __webpack_require__(__webpack_require__.s = "./src/index.js");
    /******/ })
    /************************************************************************/
    /******/ ({

        /***/ "./src/dot.js":
        /*!********************!*\
          !*** ./src/dot.js ***!
          \********************/
        /*! no static exports found */
        /***/ (function(module, exports, __webpack_require__) {

            var __WEBPACK_AMD_DEFINE_RESULT__;// doT.js
// 2011-2014, Laura Doktorova, https://github.com/olado/doT
// Licensed under the MIT license.
            (function (global) {
                "use strict";

                var doT = {
                        name: "doT",
                        version: "1.1.1",
                        templateSettings: {
                            evaluate: /\{\{([\s\S]+?(\}?)+)\}\}/g,
                            interpolate: /\{\{=([\s\S]+?)\}\}/g,
                            encode: /\{\{!([\s\S]+?)\}\}/g,
                            use: /\{\{#([\s\S]+?)\}\}/g,
                            useParams: /(^|[^\w$])def(?:\.|\[[\'\"])([\w$\.]+)(?:[\'\"]\])?\s*\:\s*([\w$\.]+|\"[^\"]+\"|\'[^\']+\'|\{[^\}]+\})/g,
                            define: /\{\{##\s*([\w\.$]+)\s*(\:|=)([\s\S]+?)#\}\}/g,
                            defineParams: /^\s*([\w$]+):([\s\S]+)/,
                            conditional: /\{\{\?(\?)?\s*([\s\S]*?)\s*\}\}/g,
                            iterate: /\{\{~\s*(?:\}\}|([\s\S]+?)\s*\:\s*([\w$]+)\s*(?:\:\s*([\w$]+))?\s*\}\})/g,
                            varname: "it",
                            strip: true,
                            append: true,
                            selfcontained: false,
                            doNotSkipEncoded: false
                        },
                        template: undefined,
                        //fn, compile template
                        compile: undefined,
                        //fn, for express
                        log: true
                    },
                    _globals;

                doT.encodeHTMLSource = function (doNotSkipEncoded) {
                    var encodeHTMLRules = {
                            "&": "&#38;",
                            "<": "&#60;",
                            ">": "&#62;",
                            '"': "&#34;",
                            "'": "&#39;",
                            "/": "&#47;"
                        },
                        matchHTML = doNotSkipEncoded ? /[&<>"'\/]/g : /&(?!#?\w+;)|<|>|"|'|\//g;
                    return function (code) {
                        return code ? code.toString().replace(matchHTML, function (m) {
                            return encodeHTMLRules[m] || m;
                        }) : "";
                    };
                };

                _globals = function () {
                    return this || (0, eval)("this");
                }();
                /* istanbul ignore else */


                if ( true && module.exports) {
                    module.exports = doT;
                } else if (true) {
                    !(__WEBPACK_AMD_DEFINE_RESULT__ = (function () {
                        return doT;
                    }).call(exports, __webpack_require__, exports, module),
                    __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
                } else {}

                var startend = {
                        append: {
                            start: "'+(",
                            end: ")+'",
                            startencode: "'+encodeHTML("
                        },
                        split: {
                            start: "';out+=(",
                            end: ");out+='",
                            startencode: "';out+=encodeHTML("
                        }
                    },
                    skip = /$^/;

                function resolveDefs(c, block, def) {
                    return (typeof block === "string" ? block : block.toString()).replace(c.define || skip, function (m, code, assign, value) {
                        if (code.indexOf("def.") === 0) {
                            code = code.substring(4);
                        }

                        if (!(code in def)) {
                            if (assign === ":") {
                                if (c.defineParams) value.replace(c.defineParams, function (m, param, v) {
                                    def[code] = {
                                        arg: param,
                                        text: v
                                    };
                                });
                                if (!(code in def)) def[code] = value;
                            } else {
                                new Function("def", "def['" + code + "']=" + value)(def);
                            }
                        }

                        return "";
                    }).replace(c.use || skip, function (m, code) {
                        if (c.useParams) code = code.replace(c.useParams, function (m, s, d, param) {
                            if (def[d] && def[d].arg && param) {
                                var rw = (d + ":" + param).replace(/'|\\/g, "_");
                                def.__exp = def.__exp || {};
                                def.__exp[rw] = def[d].text.replace(new RegExp("(^|[^\\w$])" + def[d].arg + "([^\\w$])", "g"), "$1" + param + "$2");
                                return s + "def.__exp['" + rw + "']";
                            }
                        });
                        var v = new Function("def", "return " + code)(def);
                        return v ? resolveDefs(c, v, def) : v;
                    });
                }

                function unescape(code) {
                    return code.replace(/\\('|\\)/g, "$1").replace(/[\r\t\n]/g, " ");
                }

                doT.template = function (tmpl, c, def) {
                    c = c || doT.templateSettings;
                    var cse = c.append ? startend.append : startend.split,
                        needhtmlencode,
                        sid = 0,
                        indv,
                        str = c.use || c.define ? resolveDefs(c, tmpl, def || {}) : tmpl;
                    str = ("var out='" + (c.strip ? str.replace(/(^|\r|\n)\t* +| +\t*(\r|\n|$)/g, " ").replace(/\r|\n|\t|\/\*[\s\S]*?\*\//g, "") : str).replace(/'|\\/g, "\\$&").replace(c.interpolate || skip, function (m, code) {
                        return cse.start + unescape(code) + cse.end;
                    }).replace(c.encode || skip, function (m, code) {
                        needhtmlencode = true;
                        return cse.startencode + unescape(code) + cse.end;
                    }).replace(c.conditional || skip, function (m, elsecase, code) {
                        return elsecase ? code ? "';}else if(" + unescape(code) + "){out+='" : "';}else{out+='" : code ? "';if(" + unescape(code) + "){out+='" : "';}out+='";
                    }).replace(c.iterate || skip, function (m, iterate, vname, iname) {
                        if (!iterate) return "';} } out+='";
                        sid += 1;
                        indv = iname || "i" + sid;
                        iterate = unescape(iterate);
                        return "';var arr" + sid + "=" + iterate + ";if(arr" + sid + "){var " + vname + "," + indv + "=-1,l" + sid + "=arr" + sid + ".length-1;while(" + indv + "<l" + sid + "){" + vname + "=arr" + sid + "[" + indv + "+=1];out+='";
                    }).replace(c.evaluate || skip, function (m, code) {
                        return "';" + unescape(code) + "out+='";
                    }) + "';return out;").replace(/\n/g, "\\n").replace(/\t/g, '\\t').replace(/\r/g, "\\r").replace(/(\s|;|\}|^|\{)out\+='';/g, '$1').replace(/\+''/g, ""); //.replace(/(\s|;|\}|^|\{)out\+=''\+/g,'$1out+=');

                    if (needhtmlencode) {
                        if (!c.selfcontained && _globals && !_globals._encodeHTML) _globals._encodeHTML = doT.encodeHTMLSource(c.doNotSkipEncoded);
                        str = "var encodeHTML = typeof _encodeHTML !== 'undefined' ? _encodeHTML : (" + doT.encodeHTMLSource.toString() + "(" + (c.doNotSkipEncoded || '') + "));" + str;
                    }

                    try {
                        return new Function(c.varname, str);
                    } catch (e) {
                        /* istanbul ignore else */
                        if (typeof console !== "undefined") console.log("Could not create a template function: " + str);
                        throw e;
                    }
                };

                doT.compile = function (tmpl, def) {
                    return doT.template(tmpl, null, def);
                };

                global.doT = doT;
            })(window);

            /***/ }),

        /***/ "./src/environment.js":
        /*!****************************!*\
          !*** ./src/environment.js ***!
          \****************************/
        /*! no static exports found */
        /***/ (function(module, exports) {

            module.exports = {
                development: {
                    // url: 'http://localhost:4201/',
                    url: 'https://console-lohika.0126-int-use2.unbxd.io/v2.0/'
                },
                production: {
                    url: 'https://recommendations.unbxd.io/v2.0/'
                }
            };

            /***/ }),

        /***/ "./src/handlers/events.js":
        /*!********************************!*\
          !*** ./src/handlers/events.js ***!
          \********************************/
        /*! exports provided: eventHandlers, sendWarning */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "eventHandlers", function() { return eventHandlers; });
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "sendWarning", function() { return sendWarning; });
            var eventHandlers = {
                // side scroll function with delayed scroll to provide smooth scroll
                // feature across cross browsers
                // the code inside the methods are in native javascript
                // because they are to be appended directly in the DOM
                _unbxd_recsSliderSideScroll: function (targetDOMId, direction) {
                    var scrollAmount = 0; // the target selector

                    var elementSelector = "#" + targetDOMId + " #_unbxd_recs-slider-container"; // the element which is going to be scrolled programmatically

                    var element = document.querySelector(elementSelector);

                    if (!element) {
                        return console.warn("slider container id is missing. Execution can not continue");
                    }

                    var sliderItemSelector = "#" + targetDOMId + " ._unbxd_recs-slider__item";
                    var sliderItem = document.querySelector(sliderItemSelector);

                    if (!sliderItem) {
                        return console.warn("slider item tile class is missing. Execution can not continue");
                    }

                    var recsSlider = document.querySelector("#_unbxd_recs-slider"); // hard coding no of steps scrolled in given time frame to produce smooth effect

                    var initialSteps = 30; // hard coding speed along with steps for smooth transition

                    var speed = 25; // taking the no of items to be scrolled from window

                    var itemsToScroll = window._unbxd_recsItemToScrollHz; // an offset flag that is used around scroll limit and smoothness

                    var eventualSteps = initialSteps + itemsToScroll * 5; // the entire width of the slider visible at once exclusive of margin

                    var tileSliderWidth = sliderItem.clientWidth * itemsToScroll; // the total distance to scroll inclusive of margin. 10 is the constant margin

                    var distance = tileSliderWidth + 10 * itemsToScroll;
                    var slideTimer = setInterval(function () {
                        if (direction == 'left') {
                            var distToScroll = scrollAmount + eventualSteps;

                            if (distToScroll > distance) {
                                eventualSteps -= distToScroll - distance;
                            }

                            element.scrollLeft -= eventualSteps;
                        } else {
                            var distToScroll = scrollAmount + eventualSteps;

                            if (distToScroll > distance) {
                                eventualSteps -= distToScroll - distance;
                            }

                            element.scrollLeft += eventualSteps;
                        }

                        scrollAmount += eventualSteps;

                        if (scrollAmount >= distance) {
                            window.clearInterval(slideTimer);
                        }

                        if (element.scrollLeft === 0) {
                            // we have reached the starting position for scroll
                            // thus we need to disable the prev button for slider
                            var prevButton = document.querySelector(elementSelector + " ._unbxd_rex-slider--prev");

                            if (!prevButton) {
                                return console.warn("_unbxd_rex-slider--prev class missing");
                            }

                            prevButton.disabled = true;
                        }

                        if (element.scrollLeft + element.clientWidth === recsSlider.clientWidth) {
                            // we have reached the end position for scroll
                            // thus we need to disable the next button for slider
                            var nextButton = document.querySelector(elementSelector + " ._unbxd_rex-slider--next");

                            if (!nextButton) {
                                return console.warn("_unbxd_rex-slider--next class missing");
                            }

                            nextButton.disabled = true;
                        }
                    }, speed);
                },
                // horizontal slider next button
                _unbxd_recsSliderScrollNext: function (event) {
                    // a bit clumsy. But the only way to reach out to the id of the container
                    var targetEl;

                    try {
                        targetEl = event.currentTarget.parentElement.parentElement.parentElement;
                    } catch (err) {
                        console.warn(err);
                    }

                    if (!targetEl) {
                        console.warn("target element not found. HTML was changed");
                        return;
                    }

                    var targetElId = targetEl.id;
                    var prevButtonSelector = "#" + targetElId + " ._unbxd_rex-slider--prev";
                    var prevButton = document.querySelector(prevButtonSelector);

                    if (!prevButton) {
                        return console.warn("_unbxd_rex-slider--prev class missing");
                    }

                    if (prevButton.disabled) {
                        prevButton.disabled = false;
                    }

                    _unbxd_recsSliderSideScroll(targetElId, 'right');
                },
                // horizontal slider prev button
                _unbxd_recsSliderScrollPrev: function () {
                    var targetEl;

                    try {
                        targetEl = event.currentTarget.parentElement.parentElement.parentElement;
                    } catch (err) {
                        console.warn(err);
                    }

                    if (!targetEl) {
                        console.warn("target element not found. HTML was changed");
                        return;
                    }

                    var targetElId = targetEl.id;
                    var nextButtonSelector = "#" + targetElId + " ._unbxd_rex-slider--next";
                    var nextButton = document.querySelector(nextButtonSelector);

                    if (!nextButton) {
                        return console.warn("_unbxd_rex-slider--next class missing");
                    }

                    if (nextButton.disabled) {
                        nextButton.disabled = false;
                    }

                    _unbxd_recsSliderSideScroll(targetElId, 'left');
                },
                // vertical scroll function with delayed scroll to provide smooth scroll
                // feature across cross browsers
                // the code inside the methods are in native javascript
                // because they are to be appended directly in the DOM
                _unbxd_recsSliderVerticalScroll: function (targetDOMId, direction) {
                    var scrollAmount = 0; // the target selector

                    var elementSelector = "#" + targetDOMId + " #_unbxd_recs-vertical-slider-container"; // the element which is going to be scrolled programmatically

                    var element = document.querySelector(elementSelector);

                    if (!element) {
                        return console.warn("slider container id is missing. Execution can not continue");
                    }

                    var sliderItemSelector = "#" + targetDOMId + " ._unbxd_recs-vertical-slider__item";
                    var sliderItem = document.querySelector(sliderItemSelector);

                    if (!sliderItem) {
                        return console.warn("vertical slider item tile class is missing. Execution can not continue");
                    } // hard coding no of steps scrolled in given time frame to produce smooth effect


                    var initialSteps = 50; // hard coding speed along with steps for smooth transition

                    var speed = 40; // taking the no of items to be scrolled from window

                    var itemsToScroll = 1; //window.recsItemToScrollVt;
                    // an offset flag that is used around scroll limit and smoothness

                    var eventualSteps = initialSteps + itemsToScroll * 5; // the entire width of the slider visible at once exclusive of margin

                    var tileSliderWidth = (sliderItem.clientWidth + 20) * itemsToScroll; // the total distance to scroll inclusive of margin. 10 is the constant margin

                    var distance = tileSliderWidth;
                    var slideTimer = setInterval(function () {
                        if (direction == 'top') {
                            var distToScroll = scrollAmount + eventualSteps;

                            if (distToScroll > distance) {
                                eventualSteps -= distToScroll - distance;
                            }

                            element.scrollLeft -= eventualSteps;
                        } else {
                            var distToScroll = scrollAmount + eventualSteps;

                            if (distToScroll > distance) {
                                eventualSteps -= distToScroll - distance;
                            }

                            element.scrollLeft += eventualSteps;
                        }

                        scrollAmount += eventualSteps;

                        if (scrollAmount >= distance) {
                            window.clearInterval(slideTimer);
                        }

                        if (element.scrollLeft === 0) {
                            // we have reached the starting position for scroll
                            // thus we need to disable the prev button for slider
                            var prevButton = document.querySelector("#" + targetDOMId + " ._unbxd_rex-vertical-slider--top");

                            if (!prevButton) {
                                return console.warn("#" + targetDOMId + " _unbxd_rex-vertical-slider--top class missing");
                            }

                            prevButton.disabled = true;
                        }

                        if (element.clientWidth + element.scrollLeft >= element.scrollWidth) {
                            // we have reached the end position for scroll
                            // thus we need to disable the next button for slider
                            var nextButton = document.querySelector("#" + targetDOMId + " ._unbxd_rex-vertical-slider--bottom");

                            if (!nextButton) {
                                return console.warn("#" + targetDOMId + " _unbxd_rex-vertical-slider--bottom class missing");
                            }

                            nextButton.disabled = true;
                        }
                    }, speed);
                },
                _unbxd_recsSliderScrollBottom: function () {
                    var targetEl;

                    try {
                        targetEl = event.currentTarget.parentElement.parentElement.parentElement;
                    } catch (err) {
                        console.warn(err);
                    }

                    if (!targetEl) {
                        console.warn("target element not found. HTML was changed");
                        return;
                    }

                    var targetElId = targetEl.id;
                    var topButtonSelector = "#" + targetElId + " ._unbxd_rex-vertical-slider--top";
                    var topButton = document.querySelector(topButtonSelector);

                    if (!topButton) {
                        return console.warn("_unbxd_rex-vertical-slider--top class missing");
                    }

                    if (topButton.disabled) {
                        topButton.disabled = false;
                    }

                    _unbxd_recsSliderVerticalScroll(targetElId, 'bottom');
                },
                _unbxd_recsSliderScrollTop: function () {
                    var targetEl;

                    try {
                        targetEl = event.currentTarget.parentElement.parentElement.parentElement;
                    } catch (err) {
                        console.warn(err);
                    }

                    if (!targetEl) {
                        console.warn("target element not found. HTML was changed");
                        return;
                    }

                    var targetElId = targetEl.id;
                    var bottomButtonSelector = "#" + targetElId + " ._unbxd_rex-vertical-slider--bottom";
                    var bottomButtom = document.querySelector(bottomButtonSelector);

                    if (!bottomButtom) {
                        return console.warn("_unbxd_rex-vertical-slider--bottom class missing");
                    }

                    if (bottomButtom.disabled) {
                        bottomButtom.disabled = false;
                    }

                    _unbxd_recsSliderVerticalScroll(targetElId, 'top');
                }
            };
            var sendWarning = function (msg) {
                return console.warn(msg);
            };

            /***/ }),

        /***/ "./src/handlers/images.js":
        /*!********************************!*\
          !*** ./src/handlers/images.js ***!
          \********************************/
        /*! exports provided: appendImagesToClass, setImagesSource */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "appendImagesToClass", function() { return appendImagesToClass; });
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "setImagesSource", function() { return setImagesSource; });
            var appendImagesToClass = function (className, imgUrl, targetContainerId) {
                var elements = document.querySelectorAll("#" + targetContainerId + " ." + className);

                for (var i = 0; i < elements.length; i++) {
                    var img = document.createElement('img');
                    img.src = imgUrl;
                    elements[i].appendChild(img);
                }
            };
            var setImagesSource = function (imgArr, targetContainerId) {
                for (var i = 0; i < imgArr.length; i++) {
                    appendImagesToClass(imgArr[i].classname, imgArr[i].url, targetContainerId);
                }
            };

            /***/ }),

        /***/ "./src/handlers/index.js":
        /*!*******************************!*\
          !*** ./src/handlers/index.js ***!
          \*******************************/
        /*! exports provided: eventHandlers, appendImagesToClass, setImagesSource, sendWarning */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony import */ var _events__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./events */ "./src/handlers/events.js");
            /* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "eventHandlers", function() { return _events__WEBPACK_IMPORTED_MODULE_0__["eventHandlers"]; });

            /* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "sendWarning", function() { return _events__WEBPACK_IMPORTED_MODULE_0__["sendWarning"]; });

            /* harmony import */ var _images__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./images */ "./src/handlers/images.js");
            /* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "appendImagesToClass", function() { return _images__WEBPACK_IMPORTED_MODULE_1__["appendImagesToClass"]; });

            /* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "setImagesSource", function() { return _images__WEBPACK_IMPORTED_MODULE_1__["setImagesSource"]; });





            /***/ }),

        /***/ "./src/index.js":
        /*!**********************!*\
          !*** ./src/index.js ***!
          \**********************/
        /*! no exports provided */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony import */ var _dot__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./dot */ "./src/dot.js");
            /* harmony import */ var _dot__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_dot__WEBPACK_IMPORTED_MODULE_0__);
            /* harmony import */ var _handlers__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./handlers */ "./src/handlers/index.js");
            /* harmony import */ var _ratings__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./ratings */ "./src/ratings.js");
            /* harmony import */ var _strikeThrough__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./strikeThrough */ "./src/strikeThrough.js");
            /* harmony import */ var _environment__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./environment */ "./src/environment.js");
            /* harmony import */ var _environment__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(_environment__WEBPACK_IMPORTED_MODULE_4__);








            (function (global) {
                /**
                 * Global declaration section
                 */

                /** Function for fetching api requests */
                function fetchData(url, cb) {
                    var xhttp = new XMLHttpRequest();

                    xhttp.onreadystatechange = function () {
                        if (this.readyState == 4 && (this.status == 200 || this.status == 204)) {
                            // Typical action to be performed when the document is ready:
                            cb(null, xhttp.responseText);
                        } else if (this.readyState == 4 && (this.status != 200 || this.status != 204)) {
                            cb('Invalid network request: ' + url);
                        }
                    };

                    xhttp.onerror = function () {
                        cb('Failed network request: ' + url);
                    };

                    xhttp.open("GET", url, true);
                    xhttp.send();
                }
                /** This function find the node containing selector passed as param
                 *  closest to element passed as param
                 *  The purpose of this function is to find the target tile parent w.r.t
                 *  event target. This was done to minimize the no of click handlers
                 * */


                function getClosestNode(elem, selector) {
                    var firstChar = selector.charAt(0); // Get closest match

                    for (; elem && elem !== document; elem = elem.parentNode) {
                        // If selector is a class
                        if (firstChar === '.') {
                            if (elem.classList.contains(selector.substr(1))) {
                                return elem;
                            }
                        } // If selector is an ID


                        if (firstChar === '#') {
                            if (elem.id === selector.substr(1)) {
                                return elem;
                            }
                        } // If selector is a data attribute


                        if (firstChar === '[') {
                            if (elem.hasAttribute(selector.substr(1, selector.length - 2))) {
                                return elem;
                            }
                        } // If selector is a tag


                        if (elem.tagName.toLowerCase() === selector) {
                            return elem;
                        }
                    }

                    return false;
                }

                ;
                var MOBILE = 'mobile';
                var DESKTOP = 'desktop';
                var SMALL = 'small';
                var LARGE = 'large';

                function getDeviceType() {
                    console.log("screen.width: ", window.screen.width);
                    console.log("screen.height: ", window.screen.height);
                    const mediaQueryList = window.matchMedia("(orientation: portrait)");

                    if (mediaQueryList.matches) {
                        console.log('portrait mode');

                        if (window.screen.width <= 667) {
                            return MOBILE;
                        } else {
                            return DESKTOP;
                        }
                    } else {
                        console.log('landscape mode');

                        if (window.screen.height <= 667) {
                            return MOBILE;
                        } else {
                            return DESKTOP;
                        }
                    }
                }

                function getBrowserSize() {
                    console.log("window.innerWidth: ", window.innerWidth);
                    console.log("window.innerHeight: ", window.innerHeight);

                    if (window.innerWidth <= 667) {
                        return SMALL;
                    } else {
                        return LARGE;
                    }
                }
                /** Global variables */
                    // the domain url
                    // var platformDomain = 'http://localhost:4201/';
                    // console.log(process.env.NODE_ENV);


                var platformDomain = _environment__WEBPACK_IMPORTED_MODULE_4___default.a["development"].url; // var platformDomain = 'https://console-lohika.0126-int-use2.unbxd.io/v2.0/';
                // Constants

                var HOME_PAGE = 'home';
                var PRODUCT_PAGE = 'product';
                var CATEGORY_PAGE = 'category';
                var CART_PAGE = 'cart';
                var allowedPageTypes = [HOME_PAGE, PRODUCT_PAGE, CATEGORY_PAGE, CART_PAGE];
                var widgetIdMap = {};
                widgetIdMap[HOME_PAGE] = {
                    'widget1': 'unbxd_rex_' + HOME_PAGE + '_w1',
                    'widget2': 'unbxd_rex_' + HOME_PAGE + '_w2',
                    'widget3': 'unbxd_rex_' + HOME_PAGE + '_w3'
                };
                widgetIdMap[PRODUCT_PAGE] = {
                    'widget1': 'unbxd_rex_' + PRODUCT_PAGE + '_w1',
                    'widget2': 'unbxd_rex_' + PRODUCT_PAGE + '_w2',
                    'widget3': 'unbxd_rex_' + PRODUCT_PAGE + '_w3'
                };
                widgetIdMap[CATEGORY_PAGE] = {
                    'widget1': 'unbxd_rex_' + CATEGORY_PAGE + '_w1',
                    'widget2': 'unbxd_rex_' + CATEGORY_PAGE + '_w2',
                    'widget3': 'unbxd_rex_' + CATEGORY_PAGE + '_w3'
                };
                widgetIdMap[CART_PAGE] = {
                    'widget1': 'unbxd_rex_' + CART_PAGE + '_w1',
                    'widget2': 'unbxd_rex_' + CART_PAGE + '_w2',
                    'widget3': 'unbxd_rex_' + CART_PAGE + '_w3'
                }; // Declaration of template containers

                var widget1;
                var widget2;
                var widget3; // Horizontal template config containers

                var horizontalConfig;
                var horizontalAssets; // Vertical template config containers

                var verticalConfig;
                var verticalAssets; // Setting constant values for margin between slider items and the DOM id for the slider

                var margin = 10;
                /** End of Global variables */

                /**
                 * End of Global declaration section
                 */

                /** Scripts and styles that are appended to the DOM */

                /** Adding event handlers for the horizontal slider to the DOM */

                global._unbxd_recsSliderScrollNext = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderScrollNext;
                global._unbxd_recsSliderScrollPrev = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderScrollPrev;
                global._unbxd_recsSliderSideScroll = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderSideScroll;
                global._unbxd_recsSliderScrollBottom = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderScrollBottom;
                global._unbxd_recsSliderScrollTop = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderScrollTop;
                global._unbxd_recsSliderVerticalScroll = _handlers__WEBPACK_IMPORTED_MODULE_1__["eventHandlers"]._unbxd_recsSliderVerticalScroll;
                /** End of Scripts and styles that are appended to the DOM */
                    // Configuration object for vertical/horizontal sliders

                var sliderConfig = {
                        horizontal: {
                            containerId: " #_unbxd_recs-slider-container",
                            sliderItemClassSelector: " ._unbxd_recs-slider__item",
                            dimension: "width",
                            offsetDimension: "offsetWidth",
                            buttonClassSelector: "._unbxd_recs-slider-btn",
                            prevButtonClass: "_unbxd_rex-slider--prev",
                            nextButtonClass: "_unbxd_rex-slider--next",
                            headingContainerId: " #_unbxd_recs-slider-heading",
                            sliderContentClass: "_unbxd_recs-slider__content"
                        },
                        vertical: {
                            containerId: " #_unbxd_recs-vertical-slider-container",
                            sliderItemClassSelector: " ._unbxd_recs-vertical-slider__item",
                            dimension: "height",
                            offsetDimension: "offsetHeight",
                            buttonClassSelector: "._unbxd_recs-vertical-slider-btn",
                            prevButtonClass: "_unbxd_rex-vertical-slider--top",
                            nextButtonClass: "_unbxd_rex-vertical-slider--bottom",
                            headingContainerId: " #_unbxd_recs-vertical-slider-heading",
                            sliderContentClass: "_unbxd_recs-vertical-slider__content"
                        }
                    };

                function missingValueError(valueKey, contentObject) {
                    throw new Error('Error: ' + valueKey + ' not found in ' + JSON.stringify(contentObject));
                }

                function handleHorizontalWidgetClicks(parentId, clickHandler, recommendations) {
                    var hzRegex = /hz-item/;

                    if (hzRegex.test(parentId)) {
                        var arrayIndex = parentId.split("-")[2]; // fixed id of form hz-slider-0

                        clickHandler(recommendations[arrayIndex]);
                    }
                }

                function handleVerticalWidgetClicks(parent1Id, parent2Id, clickHandler, recommendationsModified) {
                    var vtRegex = /[0-9]-vt-level2-/;

                    if (vtRegex.test(parent1Id)) {
                        var parent1ArrayIndex = parent1Id.split("-")[3]; // fixed id of form *-vt-slider-0

                        var parent2ArrayIndex = parent2Id.split("-")[3];
                        clickHandler(recommendationsModified[parent2ArrayIndex][parent1ArrayIndex]);
                    }
                }

                function handleSizeCalculations(targetDOMElementId, options) {
                    var rexConsoleConfigs = options.rexConsoleConfigs;
                    var recommendations = options.recommendations;
                    var clickHandler = options.clickHandler;
                    var itemsToShow = options.itemsToShow;
                    var maxProducts = options.maxProducts;
                    var assets = options.assets;
                    var sliderType = options.sliderType;
                    var sliderClass = options.sliderClass;
                    var recommendationsModified = options.recommendationsModified;
                    var sliderContent = sliderConfig[sliderType];
                    var domSelector = "#" + targetDOMElementId + sliderContent.containerId;
                    var sliderContainer = document.querySelector(domSelector);
                    var widgetWidth = options.widgetWidth;
                    var ratingFeature = rexConsoleConfigs.products.ratings_feature || rexConsoleConfigs.products.ratingsFeature;

                    if (!sliderContainer) {
                        return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])('The slider container id was not found. Script can not continue');
                    }

                    var sliderItemSelector = "#" + targetDOMElementId + sliderContent.sliderItemClassSelector;
                    var sliderItems = document.querySelectorAll(sliderItemSelector);

                    if (!sliderItems.length) {
                        return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])('Found 0 nodes with class : ' + sliderContent.sliderItemClassSelector);
                    }

                    var productFields = rexConsoleConfigs.products.fields || missingValueError('products.fields', rexConsoleConfigs);
                    productFields = productFields.sort(function (b, a) {
                        a.sequence = a.sequence || a.sequence_number;
                        b.sequence = b.sequence || b.sequence_number;

                        if (a.sequence < b.sequence) {
                            return 1;
                        }

                        return -1;
                    });
                    var dimension = sliderContent.dimension;

                    if (clickHandler) {
                        if (sliderContent.dimension == "width") {
                            sliderContainer.addEventListener("click", function (event) {
                                if (event.target.className == "_unbxd_recs-slider__item") {
                                    handleHorizontalWidgetClicks(event.target.id, clickHandler, recommendations);
                                } else {
                                    var el = getClosestNode(event.target, "._unbxd_recs-slider__item");
                                    handleHorizontalWidgetClicks(el.id, clickHandler, recommendations);
                                }
                            });
                        } else {
                            sliderContainer.addEventListener("click", function (event) {
                                if (event.target.className == "_unbxd_recs-vertical-slider__item") {
                                    var parentId = event.target.parentElement.id;
                                    handleVerticalWidgetClicks(event.target.id, parentId, clickHandler, recommendationsModified);
                                } else {
                                    var el = getClosestNode(event.target, "._unbxd_recs-vertical-slider__item");
                                    var parentId = el.parentElement.id;
                                    handleVerticalWidgetClicks(el.id, parentId, clickHandler, recommendationsModified);
                                }
                            });
                        }
                    }

                    for (var i = 0; i < sliderItems.length; i++) {
                        var fragment = document.createDocumentFragment();

                        for (var j = 0; j < productFields.length; j++) {
                            var styles = productFields[j].styles || missingValueError('styles', productFields[j]);
                            var productAttributeKey = productFields[j].unbxdDimensionKey || productFields[j].catalogKey || missingValueError('unbxdDimensionKey or catalogKey', productFields[j]);
                            var cssArr = Object.keys(styles);

                            if (!recommendations[i][productAttributeKey]) {
                                productAttributeKey = productFields[j].catalogKey;
                            } // appending fields to slider item
                            // field appending doesn't applies to imageUrl


                            if (productAttributeKey != "imageUrl") {
                                var newnode = document.createElement("p");
                                var dimension = recommendations[i][productAttributeKey];
                                newnode.className = sliderContent.sliderContentClass;

                                if (rexConsoleConfigs.products.strike_price_feature && productAttributeKey == rexConsoleConfigs.products.strike_price_feature.new.field) {
                                    if (rexConsoleConfigs.products.strike_price_feature.enabled) {
                                        newnode.innerHTML = Object(_strikeThrough__WEBPACK_IMPORTED_MODULE_3__["strikeThrough"])(recommendations[i], rexConsoleConfigs, domSelector);
                                    } else {
                                        newnode.innerHTML = rexConsoleConfigs.products.currency + dimension;
                                    }
                                } else if (ratingFeature && ratingFeature.enabled && productFields[j].unbxdDimensionKey && productFields[j].unbxdDimensionKey.toLowerCase() == "rating") {
                                    var ratingContentData = Object(_ratings__WEBPACK_IMPORTED_MODULE_2__["getRatingContent"])(recommendations[i], ratingFeature, domSelector, productAttributeKey);

                                    if (ratingContentData) {
                                        newnode.innerHTML = ratingContentData;
                                    }
                                } else {
                                    if (!dimension) {
                                        newnode.innerHTML = "";
                                    } else {
                                        newnode.innerHTML = dimension;
                                    }
                                }

                                if (newnode.innerHTML) {
                                    for (var k = 0; k < cssArr.length; k++) {
                                        newnode.style[cssArr[k]] = styles[cssArr[k]];
                                    }

                                    fragment.appendChild(newnode);
                                }
                            }
                        }

                        sliderItems[i].appendChild(fragment);
                    } // Setting width of each slider item and
                    // setting width of the visible slider


                    var recsSliderSelector = "#" + targetDOMElementId + " ." + sliderClass;
                    var recsSlider = document.querySelector(recsSliderSelector);

                    if (!recsSlider) {
                        return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])('Slider Parent id was not found in the DOM');
                    }

                    var maxprodLimit = maxProducts;

                    if (recommendations.length < maxProducts) {
                        maxprodLimit = recommendations.length;
                    }

                    function incrementCounter() {
                        counter++;

                        if (counter === len) {
                            if (sliderContent.dimension == "width") {
                                setTimeout(function () {
                                    var sliderParentContainer = document.querySelector("#" + targetDOMElementId + " .unbxd-recs-slider");
                                    var sliderRootContainer = sliderParentContainer.parentElement;
                                    sliderParentContainer.style.width = widgetWidth || "initial";

                                    if (sliderRootContainer.clientWidth < sliderParentContainer.clientWidth) {
                                        sliderParentContainer.style.width = sliderRootContainer.clientWidth + "px";
                                    }

                                    sliderContainer.style.width = sliderContainer[sliderContent.offsetDimension] + "px";
                                    var hzSliderWidth = (sliderContainer[sliderContent.offsetDimension] - itemsToShow * margin) / itemsToShow;

                                    for (i = 0; i < sliderItems.length; i++) {
                                        sliderItems[i].style.width = hzSliderWidth + "px";
                                        recsSlider.style.width = maxprodLimit * hzSliderWidth + maxprodLimit * margin + "px";
                                    }

                                    var opaqueElSelector = document.querySelector("#" + targetDOMElementId + " ._unxbd_slider_hide");
                                    opaqueElSelector.classList.remove("_unxbd_slider_hide");
                                }, 0);
                            } else {
                                setTimeout(function () {
                                    var sliderParentContainer = document.querySelector("#" + targetDOMElementId + " ._unbxd_vertical-recs-slider");
                                    var sliderRootContainer = sliderParentContainer.parentElement; // if root container width is less than configuration width, then
                                    // the container inherits root container width

                                    sliderParentContainer.style.width = widgetWidth || "initial";

                                    if (sliderRootContainer.clientWidth < sliderParentContainer.clientWidth) {
                                        sliderParentContainer.style.width = sliderRootContainer.clientWidth + "px";
                                    }

                                    for (i = 0; i < sliderItems.length; i++) {
                                        sliderItems[i].style.width = sliderParentContainer.clientWidth - 2 * margin + "px";
                                    }

                                    recsSlider.style.width = sliderParentContainer.clientWidth * recommendationsModified.length + "px";
                                    var opaqueElSelector = document.querySelector("#" + targetDOMElementId + " ._unxbd_slider_hide");
                                    opaqueElSelector.classList.remove("_unxbd_slider_hide");
                                }, 0);
                            }
                        }
                    }

                    var imgs = document.images,
                        len = imgs.length,
                        counter = 0;
                    [].forEach.call(imgs, function (img) {
                        if (img.complete) incrementCounter();else img.addEventListener('load', incrementCounter, false);
                    });
                    /** Setting styles for carousel buttons */
                    // the navigation button need to be hidden in case the total no of items to be shown
                    // are less than the no of items to be shown at in one slide

                    if (recommendations.length <= itemsToShow) {
                        var navigationButtonSelector = "#" + targetDOMElementId + " " + sliderContent.buttonClassSelector;
                        var navigationButtons = document.querySelectorAll(navigationButtonSelector);

                        if (!navigationButtons || !navigationButtons.length) {
                            return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])(sliderContent.buttonClassSelector + 'class not found on navigation buttons');
                        }

                        for (var i = 0; i < navigationButtons.length; i++) {
                            navigationButtons[i].style.display = 'none';
                        }
                    } // the previous button for the slider needs to be disabled initially


                    var prevSliderButtonSelector = "#" + targetDOMElementId + " ." + sliderContent.prevButtonClass;
                    var prevSliderButton = document.querySelector(prevSliderButtonSelector);

                    if (!prevSliderButton) {
                        return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])(sliderContent.prevButtonClass + ' class was not found on the navigation buttons');
                    }

                    prevSliderButton.disabled = true;
                    /** Setting images value */

                    var imgArr = [];
                    var classMap = {
                        "next_arrow": sliderContent.nextButtonClass,
                        "prev_arrow": sliderContent.prevButtonClass,
                        "empty_rating": "_unbxd_rex-empty-star",
                        "half_rating": "_unbxd_rex-half-star",
                        "full_rating": "_unbxd_rex-full-star"
                    };

                    for (i = 0; i < assets.length; i++) {
                        var horizontalAssetItem = assets[i];
                        imgArr.push({
                            classname: classMap[horizontalAssetItem.tag],
                            url: horizontalAssetItem.src
                        });
                    }

                    Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["setImagesSource"])(imgArr, targetDOMElementId);
                    /** Setting images value end*/

                    /** Setting styles for heading */

                    var headingSelector = "#" + targetDOMElementId + sliderContent.headingContainerId;
                    var styleConfig = rexConsoleConfigs.header;
                    var headingEl = document.querySelector(headingSelector);

                    if (headingEl.innerHTML == "null" || headingEl.innerHTML == "undefined") {
                        headingEl.style.display = "none";
                    } else {
                        headingEl.style.textAlign = styleConfig.alignment;
                        headingEl.style.fontSize = styleConfig.text.size.value + styleConfig.text.size.unit;
                        headingEl.style.fontWeight = styleConfig.text.style;
                        headingEl.style.color = styleConfig.text.colour;
                    }
                    /** End of Setting styles for heading */

                }
                /** exporting a global function to initialize recs slider */


                global._unbxd_generateRexContent = function (options) {
                    // console.log(options)

                    /** Template rendering logic */
                    var template = options.template || missingValueError('template', options);
                    var targetDOMElementId = options.targetDOMElementId || missingValueError('targetDOMElementId', options);
                    var recommendations = options.recommendations || missingValueError('recommendations', options);
                    var heading = options.heading || missingValueError('heading', options);
                    var rexConsoleConfigs = options.rexConsoleConfigs || missingValueError('rexConsoleConfigs', options);
                    var itemsToShow = rexConsoleConfigs.products.visible || missingValueError('products.visible', rexConsoleConfigs);
                    var maxProducts = rexConsoleConfigs.products.max || missingValueError('products.max', rexConsoleConfigs.products);
                    var clickHandler = options.clickHandler;
                    var dataParser = options.dataParser;
                    var isVertical = options.isVertical;
                    var compressedStyle = rexConsoleConfigs.css || missingValueError('css', rexConsoleConfigs);
                    var recommendationsModified = null;
                    var widgetWidthData = rexConsoleConfigs.widget.width || missingValueError('products.widget.width', rexConsoleConfigs.widget); // var widgetWidthData = verticalConfig.width;

                    var widgetWidth = "";

                    if (widgetWidthData.value && widgetWidthData.value != 0) {
                        widgetWidth = widgetWidthData.value + widgetWidthData.unit;
                    }

                    var renderFn = doT.template(template);
                    var renderTargetEl = document.getElementById(targetDOMElementId); // console.log(screen.width)
                    // console.log(window.innerWidth);

                    var device = getDeviceType();
                    var browserSize = getBrowserSize();

                    if (device === MOBILE || browserSize === SMALL) {
                        const itemsToShowOnMobile = rexConsoleConfigs.products.visibleOnMobile;
                        itemsToShow = itemsToShowOnMobile ? itemsToShowOnMobile : 2;
                    }

                    if (!renderTargetEl) {
                        return Object(_handlers__WEBPACK_IMPORTED_MODULE_1__["sendWarning"])('The target element id <' + targetDOMElementId + '> is not present in DOM. Execution can not continue');
                    }

                    if (maxProducts < recommendations.length) {
                        recommendations = recommendations.splice(0, maxProducts);
                    }

                    if (isVertical) {
                        recommendationsModified = [];

                        for (var i = 0; i < recommendations.length; i++) {
                            if (i % itemsToShow === 0) {
                                var slicedItems = recommendations.slice(i, i + itemsToShow);
                                recommendationsModified.push(slicedItems);
                            }
                        }
                    }

                    var templateData = {
                        recommendations: recommendationsModified || recommendations,
                        heading: heading
                    };
                    /* Callback to make any modification to data and pass on the modified data to renderFn  */

                    if (dataParser && typeof dataParser === "function") {
                        templateData = dataParser(templateData);
                    }

                    document.getElementById(targetDOMElementId).innerHTML = renderFn(templateData);
                    /** Dynamically adjusting width based on no of items to be shown */

                    var sliderOptionsConfig = {
                        rexConsoleConfigs: rexConsoleConfigs,
                        recommendations: recommendations,
                        recommendationsModified: recommendationsModified,
                        clickHandler: clickHandler,
                        itemsToShow: itemsToShow,
                        maxProducts: maxProducts,
                        assets: options.assets,
                        sliderType: isVertical ? "vertical" : "horizontal",
                        sliderClass: isVertical ? "_unbxd_recs-vertical-slider" : "_unbxd_recs-slider",
                        widgetWidth: widgetWidth
                    }; // no of items to be shown

                    if (isVertical) {
                        global._unbxd_recsItemToScrollVt = itemsToShow;
                    } else {
                        global._unbxd_recsItemToScrollHz = itemsToShow;
                    }
                    /** Attaching styles for the slider */


                    var eventHandlerStyle = document.createElement('style');
                    eventHandlerStyle.type = 'text/css'; // innerHTML needs to stay as es5 since it will be embedded directly to client's browser

                    eventHandlerStyle.innerHTML = compressedStyle;
                    document.head.appendChild(eventHandlerStyle);
                    handleSizeCalculations(targetDOMElementId, sliderOptionsConfig);
                };
                /** The initialization function that has to be exposed to the merchandiser website
                 *  it takes context object from the client html
                 *  and makes a call to the recommender proxy
                 *  and updates the dom as per the response
                 */


                global._unbxd_getRecommendations = function (context) {
                    // Get widget id
                    function getWidgetId(pageType, widgetKey, widgetDetails) {
                        console.log(pageType, widgetKey, widgetDetails);
                        var widgetIdLocal;

                        if (widgetDetails) {
                            return widgetDetails[widgetKey] ? widgetDetails[widgetKey].name : null;
                        } else {
                            widgetIdLocal = widgetIdMap[pageType.toLowerCase()][widgetKey]; // Check if widget exists in the page

                            if (document.getElementById(widgetIdLocal)) {
                                return widgetIdLocal;
                            } else {
                                return null;
                            }
                        }

                        return null;
                    }

                    function getPageDetails(pageInfo) {
                        if (!pageInfo || !pageInfo.pageType) {
                            throw new Error("Page type info missing");
                        }

                        var pageTypeLocal = pageInfo.pageType;

                        if (allowedPageTypes.indexOf(pageTypeLocal.toLowerCase()) == -1) {
                            throw new Error("Invalid value for page type");
                        }

                        return pageTypeLocal;
                    }

                    function getClickHandler(context) {
                        return context.itemClickHandler;
                    }

                    function getDataParserHandler(context) {
                        return context.dataParser;
                    }

                    function getUrlEncodedParam(paramName, paramValue) {
                        return "" + paramName + "=" + encodeURIComponent(paramValue);
                    }

                    function getProductIdsAsUrlParams(productIdsList) {
                        var queryStringLocal = '';

                        if (productIdsList instanceof Array) {
                            productIdsList.forEach(function (item) {
                                queryStringLocal += '&' + getUrlEncodedParam('id', item);
                            });
                        } else {
                            queryStringLocal += '&' + getUrlEncodedParam('id', productIdsList);
                        }

                        return queryStringLocal;
                    } // getting page info


                    var pageType = getPageDetails(context.pageInfo); // get widget if

                    var widgets = context.widgets;
                    widget1 = getWidgetId(pageType, 'widget1', widgets);
                    widget2 = getWidgetId(pageType, 'widget2', widgets);
                    widget3 = getWidgetId(pageType, 'widget3', widgets);

                    if (!widget1 && !widget2 && !widget3) {
                        throw new Error('No widget id provided');
                    }

                    var itemClickHandler = getClickHandler(context);
                    var dataParser = getDataParserHandler(context); // getting userId, siteKey and apiKey

                    var userInfo = context.userInfo;

                    if (!userInfo) {
                        throw new Error("User info missing");
                    }

                    var userId = userInfo.userId;
                    var siteKey = userInfo.siteKey;
                    var apiKey = userInfo.apiKey;
                    var requestUrl = platformDomain + apiKey + "/" + siteKey + '/items?&template=true&pageType=';

                    if (!userId) {
                        throw new Error("user id is missing");
                    }

                    if (!siteKey) {
                        throw new Error("site Key is missing");
                    }

                    if (!apiKey) {
                        throw new Error("api key is missing");
                    }

                    requestUrl += encodeURIComponent(pageType);
                    var pageInfo = context.pageInfo;

                    switch (pageType.toLowerCase()) {
                        case PRODUCT_PAGE:
                        case CART_PAGE:
                            if (!pageInfo.productIds) {
                                throw new Error("product id is missing for page type:" + pageType);
                            }

                            requestUrl += getProductIdsAsUrlParams(pageInfo.productIds);
                            break;

                        case CATEGORY_PAGE:
                            var catlevel1Name = pageInfo.catlevel1Name;

                            if (!catlevel1Name) {
                                throw new Error("catlevel1Name is mandatory for page type:" + pageType);
                            }

                            var catlevel2Name = pageInfo.catlevel2Name;
                            var catlevel3Name = pageInfo.catlevel3Name;
                            var catlevel4Name = pageInfo.catlevel4Name;
                            var categoryUrl = "&" + getUrlEncodedParam("catlevel1Name", catlevel1Name);

                            if (catlevel2Name) {
                                categoryUrl += "&" + getUrlEncodedParam("catlevel2Name", catlevel2Name);

                                if (catlevel3Name) {
                                    categoryUrl += "&" + getUrlEncodedParam("catlevel3Name=", catlevel3Name);

                                    if (catlevel4Name) {
                                        categoryUrl += "&" + getUrlEncodedParam("catlevel4Name=", catlevel4Name);
                                    }
                                }
                            }

                            requestUrl += categoryUrl;
                            break;

                        case HOME_PAGE:
                            break;

                        default:
                            throw new Error("Invalid page type: " + pageType);
                    }

                    requestUrl += "&uid=" + userId;

                    function renderWidgetDataHorizontal(widget, recommendations, heading) {
                        var maxProducts = horizontalConfig.products.max || horizontalConfig.products.max_products;
                        var targetDOMElementId = widget;
                        var clickHandler = itemClickHandler;

                        if (recommendations.length) {
                            if (maxProducts < recommendations.length) {
                                recommendations = recommendations.splice(0, maxProducts);
                            }

                            var options = {
                                template: horizontalTemplate,
                                targetDOMElementId: targetDOMElementId,
                                recommendations: recommendations,
                                heading: heading,
                                rexConsoleConfigs: horizontalConfig,
                                assets: horizontalAssets,
                                maxProducts: maxProducts,
                                clickHandler: clickHandler,
                                dataParser: dataParser,
                                sliderClass: "_unbxd_recs-slider",
                                compressedStyle: compressedStyle
                            };

                            _unbxd_generateRexContent(options);
                        }
                    }

                    function renderWidgetDataVertical(widget, recommendations, heading) {
                        var maxProducts = verticalConfig.products.max || verticalConfig.products.max_products;
                        var targetDOMElementId = widget;
                        var clickHandler = itemClickHandler;

                        if (recommendations.length) {
                            if (maxProducts < recommendations.length) {
                                recommendations = recommendations.splice(0, maxProducts);
                            }

                            var options = {
                                template: verticalTemplate,
                                targetDOMElementId: targetDOMElementId,
                                recommendations: recommendations,
                                heading: heading,
                                rexConsoleConfigs: verticalConfig,
                                assets: verticalAssets,
                                maxProducts: maxProducts,
                                clickHandler: clickHandler,
                                dataParser: dataParser,
                                isVertical: true,
                                sliderClass: "_unbxd_recs-vertical-slider",
                                compressedStyle: compressedStyleVertical
                            };

                            _unbxd_generateRexContent(options);
                        }
                    }

                    function handleWidgetRenderingVertical() {
                        if (widget3) {
                            var widget3Data = recommendationsResponse.widget3;
                            var widget3Heading = widget3Data.widgetTitle;
                            var widget3Recommendations = widget3Data.recommendations;
                            renderWidgetDataVertical(widget3, widget3Recommendations, widget3Heading);
                        }
                    }

                    function handleWidgetRendering() {
                        if (widget1) {
                            var widget1Data = recommendationsResponse.widget1;
                            var widget1Heading = widget1Data.widgetTitle;
                            var widget1Recommendations = widget1Data.recommendations;
                            renderWidgetDataHorizontal(widget1, widget1Recommendations, widget1Heading);
                        }

                        if (widget2) {
                            var widget2Data = recommendationsResponse.widget2;
                            var widget2Heading = widget2Data.widgetTitle;
                            var widget2Recommendations = widget2Data.recommendations;
                            renderWidgetDataHorizontal(widget2, widget2Recommendations, widget2Heading);
                        }
                    }

                    function horizontalTemplateHandler(err, res) {
                        if (err) {
                            throw new Error('Failed to fetch templates');
                        } // populating the template string


                        horizontalTemplate = res;
                        handleWidgetRendering();
                    }

                    function verticalTemplateHandler(err, res) {
                        if (err) {
                            throw new Error('Failed to fetch templates');
                        } // populating the template string


                        verticalTemplate = res;
                        handleWidgetRenderingVertical();
                    }
                    /** Fetch recommendations response */
                        // to store recommendations response


                    var recommendationsResponse; // to store template string

                    var horizontalTemplate; // to store vertical template string

                    var verticalTemplate;
                    var compressedStyle;
                    var compressedStyleVertical;
                    fetchData(requestUrl, function (err, res) {
                        // fetching data specific to a page type
                        if (err) {
                            throw new Error('Failed to fetch recommendations');
                        }

                        recommendationsResponse = JSON.parse(res); // horizontal templates configuration

                        horizontalTemplate = recommendationsResponse.template.horizontal;

                        if (horizontalTemplate) {
                            horizontalConfig = horizontalTemplate.conf;
                            horizontalAssets = horizontalConfig.assets;
                            var templateUrlHorizontal = horizontalTemplate.scriptUrl;

                            if (templateUrlHorizontal) {
                                /** Fetch template layout string */
                                fetchData(templateUrlHorizontal, horizontalTemplateHandler);
                            } else {
                                console.warn("script url not found for horizontal template");
                            }
                        } // vertical templates configuration


                        verticalTemplate = recommendationsResponse.template.vertical;

                        if (verticalTemplate) {
                            verticalConfig = verticalTemplate.conf;
                            verticalAssets = verticalConfig.assets;
                            var templateUrlVertical = verticalTemplate.scriptUrl;

                            if (templateUrlVertical) {
                                /** Fetch vertical template layout string */
                                fetchData(templateUrlVertical, verticalTemplateHandler);
                            } else {
                                console.warn("script url not found for vertical template");
                            }
                        }
                    });
                };
            })(window);

            /***/ }),

        /***/ "./src/ratings.js":
        /*!************************!*\
          !*** ./src/ratings.js ***!
          \************************/
        /*! exports provided: getRatings, getRatingContent */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "getRatings", function() { return getRatings; });
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "getRatingContent", function() { return getRatingContent; });
            var getRatings = function (rating) {
                if (isNaN(rating)) {
                    console.warn("Invalid rating value provided");
                    return '';
                } // Divide the rating by 0.5
                // if the result is an even no e.g 8
                //  // divide the no by 2. this will be our full stars
                //  // our empty stars will be 5 - (full stars)
                // else when the no is odd e.g 7
                //  // divide the no by 2 and floor the result. This will be the total no of full stars;
                //  // half star will be one
                //  // empty stars will be 5 - (full stars + half stars)


                var result = rating / 0.5;
                var fullStars = 0;
                var halfStars = 0;
                var emptyStars = 0;
                var resultString = '';

                if (result % 2 !== 0) {
                    halfStars = 1;
                }

                fullStars = Math.floor(result / 2);
                emptyStars = 5 - (fullStars + halfStars);

                for (var i = 0; i < fullStars; i++) {
                    resultString += '<span class="_unbxd_rex-full-star recs-star _unbxd_rating-item"></span>';
                }

                for (var i = 0; i < halfStars; i++) {
                    resultString += '<span class="_unbxd_rex-half-star recs-star _unbxd_rating-item"></span>';
                }

                for (var i = 0; i < emptyStars; i++) {
                    resultString += '<span class="_unbxd_rex-empty-star recs-star _unbxd_rating-item"></span>';
                }

                return resultString;
            };

            function getRatingValuePrefixed(recommendation, ratingConfig, productAttributeKey) {
                return "<span class='_unbxd-rating-value-container'><span class='_unbxd-rating-value-prefix _unbxd_rating-item'>" + ratingConfig.prefix.text + "</span><span class='_unbxd-rating-value'>" + recommendation[productAttributeKey] + "</span></span>";
            }

            function styleRatingValue(domSelector, ratingConfig) {
                setTimeout(function () {
                    var prefixItemsStyles = document.querySelectorAll(domSelector + " ._unbxd-rating-value-prefix");
                    var valueItemStyles = document.querySelectorAll(domSelector + " ._unbxd-rating-value");

                    for (var i = 0; i < prefixItemsStyles.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(ratingConfig.prefix.styles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                prefixItemsStyles[index].style[stylesArr[j]] = ratingConfig.prefix.styles[stylesArr[j]];
                            }
                        })(i);

                        (function (index) {
                            var stylesArr = Object.keys(ratingConfig.value.styles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                valueItemStyles[index].style[stylesArr[j]] = ratingConfig.value.styles[stylesArr[j]];
                            }
                        })(i);
                    }
                }, 0);
            }

            var getRatingContent = function (recommendation, ratingConfig, domSelector, productAttributeKey) {
                if (!recommendation[productAttributeKey]) {
                    return;
                }

                var ratingData = ""; // if type selected is value only

                if (ratingConfig.type === "value") {
                    ratingData = getRatingValuePrefixed(recommendation, ratingConfig, productAttributeKey);
                    styleRatingValue(domSelector, ratingConfig);
                } // if type selected is image only
                else if (ratingConfig.type === "image") {
                    ratingData = getRatings(recommendation[productAttributeKey]);
                } // else both
                else {
                    // if image comes first
                    if (ratingConfig.sequence[0] === "image") {
                        ratingData = getRatings(recommendation[productAttributeKey]) + "<br>" + getRatingValuePrefixed(recommendation, ratingConfig, productAttributeKey);
                    } // else
                    else {
                        ratingData = getRatingValuePrefixed(recommendation, ratingConfig, productAttributeKey) + "<br>" + getRatings(recommendation[productAttributeKey]);
                    }

                    styleRatingValue(domSelector, ratingConfig);
                }

                return ratingData;
            };

            /***/ }),

        /***/ "./src/strikeThrough.js":
        /*!******************************!*\
          !*** ./src/strikeThrough.js ***!
          \******************************/
        /*! exports provided: strikeThrough */
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "strikeThrough", function() { return strikeThrough; });
            var strikeThrough = function (recommendation, config, domSelector) {
                var strikeConfigObj = config.products.strike_price_feature;
                var displayPriceObj = strikeConfigObj.new;
                var strikeThroughObj = strikeConfigObj.old;
                var discountObj = strikeConfigObj.discount;
                var oldPrice = recommendation[strikeThroughObj.field];
                var oldPricePrefObject = strikeThroughObj.prefix;
                var currentPrice = recommendation[displayPriceObj.field]; // if old price exists

                var priceHtml = "";

                if (oldPrice && oldPrice > currentPrice) {
                    priceHtml = "<p class='_unbxd_strike_through_container'><span class='_unbxd_strike_through_prefix'>" + oldPricePrefObject.text + "</span><span class='_unbxd_strike_through_text'>" + config.products.currency + oldPrice + "</span></p>";
                    priceHtml += "<p class='_unbxd_original_price_container'>" + displayObjectText(displayPriceObj) + displayOriginalPrice(config, currentPrice) + "</p>" + displayDiscountText(discountObj, oldPrice, currentPrice, config, domSelector); // calling paint and reflow for original and old price

                    styleStrikeThroughItems(config, domSelector);
                } else {
                    priceHtml = "<p class='_unbxd_original_price_container'>" + config.products.currency + currentPrice + "</p>";
                }

                return priceHtml;
            };

            function displayOriginalPrice(config, currentPrice) {
                return "<span class='_unbxd_original_price_value'>" + config.products.currency + currentPrice + "</span>";
            }

            function displayObjectText(displayPriceObj) {
                if (displayPriceObj.prefix.text) {
                    return "<span class='_unbxd_item_display_text'>" + displayPriceObj.prefix.text + "</span>";
                }

                return "";
            }

            function displayDiscountText(discountObj, oldPrice, newPrice, config, domSelector) {
                var discountHtml = "";

                if (discountObj.enabled) {
                    var mode = discountObj.mode;
                    var discountVal = 0;

                    if (mode == "percentage") {
                        var discPercent = (oldPrice - newPrice) / oldPrice * 100;
                        discountVal = discPercent % 1 === 0 ? discPercent : parseFloat(discPercent).toFixed(2);
                        discountVal += "%";
                    } else {
                        var discVal = oldPrice - newPrice;
                        discVal = discVal % 1 === 0 ? discVal : parseFloat(discVal).toFixed(2);
                        discountVal = config.products.currency + discVal;
                    }

                    if (discountObj.prefix.text) {
                        discountHtml = "<p class='_unbxd_item_discount_text'>" + "<span class='_unbxd_discount_text_label'>" + discountObj.prefix.text + "</span>" + "<span class='_unbxd_discount_text_val'>" + discountVal + "</span></p>"; // calling paint and reflow for discounted price

                        styleDiscountedLabels(config, domSelector);
                    } else {
                        discountHtml = "<p class='_unbxd_discount_text_val'>" + discountVal + "</p>";
                    }
                }

                return discountHtml;
            }

            function styleStrikeThroughItems(config, domSelector) {
                setTimeout(function () {
                    var strikethroughItems = document.querySelectorAll(domSelector + " ._unbxd_strike_through_text");
                    var strikethroughPrefixes = document.querySelectorAll(domSelector + " ._unbxd_strike_through_prefix");
                    var strikedObjConfStyles = config.products.strike_price_feature.old.value.styles;
                    var strikedPrefixStlyes = config.products.strike_price_feature.old.prefix.styles;

                    for (var i = 0; i < strikethroughItems.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(strikedObjConfStyles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                strikethroughItems[index].style[stylesArr[j]] = strikedObjConfStyles[stylesArr[j]];
                            }
                        })(i);
                    }

                    for (var i = 0; i < strikethroughPrefixes.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(strikedPrefixStlyes);

                            for (var j = 0; j < stylesArr.length; j++) {
                                strikethroughPrefixes[index].style[stylesArr[j]] = strikedPrefixStlyes[stylesArr[j]];
                            }
                        })(i);
                    }
                }, 0);
                setTimeout(function () {
                    var originalItems = document.querySelectorAll(domSelector + " ._unbxd_original_price_value");
                    var originalConfStyles = config.products.strike_price_feature.new.value.styles;
                    var originalItemsPrefix = document.querySelectorAll(domSelector + " ._unbxd_item_display_text");
                    var originalPrefixConfStyles = config.products.strike_price_feature.new.prefix.styles;

                    for (var i = 0; i < originalItems.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(originalConfStyles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                originalItems[index].style[stylesArr[j]] = originalConfStyles[stylesArr[j]];
                            }
                        })(i);
                    }

                    for (var i = 0; i < originalItemsPrefix.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(originalPrefixConfStyles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                originalItemsPrefix[index].style[stylesArr[j]] = originalPrefixConfStyles[stylesArr[j]];
                            }
                        })(i);
                    }
                }, 0);
            }

            function styleDiscountedLabels(config, domSelector) {
                setTimeout(function () {
                    var discountedLabels = document.querySelectorAll(domSelector + " ._unbxd_discount_text_val");
                    var discountedObjConfStyles = config.products.strike_price_feature.discount.value.styles;
                    var discountedLabelsPrefix = document.querySelectorAll(domSelector + " ._unbxd_discount_text_label");
                    var discountedObjPrefixConfStyles = config.products.strike_price_feature.discount.prefix.styles;

                    for (var i = 0; i < discountedLabels.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(discountedObjConfStyles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                discountedLabels[index].style[stylesArr[j]] = discountedObjConfStyles[stylesArr[j]];
                            }
                        })(i);
                    }

                    for (var i = 0; i < discountedLabelsPrefix.length; i++) {
                        (function (index) {
                            var stylesArr = Object.keys(discountedObjPrefixConfStyles);

                            for (var j = 0; j < stylesArr.length; j++) {
                                discountedLabelsPrefix[index].style[stylesArr[j]] = discountedObjPrefixConfStyles[stylesArr[j]];
                            }
                        })(i);
                    }
                }, 0);
            }

            /***/ })

        /******/ });
//# sourceMappingURL=unbxd_rex_template_sdk_dev.js.map