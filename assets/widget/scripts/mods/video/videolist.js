define(["scripts/lib/template","scripts/echo"],function(tpl,echo){

   function videolist(opts) {
       this.opts = $.extend({}, videolist.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
	   this.curPage=1;
	   this.pageNum=1;
	   this.finalData=[];
       this.init();
   }
   videolist.DEFAULTS = {
		domSel : '#videolist'
	};
   videolist.prototype = {
        init:function(){
           var _this = this;
           _this.render();
        	_this.bindEvt();
        	_this.setRefreshHeader();
        	_this.setInfiniteScroll();
        },
        render:function(){
            var _this=this;
            var cid=api.pageParam.cid;
            var id="videolist2"+"_"+cid;
			var folder = "cartype";
			var url=td.domain+"/video/"+cid+"/"+1+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			//alert("url"+url);
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
			    //alert("finalData"+JSON.stringify(finalData));
				if(data.code==200){
                    if(finalData.length>0){
		                 _this.$dom.html(tpl("tpl", {data:finalData}));
		                 _this.curPage=1;
		                 _this.pageNum=data.pageinfo.pagenum;
		                 _this.lazyImg();
		            }
                }
                else{
                	toast(finalData);
                }
			});
            
        },
        appendRender:function(){
            var _this=this;
            var cid=api.pageParam.cid;
            var id="videolist_"+cid+"_"+_this.curPage;
			var folder = "cartype";
			var url=td.domain+"/video/"+cid+"/"+_this.curPage+"?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
		                _this.$dom.append(tpl("tpl", {data:finalData}));
                 		_this.lazyImg();
		            }
                }
                else{
                	toast(finalData);
                }
			});
            
//          var finalData2=data.data;
//          if(finalData2.length>0){
//               _this.$dom.append(tpl("news/news", {data:finalData2}));
//               _this.lazyImg();
//          }
        },
        bindEvt : function() {
			var _this = this;
			_this.$dom.on("click","li",function(){
				var aid=$(this).data("aid");
			    var cid=$(this).data("cid");
				api.openWin({
	              name: 'newsDetail_win',
	              url: '../common/common-header-level2.html',
	              slidBackEnabled:false,
	              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
	            });
			});
			
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'middle'
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
				api.refreshHeaderLoadDone();
			});
		},
		setInfiniteScroll:function(){
		    var _this=this;
		    //if(_this.pageNum>1){
        		api.addEventListener({
					name : 'scrolltobottom'
				}, function(ret, err) {
				   if(_this.curPage>=_this.pageNum){
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
		}
   }
   return {
		videolist : videolist
	};
   

});