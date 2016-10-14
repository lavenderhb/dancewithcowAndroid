define(['scripts/lib/template',"scripts/echo"],function(tpl,echo){

   function customeService(opts) {
       this.opts = $.extend({}, customeService.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
	   this.curPage=1;
	   this.pageNum=1;
       this.init();
   }
   customeService.DEFAULTS = {
		domSel : '#customList'
	};
   customeService.prototype = {
        init:function(){
           var _this = this;
           _this.render();
        	_this.bindEvt();
        	 _this.setInfiniteScroll();
        },
        
        render:function(){
            var _this=this;
            var gid=api.pageParam.cid;
            var id="custome_customer2"+"_"+gid;
			var folder = "cartype";
			var url=td.domain+"/customer/"+gid+"/"+1+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
		                 _this.$dom.html(tpl("tpl-customList", {data:finalData}));
		                 _this.curPage=1;
		                 _this.pageNum=data.pageinfo.pagenum;
		                 _this.lazyImg();
		            }
                }
                else{
                	//toast(finalData);
                }
			});
        },
        appendRender:function(){
            var _this=this;
            var gid=api.pageParam.cid;
            var id="custome_customer2"+"_"+gid+"_"+_this.curPage;
			var folder = "cartype";
			var url=td.domain+"/customer/"+gid+"/"+_this.curPage+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
		                 _this.$dom.append(tpl("tpl-customList", {data:finalData}));
                 		 _this.lazyImg();	
		            }
                }
                else{
                	//toast(finalData);
                }
			});
            
//          var finalData=data.data;
//          if(finalData.length>0){
//               _this.$dom.append(tpl("customeService/customeService", {data:finalData}));
//               _this.lazyImg();	
//          }
        },
        bindEvt : function() {
			var _this = this;
			_this.$dom.on("click",".custom-link",function(){
			    var id=$(this).attr("id");
			    var page=api.pageParam.page;
				api.openWin({
					name : 'customeServiceDetail',
					url : 'customeServiceDetail.html',
					bounces : false,
					animation : 'movein',
					scrollToTop : true,
					pageParam : {id:id,page:page},
					slidBackEnabled:false,
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					}
				});
			});
			
		},
		setInfiniteScroll:function(){
		    var _this=this;
		    //if(_this.pageNum>1){
        		api.addEventListener({
					name : 'scrolltobottom'
				}, function(ret, err) {
				   if(_this.curPage>=parseInt(_this.pageNum)){
				        _this.toast("已到最底部");
//				   		api.removeEventListener({
//						    name: 'scrolltobottom'
//						});
				   }
				   else{
				   		_this.curPage++;
						_this.appendRender();
				   }
				});
        	//}
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
   }
   return {
		customeService : customeService
	};
   

});