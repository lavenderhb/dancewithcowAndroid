define(["scripts/lib/template"],function(tpl){

   function newsDetail(opts) {
       this.opts = $.extend({}, newsDetail.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
       this.init();
   }
   newsDetail.DEFAULTS = {
		domSel : '#newsDetail'
	};
   newsDetail.prototype = {
        init:function(){
           var _this = this;
           _this.render();
        	_this.bindEvt();
        },
        render:function(){
            var _this=this;
            var cid=api.pageParam.cid;
		    var aid=api.pageParam.aid;
		    
            _this.getAjaxData(cid,aid,_this.callBack,_this.$dom); 
        },
        callBack:function(finalData,$dom){
             var _this=this;
             finalData.systemType=api.systemType;
             tpl.config("escape", false);
             $dom.html(tpl("tpl", finalData));
             var fontSize=20;
	         function getStyle(obj, attr) {
                return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj)[attr];
       		 }
	         function getFontSize() {
                fontSize = getStyle(document.documentElement, "fontSize");
                fontSize = fontSize.substring(0, fontSize.length - 2);
	         } 
             if(finalData.video&&finalData.video!=""){
                if(finalData.wtype=="app"){
//                  if(api.systemType=="ios"){
//                      getFontSize();
//                      var hgt=api.pageParam.headerH+fontSize*4.25;
//                      var progressY=hgt+200-40;
//                      var winHgt=api.winHeight;
//  					var winWdt=api.winWidth;
//                      api.openFrame({
//							name : 'native_video',
//							url : 'native_video.html',
//							rect : {
//								x : 0,
//								y : hgt,
//								w : winWdt,
//								h : 200
//							},
//							showProgress : 'true',
//							pageParam:{videourl:finalData.video,progressY:progressY,videoY:hgt}
//						});
//                  }
//                  else{
                    	SewisePlayer.setup({
	                        server : "vod",
	                        type : finalData.vtype,
	                        autostart : "false",
	                        skin : "vodTransparent",
	                        videourl : finalData.video,
	                        lang : 'zh_CN',
	                        poster:finalData.cover,
	                        topbardisplay:"disable"
	                    },"videoWrap");
//	                    var head= document.getElementsByTagName('head')[0];
//	                    var script= document.createElement('script');   
//	                    script.type= 'text/javascript';   
//	                    script.src= '../../scripts/mods/video.js';   
//	                    head.appendChild(script);
                   // }
                }
                else{
                   api.showProgress({
                        style: 'default',
                        animationType: 'fade',
                        title: '努力加载中...',
                        modal: false
                    });
//                 var fontSize=20;
//                 function getStyle(obj, attr) {
//                      return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj)[attr];
//                 }
//                 function getFontSize() {
//                      fontSize = getStyle(document.documentElement, "fontSize");
//                      fontSize = fontSize.substring(0, fontSize.length - 2);
//                 } 
                   var $iframe = $(document.createElement("IFRAME"));  
                    $iframe.attr("frameborder", 0);  
                    $iframe.attr("src", finalData.video); 
                    $iframe.css("width", "100%"); 
                    getFontSize();
                    var hgt=api.winHeight-fontSize * 8;
                    $iframe.css("height", hgt);  
                    $iframe.prependTo($(".text"));
                    $iframe[0].onload=function(){
                        api.hideProgress();
                    }
                }
             }
             //"http://liveproxy.kukuplay.com:9222/mweb/fytv-letv/jsws.m3u8",
        },
        getAjaxData:function(cid,aid,callBack,$dom){
            var _this=this;
            api.showProgress();
		    api.ajax({
                url: td.domain+"/article/"+cid+"/"+aid+"?token="+td.token,
                method: "GET"
            },function(ret, err){
                if (ret) {
                     if(ret.code==200){
                        //alert(JSON.stringify(ret));
                        api.hideProgress();
                        callBack(ret.data,$dom);
                     } 
                     
                } else {
                    api.hideProgress();
                    _this.toast("网络异常，请稍后重试");
                }
            });
		},
        bindEvt : function() {
//			var _this = this;
//			_this.$dom.on("click","li",function(){
//			    var aid=$(this).data("aid");
//			    var cid=$(this).data("cid");
//				api.openWin({
//					name : 'newsDetail',
//					url : '../news/newsDetail.html',
//					bounces : false,
//					delay : 200,
//					pageParam:{aid:aid,cid:cid}
//				});
//			});
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'bottom'
		    });
		}
   }
   return {
		newsDetail : newsDetail
	};
   

});