define(["scripts/lib/template"],function(tpl){

   function newsList(opts) {
       this.opts = $.extend({}, newsList.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
       this.init();
   }
   newsList.DEFAULTS = {
		domSel : '#newsList'
	};
   newsList.prototype = {
        init:function(){
           var _this = this;
           _this.render();
        	_this.bindEvt();
        },
        render:function(){
            var _this=this;
            var id="main_artlist";
			var folder = "cartype";
			var url=td.domain+"/artlist/0/5?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
				var finalData=data.data;
				if(data.code==200){
                	if(finalData.length>0){
		                 _this.$dom.html(tpl("tpl-newslist", {data:finalData}));
		            }
                }
                else{
                	_this.toast(finalData);
                }
			
			});
            
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
		        location: 'bottom'
		    });
		}
   }
   return {
		newsList : newsList
	};
   

});