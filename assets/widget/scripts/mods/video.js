$(function() {
    $api.setStorage("noFullScreen", true);//android 后退键的需要，具体见 scripts/app/newsDetail.js
    var $fullscreenBtn, $normalscreenBtn;
    var ortPlayer = {};

    ortPlayer = {
        _isAndroid : /Android/i.test(navigator.userAgent),
        _isIphone : /iphone/i.test(navigator.userAgent),
        _isWeixin : /micromessenger/i.test(navigator.userAgent),
        _orientation : Math.abs(window.orientation),
        isFullScreen : !1,
        isActiveBehavior : !1,
        init : function(t) {
            this._initDom();
            this._initEvent();
            this.fullScreenObj = this.createFullScreenObj($("video")[0]);
            this.getFontSize();
        },
        _initDom : function() {
            this._videoWrapper = $("#videoWrap");
        },
        _initEvent : function() {
            var _this=this;
            function resize() {
                var ort = Math.abs(window.orientation);
                if (_this._orientation != ort) {
                    _this._orientation = ort
                    if((_this._isAndroid || _this._isIphone) && (!_this.isActiveBehavior || !_this.isFullScreen)){
                        var ischangeFS = 90 == ort;
                        _this._changeFullScreen(ischangeFS);
                        return;
                    }
                }
                _this._reSize();
            }
            var timer;
            $(window).on("resize", function() {
                clearTimeout(timer);
                timer = setTimeout(resize, 500);
            });
        },
        getStyle : function(obj, attr) {
            return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj)[attr];
        },
        getFontSize : function() {
            var _this = this;
            var fontSize = _this.getStyle(document.documentElement, "fontSize");
            _this.fontSize = fontSize.substring(0, fontSize.length - 2);
        },
        _getSize : function() {
            var _this = this;

            return {
                height : _this.fontSize * 10
            }

        },
        _changeFullScreen : function(ischangeFS) {
            //this._isAndroid||(this._isIphone&&this.isWeixin)
            if (this._isAndroid || (this._isIphone)) {

                if (ischangeFS) {
                    if (this._isAndroid) {
                        if ($fullscreenBtn && $fullscreenBtn.length > 0) {

                            $fullscreenBtn.hide();
                            $normalscreenBtn.show();
                        }
                    }
                    document.addEventListener("touchmove", this.preventDefault, !1);
                    $("#videoWrap").addClass("hv_fullscreen");
                    this.changeFullScreen(true, false);
                    this.onWindowResize();

                } else {
                    if ($fullscreenBtn && $fullscreenBtn.length > 0) {

                        $fullscreenBtn.show();
                        $normalscreenBtn.hide();

                    }
                    $("#videoWrap").removeClass("hv_fullscreen");
                    this.changeFullScreen(false, false);
                    document.removeEventListener("touchmove", this.preventDefault, !1);
                    try {
                        this.fullScreenObj.cancelEl[this.fullScreenObj.cancelFn]();
                    } catch (ee) {
                    }
                }

            }
//          else if (this._isIphone) {
//              if (ischangeFS) {
//                  try {
//                      if ($fullscreenBtn && $fullscreenBtn.length > 0) {
//                          $fullscreenBtn.hide();
//                          $normalscreenBtn.show();
//                      }
//                      this.fullScreenObj.requestEl[this.fullScreenObj.requestFn]();
//
//                  } catch (ee) {
//                  }
//              }
//
//          }
        },
        createFullScreenObj : function(e) {
            for (var t, a, i, n, o = ["webkit", "moz"], r = 0, s = o.length; s > r; r++) {
                var d = o[r] + "RequestFullScreen", l = o[r] + "CancelFullScreen";
                "function" == typeof e[d] && ( t = d, i = e), "function" == typeof document[l] && ( a = l, n = document)
            }
            return t || "function" != typeof e.webkitEnterFullScreen || ( t = "webkitEnterFullScreen", i = e), a || "function" != typeof e.webkitExitFullScreen || ( a = "webkitExitFullScreen", n = e), {
                requestFn : t,
                requestEl : i,
                cancelFn : a,
                cancelEl : n
            }
        },
        onWindowResize : function() {
            var width = window.innerWidth, height = window.innerHeight;
            this._videoWrapper.height(height);
        },
        _reSize : function() {
            var sizeObj = this._getSize();
            this._videoWrapper.height(sizeObj.height + "px");
        },
        preventDefault : function(e) {
            e.preventDefault();
        },
        changeFullScreen : function(t, e) {
            this.isFullScreen = t, this.isActiveBehavior = e;
            var i = $("body");
            $(".main");
            t ? (i.addClass("fullScreen"), setTimeout(function() {
                0 != document.body.scrollTop && (window.scrollTo(0, 0), setTimeout(arguments.callee, 300))
            }, 0)) : i.removeClass("fullScreen"), this._reSize();
        }
    };
    //全屏按钮的处理
    var headerH = api.pageParam.headerH;
    api.addEventListener({
        name : 'changeFullScreen'
    }, function(ret, err) {
        if (ret.value.src == "keyback") {
            api.setFrameAttr({
                name : 'newsDetail_frm',
                rect : {
                    x : 0,
                    y : headerH,
                    w : 'auto',
                    h : 'auto'
                }
            });
            api.setScreenOrientation({
                orientation : 'auto_portrait'
            });
            ortPlayer._changeFullScreen(false);
            $api.setStorage("noFullScreen", true);
        }
    });
    $(document).on("click", "#fakeFullBtn", function(e) {
        e.stopPropagation();
        if ($fullscreenBtn && $fullscreenBtn.length > 0) {
        } else {
            $fullscreenBtn = $("#videoWrap").find(".controlbar-btns-fullscreen");
            $normalscreenBtn = $("#videoWrap").find(".controlbar-btns-normalscreen");
        }

        if (!ortPlayer.isFullScreen) {
            api.setFrameAttr({
                name : 'newsDetail_frm',
                rect : {
                    x : 0,
                    y : 0,
                    w : 'auto',
                    h : 'auto'
                }
            });
            api.setScreenOrientation({
                orientation : 'auto_landscape'
            });
            ortPlayer._changeFullScreen(true);
            $api.setStorage("noFullScreen", false);
        } else {
            api.setFrameAttr({
                name : 'newsDetail_frm',
                rect : {
                    x : 0,
                    y : headerH,
                    w : 'auto',
                    h : 'auto'
                }
            });

            api.setScreenOrientation({
                orientation : 'auto_portrait'
            });

            ortPlayer._changeFullScreen(false);
            $api.setStorage("noFullScreen", true);  
        }
    });
    //初始化视频模块
    ortPlayer.init();
});
