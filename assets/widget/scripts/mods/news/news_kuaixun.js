define(["scripts/lib/template","scripts/echo"],function(tpl,echo){

   function news_rec(opts) {
       this.opts = $.extend({}, news_rec.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
       this.init();
   }
   news_rec.DEFAULTS = {
		domSel : '#instant-news'
	};
   news_rec.prototype = {
        init:function(){
           var _this = this;
           _this.render();
           _this.lazyImg();
           //_this.bindEvt();
        	_this.setRefreshHeader();
        },
        render:function(){
            var _this=this;
            var cid=api.pageParam.cid;
            var id="newsflash2";
			var folder = "cartype";
			var url=td.domain+"/newsflash/"+cid+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
		                 _this.$dom.html(tpl("tpl", {data:finalData}));
		            }
                }
                else{
                	toast(data.data);
                }
			});
        },
        bindEvt : function() {
			var _this = this;
			_this.$dom.on("click",".rec-news ",function(e){
			    e.preventDefault();
				var aid=$(this).data("aid");
			    var cid=$(this).data("cid");
				api.openWin({
	              name: 'newsDetail_win',
	              url: '../common/common-header-level2.html',
	              slidBackEnabled:false,
	              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
	            });
			});
//			var fontSize = window.getComputedStyle(document.documentElement).fontSize;
//				fontSize = fontSize.substring(0, fontSize.length - 2);
//			_this.diff=fontSize*1.275;
//			_this.currIndex=0;
//			_this.prevIndex=-1;
//			$(window).on("scroll",function(){
//				setTimeout(function(){
//					_this.doScroll();
//				},300);
//			});
		},
		doScroll:function(){
		   var _this=this;
			$(".newsList").each(function(){
		       try{
		       		var scrollTop=document.body.scrollTop;
					if(scrollTop<$(this)[0].offsetHeight+$(this)[0].offsetTop-_this.diff){
					    _this.currIndex=$(this).index();
					    if(_this.prevIndex!=_this.currIndex){
					        $(".timeStamp").removeClass("fixed");
					    	$(this).find(".timeStamp").addClass("fixed");
							_this.prevIndex=_this.currIndex;
					    }
					    if(document.body.scrollTop<=0){
					    	$(this).find(".timeStamp").removeClass("fixed");
					    	_this.prevIndex=-1;
					    }
					    return false; 
					}
		       
		       }catch(err){
		       		_this.toast("滑动异常");
		       }
			});
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'bottom'
		    });
		},
		setRefreshHeader:function() {
		    var _this = this;
			api.setRefreshHeaderInfo({
				visible : true,
				bgColor : '#ccc',
				textColor : '#fff',
				textDown : '下拉刷新...',
				textUp : '松开刷新...',
				showTime : true
			}, function(ret, err) {
				_this.render();
				_this.lazyImg();
				api.refreshHeaderLoadDone();
			});
		},
		lazyImg:function(){
			echo.init({
				offset : 100,//可是区域多少像素可以被加载
				throttle : 0 //设置图片延迟加载的时间
			});
		}
   }
   return {
		news_rec : news_rec
	};
   

});