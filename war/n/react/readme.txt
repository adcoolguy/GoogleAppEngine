Setup
-----
i.bat
ig.bat

Run
---
GoogleAppEngine\war\n\react>npm run build

> node@0.0.0 build C:\Users\jim\GoogleAppEngine\war\n\react
> webpack

[BABEL] Note: The code generator has deoptimised the styling of "C:/Users/jim/Go
ogleAppEngine/war/n/react/node_modules/jquery/dist/jquery.js" as it exceeds the
max of "100KB".
Hash: b9ea5ba9c9a2ae8f596a
Version: webpack 1.13.3
Time: 45497ms
            Asset     Size  Chunks             Chunk Names
  about.bundle.js  2.21 kB       0  [emitted]  about
contact.bundle.js  2.23 kB       1  [emitted]  contact
   main.bundle.js  1.06 MB       2  [emitted]  main
 vendor.bundle.js   693 kB       3  [emitted]  vendor
   [0] multi vendor 40 bytes {3} [built]
    + 168 hidden modules

GoogleAppEngine\war\n\react>


GoogleAppEngine\war\n\react>npm run start

> node@0.0.0 start C:\Users\jim\GoogleAppEngine\war\n\react
> webpack-dev-server

 http://localhost:3000/
webpack result is served from /
content is served from ./build
[BABEL] Note: The code generator has deoptimised the styling of "C:/Users/jim/Go
ogleAppEngine/war/n/react/node_modules/jquery/dist/jquery.js" as it exceeds the
max of "100KB".
Hash: b36c13cf08d54ce22a69
Version: webpack 1.13.3
Time: 59864ms
            Asset     Size  Chunks             Chunk Names
  about.bundle.js  2.36 kB       0  [emitted]  about
contact.bundle.js  2.38 kB       1  [emitted]  contact
   main.bundle.js  1.06 MB       2  [emitted]  main
 vendor.bundle.js   930 kB       3  [emitted]  vendor
chunk    {0} about.bundle.js (about) 2.12 kB {3} [rendered]
    [0] multi about 40 bytes {0} [built]
   [75] ./dist/about.js 2.08 kB {0} [built]
chunk    {1} contact.bundle.js (contact) 2.14 kB {3} [rendered]
    [0] multi contact 40 bytes {1} [built]
  [233] ./dist/contact.js 2.1 kB {1} [built]
chunk    {2} main.bundle.js (main) 1.06 MB {3} [rendered]
    [0] multi main 40 bytes {2} [built]
  [234] ./main.js 2.38 kB {2} [built]
  [235] ./~/jquery/dist/jquery.js 219 kB {2} [built]
  [236] ./style.scss 1.01 kB {2} [built]
  [237] ./~/css-loader!./~/sass-loader!./style.scss 235 bytes {2} [built]
  [238] ./~/css-loader/lib/css-base.js 1.48 kB {2} [built]
  [239] ./rubicon.jpg 829 kB {2} [built]
  [240] ./~/style-loader/addStyles.js 7.15 kB {2} [built]
