define(['scripts/lib/template',"scripts/echo"], function(tpl,echo) {

	function NavIscroll(opts) {
		this.opts = $.extend({}, NavIscroll.DEFAULTS, opts);
		this.myScroll = undefined;
		this.domSel = this.opts.domSel;
		this.$dom=$(this.domSel);
		this.scrollSel=this.opts.scrollSel;
		this.init();
	}


	NavIscroll.DEFAULTS = {
		domSel : '#nav',
		scrollSel:'#wrapper'
	};
	NavIscroll.prototype = {

		init : function() {
		    var _this = this;
		    _this.getFontSize();
           	_this.render();
			_this.bindEvt();
		},
        render:function(){
            var _this=this;
            var gid=api.pageParam.cid;
            var id="custome_recom2"+"_"+gid;
			var folder = "cartype";
			var url=td.domain+"/customer/recom/"+gid+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
		                 _this.$dom.html(tpl("tpl-nav", {data:finalData}));
		                 if(api.pageParam.page=="finaceCustomeService"){
		                    var totalWidth=0;
		                    for(var i=0;i<finalData.length;i++){
		                    	totalWidth+=(finalData[i].name.length*0.7+0.6)*_this.fontSize;
		                    }
			            	$(".iscroll-scroller").css("width",totalWidth);
			            	$("#title").html("理财顾问推荐");
			             }
			             else{
			             	var totalWidth=0;
			             	for(var i=0;i<finalData.length;i++){
		                    	totalWidth+=(finalData[i].name.length*0.7+0.6)*_this.fontSize;
		                    }
			            	$(".iscroll-scroller").css("width",totalWidth);
			             	$("#title").html("客服推荐");
			             }
		             	 _this.loaded();
		            }
		            _this.$dom.show();
                }
                else{
                	//toast(finalData);
                	_this.$dom.hide();
                }
			});
        },
        getFontSize:function(){
        	var _this=this;
        	var fontSize = window.getComputedStyle(document.documentElement).fontSize;
		    _this.fontSize = fontSize.substring(0, fontSize.length - 2);
        },
		loaded : function() {
			var _this = this;
			try {
				_this.myScroll = new IScroll(_this.scrollSel, {
					eventPassthrough : true,
					scrollX : true,
					scrollY : false,
					preventDefault : false,
					snap : "li"
				});
				_this.myScroll.on("scrollEnd",function(){
					echo.init({
						offset : 100,//可是区域多少像素可以被加载
						throttle : 0 //设置图片延迟加载的时间
					});
				});
				
			} catch (e) {
				alert(e)
			}
		},
		bindEvt : function() {
			var _this = this;
            
			_this.$dom.on("click", ".custom-link", function() {
			   var id=$(this).attr("id");
				api.openWin({
					name : 'customeServiceDetail',
					url : 'customeServiceDetail.html',
					bounces : false,
					animation : 'movein',
					scrollToTop : true,
					pageParam : {id:id},
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					}
				});
	
			});
			//$("#nav").on("click", "#scroller li", function(e) {
			   // $(this).addClass("active");
//				_this.openFrame(e.target);
//				var scrollLi=$("#scroller").find("li");
//				if (ret.index >= 4) {
//					_this.myScroll.scrollTo(-scrollLi.width() * (ret.index - 4), 0, 1000);
//				}
//				scrollLi.removeClass('nav_active').eq(ret.index).addClass('nav_active');
			//});

		},
		lazyImg:function(){
			echo.init({
				offset : 100,//可是区域多少像素可以被加载
				throttle : 0 //设置图片延迟加载的时间
			});
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'bottom'
		    });
		},
	};

	return {
		NavIscroll : NavIscroll
	};
}); 