chunk    {3} vendor.bundle.js (vendor) 877 kB [rendered]
    [0] multi vendor 52 bytes {3} [built]
    [1] (webpack)-dev-server/client?http://localhost:3000 3.97 kB {3} [built]
    [2] ./~/url/url.js 22.1 kB {3} [built]
    [3] ./~/url/~/punycode/punycode.js 15.1 kB {3} [built]
    [4] (webpack)/buildin/module.js 259 bytes {3} [built]
    [5] (webpack)/buildin/amd-options.js 42 bytes {3} [built]
    [6] ./~/querystring/index.js 126 bytes {3} [built]
    [7] ./~/querystring/decode.js 2.42 kB {3} [built]
    [8] ./~/querystring/encode.js 2.46 kB {3} [built]
    [9] ./~/strip-ansi/index.js 161 bytes {3} [built]
   [10] ./~/ansi-regex/index.js 139 bytes {3} [built]
   [11] (webpack)-dev-server/client/socket.js 872 bytes {3} [built]
   [12] ./~/sockjs-client/lib/entry.js 243 bytes {3} [built]
   [13] ./~/sockjs-client/lib/transport-list.js 596 bytes {3} [built]
   [14] ./~/sockjs-client/lib/transport/websocket.js 2.73 kB {3} [built]
   [15] ./~/process/browser.js 5.32 kB {3} [built]
   [16] ./~/sockjs-client/lib/utils/event.js 2.08 kB {3} [built]
   [17] ./~/sockjs-client/lib/utils/random.js 775 bytes {3} [built]
   [18] ./~/sockjs-client/lib/utils/browser-crypto.js 439 bytes {3} [built]
   [19] ./~/sockjs-client/lib/utils/url.js 1.03 kB {3} [built]
   [20] ./~/url-parse/index.js 10.1 kB {3} [built]
   [21] ./~/requires-port/index.js 763 bytes {3} [built]
   [22] ./~/url-parse/lolcation.js 1.91 kB {3} [built]
   [23] ./~/querystringify/index.js 1.3 kB {3} [built]
   [24] ./~/debug/browser.js 3.99 kB {3} [built]
   [25] ./~/debug/debug.js 4.11 kB {3} [built]
   [26] ./~/ms/index.js 2.32 kB {3} [built]
   [27] ./~/inherits/inherits_browser.js 701 bytes {3} [built]
   [28] ./~/sockjs-client/lib/event/emitter.js 1.27 kB {3} [built]
   [29] ./~/sockjs-client/lib/event/eventtarget.js 1.86 kB {3} [built]
   [30] ./~/sockjs-client/lib/transport/browser/websocket.js 171 bytes {3} [buil
t]
   [31] ./~/sockjs-client/lib/transport/xhr-streaming.js 1.25 kB {3} [built]
   [32] ./~/sockjs-client/lib/transport/lib/ajax-based.js 1.32 kB {3} [built]
   [33] ./~/sockjs-client/lib/transport/lib/sender-receiver.js 1.15 kB {3} [buil
t]
   [34] ./~/sockjs-client/lib/transport/lib/buffered-sender.js 2.31 kB {3} [buil
t]
   [35] ./~/sockjs-client/lib/transport/lib/polling.js 1.32 kB {3} [built]
   [36] ./~/sockjs-client/lib/transport/receiver/xhr.js 1.59 kB {3} [built]
   [37] ./~/sockjs-client/lib/transport/sender/xhr-cors.js 340 bytes {3} [built]

   [38] ./~/sockjs-client/lib/transport/browser/abstract-xhr.js 4.88 kB {3} [bui
lt]
   [39] ./~/sockjs-client/lib/transport/sender/xhr-local.js 349 bytes {3} [built
]
   [40] ./~/sockjs-client/lib/utils/browser.js 579 bytes {3} [built]
   [41] ./~/sockjs-client/lib/transport/xdr-streaming.js 984 bytes {3} [built]
   [42] ./~/sockjs-client/lib/transport/sender/xdr.js 2.47 kB {3} [built]
   [43] ./~/sockjs-client/lib/transport/eventsource.js 767 bytes {3} [built]
   [44] ./~/sockjs-client/lib/transport/receiver/eventsource.js 1.59 kB {3} [bui
lt]
   [45] ./~/sockjs-client/lib/transport/browser/eventsource.js 51 bytes {3} [bui
lt]
   [46] ./~/sockjs-client/lib/transport/lib/iframe-wrap.js 981 bytes {3} [built]

   [47] ./~/sockjs-client/lib/transport/iframe.js 3.86 kB {3} [built]
   [48] ./~/json3/lib/json3.js 44.3 kB {3} [built]
   [49] ./~/sockjs-client/lib/version.js 40 bytes {3} [built]
   [50] ./~/sockjs-client/lib/utils/iframe.js 5.45 kB {3} [built]
   [51] ./~/sockjs-client/lib/utils/object.js 864 bytes {3} [built]
   [52] ./~/sockjs-client/lib/transport/htmlfile.js 710 bytes {3} [built]
   [53] ./~/sockjs-client/lib/transport/receiver/htmlfile.js 2.23 kB {3} [built]

   [54] ./~/sockjs-client/lib/transport/xhr-polling.js 895 bytes {3} [built]
   [55] ./~/sockjs-client/lib/transport/xdr-polling.js 712 bytes {3} [built]
   [56] ./~/sockjs-client/lib/transport/jsonp-polling.js 1.02 kB {3} [built]
   [57] ./~/sockjs-client/lib/transport/receiver/jsonp.js 5.58 kB {3} [built]
   [58] ./~/sockjs-client/lib/transport/sender/jsonp.js 2.48 kB {3} [built]
   [59] ./~/sockjs-client/lib/main.js 11.9 kB {3} [built]
   [60] ./~/sockjs-client/lib/shims.js 18.5 kB {3} [built]
   [61] ./~/sockjs-client/lib/utils/escape.js 2.33 kB {3} [built]
   [62] ./~/sockjs-client/lib/utils/transport.js 1.37 kB {3} [built]
   [63] ./~/sockjs-client/lib/utils/log.js 448 bytes {3} [built]
   [64] ./~/sockjs-client/lib/event/event.js 479 bytes {3} [built]
   [65] ./~/sockjs-client/lib/location.js 181 bytes {3} [built]
   [66] ./~/sockjs-client/lib/event/close.js 292 bytes {3} [built]
   [67] ./~/sockjs-client/lib/event/trans-message.js 289 bytes {3} [built]
   [68] ./~/sockjs-client/lib/info-receiver.js 2.24 kB {3} [built]
   [69] ./~/sockjs-client/lib/transport/sender/xhr-fake.js 455 bytes {3} [built]

   [70] ./~/sockjs-client/lib/info-iframe.js 1.55 kB {3} [built]
   [71] ./~/sockjs-client/lib/info-iframe-receiver.js 793 bytes {3} [built]
   [72] ./~/sockjs-client/lib/info-ajax.js 1.04 kB {3} [built]
   [73] ./~/sockjs-client/lib/iframe-bootstrap.js 2.91 kB {3} [built]
   [74] ./~/sockjs-client/lib/facade.js 724 bytes {3} [built]
   [76] ./~/react/react.js 55 bytes {3} [built]
   [77] ./~/react/lib/React.js 1.49 kB {3} [built]
   [78] ./~/react/lib/ReactDOM.js 3.71 kB {3} [built]
   [79] ./~/react/lib/ReactCurrentOwner.js 654 bytes {3} [built]
   [80] ./~/react/lib/ReactDOMTextComponent.js 4.39 kB {3} [built]
   [81] ./~/react/lib/DOMChildrenOperations.js 5.01 kB {3} [built]
   [82] ./~/react/lib/Danger.js 7.02 kB {3} [built]
   [83] ./~/react/~/fbjs/lib/ExecutionEnvironment.js 1.09 kB {3} [built]
   [84] ./~/react/~/fbjs/lib/createNodesFromMarkup.js 2.71 kB {3} [built]
   [85] ./~/react/~/fbjs/lib/createArrayFromMixed.js 2.68 kB {3} [built]
   [86] ./~/react/~/fbjs/lib/toArray.js 2.3 kB {3} [built]
   [87] ./~/react/~/fbjs/lib/invariant.js 1.51 kB {3} [built]
   [88] ./~/react/~/fbjs/lib/getMarkupWrap.js 3.06 kB {3} [built]
   [89] ./~/react/~/fbjs/lib/emptyFunction.js 1.09 kB {3} [built]
   [90] ./~/react/lib/ReactMultiChildUpdateTypes.js 861 bytes {3} [built]
   [91] ./~/react/~/fbjs/lib/keyMirror.js 1.28 kB {3} [built]
   [92] ./~/react/lib/ReactPerf.js 2.55 kB {3} [built]
   [93] ./~/react/lib/setInnerHTML.js 3.38 kB {3} [built]
   [94] ./~/react/lib/setTextContent.js 1.23 kB {3} [built]
   [95] ./~/react/lib/escapeTextContentForBrowser.js 849 bytes {3} [built]
   [96] ./~/react/lib/DOMPropertyOperations.js 8.05 kB {3} [built]
   [97] ./~/react/lib/DOMProperty.js 9.64 kB {3} [built]
   [98] ./~/react/lib/quoteAttributeValueForBrowser.js 746 bytes {3} [built]
   [99] ./~/react/~/fbjs/lib/warning.js 1.78 kB {3} [built]
  [100] ./~/react/lib/ReactComponentBrowserEnvironment.js 1.28 kB {3} [built]
  [101] ./~/react/lib/ReactDOMIDOperations.js 3.38 kB {3} [built]
  [102] ./~/react/lib/ReactMount.js 37.1 kB {3} [built]
  [103] ./~/react/lib/ReactBrowserEventEmitter.js 12.5 kB {3} [built]
  [104] ./~/react/lib/EventConstants.js 2.04 kB {3} [built]
  [105] ./~/react/lib/EventPluginHub.js 9.79 kB {3} [built]
  [106] ./~/react/lib/EventPluginRegistry.js 8.5 kB {3} [built]
  [107] ./~/react/lib/EventPluginUtils.js 6.83 kB {3} [built]
  [108] ./~/react/lib/ReactErrorUtils.js 2.29 kB {3} [built]
  [109] ./~/react/lib/accumulateInto.js 1.74 kB {3} [built]
  [110] ./~/react/lib/forEachAccumulated.js 912 bytes {3} [built]
  [111] ./~/react/lib/ReactEventEmitterMixin.js 1.32 kB {3} [built]
  [112] ./~/react/lib/ViewportMetrics.js 657 bytes {3} [built]
  [113] ./~/react/lib/Object.assign.js 1.26 kB {3} [built]
  [114] ./~/react/lib/isEventSupported.js 1.97 kB {3} [built]
  [115] ./~/react/lib/ReactDOMFeatureFlags.js 458 bytes {3} [built]
  [116] ./~/react/lib/ReactElement.js 8.41 kB {3} [built]
  [117] ./~/react/lib/canDefineProperty.js 632 bytes {3} [built]
  [118] ./~/react/lib/ReactEmptyComponentRegistry.js 1.38 kB {3} [built]
  [119] ./~/react/lib/ReactInstanceHandles.js 10.8 kB {3} [built]
  [120] ./~/react/lib/ReactRootIndex.js 749 bytes {3} [built]
  [121] ./~/react/lib/ReactInstanceMap.js 1.27 kB {3} [built]
  [122] ./~/react/lib/ReactMarkupChecksum.js 1.42 kB {3} [built]
  [123] ./~/react/lib/adler32.js 1.2 kB {3} [built]
  [124] ./~/react/lib/ReactReconciler.js 3.62 kB {3} [built]
  [125] ./~/react/lib/ReactRef.js 2.34 kB {3} [built]
  [126] ./~/react/lib/ReactOwner.js 3.5 kB {3} [built]
  [127] ./~/react/lib/ReactUpdateQueue.js 11.1 kB {3} [built]
  [128] ./~/react/lib/ReactUpdates.js 8.68 kB {3} [built]
  [129] ./~/react/lib/CallbackQueue.js 2.47 kB {3} [built]
  [130] ./~/react/lib/PooledClass.js 3.66 kB {3} [built]
  [131] ./~/react/lib/Transaction.js 9.62 kB {3} [built]
  [132] ./~/react/~/fbjs/lib/emptyObject.js 482 bytes {3} [built]
  [133] ./~/react/~/fbjs/lib/containsNode.js 1.43 kB {3} [built]
  [134] ./~/react/~/fbjs/lib/isTextNode.js 628 bytes {3} [built]
  [135] ./~/react/~/fbjs/lib/isNode.js 1.03 kB {3} [built]
  [136] ./~/react/lib/instantiateReactComponent.js 4.92 kB {3} [built]
  [137] ./~/react/lib/ReactCompositeComponent.js 28.2 kB {3} [built]
  [138] ./~/react/lib/ReactComponentEnvironment.js 1.69 kB {3} [built]
  [139] ./~/react/lib/ReactPropTypeLocations.js 549 bytes {3} [built]
  [140] ./~/react/lib/ReactPropTypeLocationNames.js 611 bytes {3} [built]
  [141] ./~/react/lib/shouldUpdateReactComponent.js 1.87 kB {3} [built]
  [142] ./~/react/lib/ReactEmptyComponent.js 1.9 kB {3} [built]
  [143] ./~/react/lib/ReactNativeComponent.js 3.09 kB {3} [built]
  [144] ./~/react/lib/validateDOMNesting.js 13.2 kB {3} [built]
  [145] ./~/react/lib/ReactDefaultInjection.js 3.99 kB {3} [built]
  [146] ./~/react/lib/BeforeInputEventPlugin.js 15.2 kB {3} [built]
  [147] ./~/react/lib/EventPropagators.js 5.22 kB {3} [built]
  [148] ./~/react/lib/FallbackCompositionState.js 2.52 kB {3} [built]
  [149] ./~/react/lib/getTextContentAccessor.js 994 bytes {3} [built]
  [150] ./~/react/lib/SyntheticCompositionEvent.js 1.16 kB {3} [built]
  [151] ./~/react/lib/SyntheticEvent.js 5.84 kB {3} [built]
  [152] ./~/react/lib/SyntheticInputEvent.js 1.15 kB {3} [built]
  [153] ./~/react/~/fbjs/lib/keyOf.js 1.12 kB {3} [built]
  [154] ./~/react/lib/ChangeEventPlugin.js 11.5 kB {3} [built]
  [155] ./~/react/lib/getEventTarget.js 931 bytes {3} [built]
  [156] ./~/react/lib/isTextInputElement.js 1.03 kB {3} [built]
  [157] ./~/react/lib/ClientReactRootIndex.js 571 bytes {3} [built]
  [158] ./~/react/lib/DefaultEventPluginOrder.js 1.26 kB {3} [built]
  [159] ./~/react/lib/EnterLeaveEventPlugin.js 3.92 kB {3} [built]
  [160] ./~/react/lib/SyntheticMouseEvent.js 2.23 kB {3} [built]
  [161] ./~/react/lib/SyntheticUIEvent.js 1.65 kB {3} [built]
  [162] ./~/react/lib/getEventModifierState.js 1.3 kB {3} [built]
  [163] ./~/react/lib/HTMLDOMPropertyConfig.js 7.63 kB {3} [built]
  [164] ./~/react/lib/ReactBrowserComponentMixin.js 1.16 kB {3} [built]
  [165] ./~/react/lib/findDOMNode.js 2.17 kB {3} [built]
  [166] ./~/react/lib/ReactDefaultBatchingStrategy.js 1.96 kB {3} [built]
  [167] ./~/react/lib/ReactDOMComponent.js 37.5 kB {3} [built]
  [168] ./~/react/lib/AutoFocusUtils.js 850 bytes {3} [built]
  [169] ./~/react/~/fbjs/lib/focusNode.js 726 bytes {3} [built]
  [170] ./~/react/lib/CSSPropertyOperations.js 5.84 kB {3} [built]
  [171] ./~/react/lib/CSSProperty.js 3.5 kB {3} [built]
  [172] ./~/react/~/fbjs/lib/camelizeStyleName.js 1.03 kB {3} [built]
  [173] ./~/react/~/fbjs/lib/camelize.js 729 bytes {3} [built]
  [174] ./~/react/lib/dangerousStyleValue.js 1.93 kB {3} [built]
  [175] ./~/react/~/fbjs/lib/hyphenateStyleName.js 1 kB {3} [built]
  [176] ./~/react/~/fbjs/lib/hyphenate.js 822 bytes {3} [built]
  [177] ./~/react/~/fbjs/lib/memoizeStringOnly.js 779 bytes {3} [built]
  [178] ./~/react/lib/ReactDOMButton.js 1.17 kB {3} [built]
  [179] ./~/react/lib/ReactDOMInput.js 5.81 kB {3} [built]
  [180] ./~/react/lib/LinkedValueUtils.js 5.24 kB {3} [built]
  [181] ./~/react/lib/ReactPropTypes.js 12.7 kB {3} [built]
  [182] ./~/react/lib/getIteratorFn.js 1.17 kB {3} [built]
  [183] ./~/react/lib/ReactDOMOption.js 2.85 kB {3} [built]
  [184] ./~/react/lib/ReactChildren.js 5.83 kB {3} [built]
  [185] ./~/react/lib/traverseAllChildren.js 7.22 kB {3} [built]
  [186] ./~/react/lib/ReactDOMSelect.js 6.17 kB {3} [built]
  [187] ./~/react/lib/ReactDOMTextarea.js 4.39 kB {3} [built]
  [188] ./~/react/lib/ReactMultiChild.js 14.9 kB {3} [built]
  [189] ./~/react/lib/ReactChildReconciler.js 4.57 kB {3} [built]
  [190] ./~/react/lib/flattenChildren.js 1.65 kB {3} [built]
  [191] ./~/react/~/fbjs/lib/shallowEqual.js 1.64 kB {3} [built]
  [192] ./~/react/lib/ReactEventListener.js 7.62 kB {3} [built]
  [193] ./~/react/~/fbjs/lib/EventListener.js 2.7 kB {3} [built]
  [194] ./~/react/~/fbjs/lib/getUnboundedScrollPosition.js 1.09 kB {3} [built]
  [195] ./~/react/lib/ReactInjection.js 1.37 kB {3} [built]
  [196] ./~/react/lib/ReactClass.js 28.4 kB {3} [built]
  [197] ./~/react/lib/ReactComponent.js 5.39 kB {3} [built]
  [198] ./~/react/lib/ReactNoopUpdateQueue.js 4.05 kB {3} [built]
  [199] ./~/react/lib/ReactReconcileTransaction.js 4.66 kB {3} [built]
  [200] ./~/react/lib/ReactInputSelection.js 4.4 kB {3} [built]
  [201] ./~/react/lib/ReactDOMSelection.js 6.83 kB {3} [built]
  [202] ./~/react/lib/getNodeForCharacterOffset.js 1.66 kB {3} [built]
  [203] ./~/react/~/fbjs/lib/getActiveElement.js 924 bytes {3} [built]
  [204] ./~/react/lib/SelectEventPlugin.js 6.73 kB {3} [built]
  [205] ./~/react/lib/ServerReactRootIndex.js 889 bytes {3} [built]
  [206] ./~/react/lib/SimpleEventPlugin.js 17.5 kB {3} [built]
  [207] ./~/react/lib/SyntheticClipboardEvent.js 1.24 kB {3} [built]
  [208] ./~/react/lib/SyntheticFocusEvent.js 1.12 kB {3} [built]
  [209] ./~/react/lib/SyntheticKeyboardEvent.js 2.78 kB {3} [built]
  [210] ./~/react/lib/getEventCharCode.js 1.56 kB {3} [built]
  [211] ./~/react/lib/getEventKey.js 2.93 kB {3} [built]
  [212] ./~/react/lib/SyntheticDragEvent.js 1.13 kB {3} [built]
  [213] ./~/react/lib/SyntheticTouchEvent.js 1.33 kB {3} [built]
  [214] ./~/react/lib/SyntheticWheelEvent.js 2 kB {3} [built]
  [215] ./~/react/lib/SVGDOMPropertyConfig.js 3.8 kB {3} [built]
  [216] ./~/react/lib/ReactDefaultPerf.js 9.07 kB {3} [built]
  [217] ./~/react/lib/ReactDefaultPerfAnalysis.js 5.79 kB {3} [built]
  [218] ./~/react/~/fbjs/lib/performanceNow.js 872 bytes {3} [built]
  [219] ./~/react/~/fbjs/lib/performance.js 612 bytes {3} [built]
  [220] ./~/react/lib/ReactVersion.js 379 bytes {3} [built]
  [221] ./~/react/lib/renderSubtreeIntoContainer.js 463 bytes {3} [built]
  [222] ./~/react/lib/ReactDOMServer.js 766 bytes {3} [built]
  [223] ./~/react/lib/ReactServerRendering.js 3.3 kB {3} [built]
  [224] ./~/react/lib/ReactServerBatchingStrategy.js 687 bytes {3} [built]
  [225] ./~/react/lib/ReactServerRenderingTransaction.js 2.36 kB {3} [built]
  [226] ./~/react/lib/ReactIsomorphic.js 2.06 kB {3} [built]
  [227] ./~/react/lib/ReactDOMFactories.js 3.36 kB {3} [built]
  [228] ./~/react/lib/ReactElementValidator.js 11.2 kB {3} [built]
  [229] ./~/react/~/fbjs/lib/mapObject.js 1.47 kB {3} [built]
  [230] ./~/react/lib/onlyChild.js 1.21 kB {3} [built]
  [231] ./~/react/lib/deprecated.js 1.77 kB {3} [built]
  [232] ./~/react-dom/index.js 62 bytes {3} [built]
webpack: bundle is now VALID.

Visit the following URLs and they should render correctly:

http://localhost:8888/n/react/main.html
http://localhost:8888/n/react/dist/about.html
http://localhost:8888/n/react/dist/contact.html

Visit http://localhost:3000 and you should see all webpack build results.


Preset not found issue (Obsolete)
---------------------------------

For issue like "can't find preset" e.g. Module build failed: Error: Couldn't find preset "react" relative to directory "../GoogleAppEngine/war/n/ng", c.f. http://stackoverflow.com/questions/33711512/symlinking-react-modules-with-npm-link-for-local-development-gives-error
- apply both resolve fallback and resolveLoader accordingly!

Good luck!

http://monicalent.com/blog/2015/08/03/converting-angular-js-app-from-require-js-to-webpack/
http://stackoverflow.com/questions/33711512/symlinking-react-modules-with-npm-link-for-local-development-gives-error
https://www.twilio.com/blog/2015/08/setting-up-react-for-es6-with-webpack-and-babel-2.html
https://webpack.github.io/docs/tutorials/getting-started/