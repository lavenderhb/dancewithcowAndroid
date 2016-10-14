define(["scripts/lib/template"],function(tpl){

   function personDetail(opts) {
       this.opts = $.extend({}, personDetail.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
       this.init();
   }
   personDetail.DEFAULTS = {
		domSel : '#personDetail'
	};
   personDetail.prototype = {
        init:function(){
           var _this = this;
           _this.render();
        },
        
        render:function(){
            var _this=this;
            if(api.connectionType=="none"){
	       	   _this.toast("请检查网络连接");
	           return;
	       }
           _this.getAjaxData(_this.callBack,_this.$dom);
            
        },
        callBack:function(finalData,$dom){
            $dom.html(tpl("tpl", finalData));
        },
        getAjaxData:function(callback,$dom){
            var _this=this;
            //var finalData=[];
            var id=api.pageParam.id;
//      	$.ajax({
//	             type: "GET",
//	             url: td.domain+"/customer/desc/"+id+"?token="+td.token,
//	             async: false, 
//	             success: function(data){
//                  var data = JSON.parse(data); 
//                  if(data.code==200){
//                  	finalData=data.data;
//                  }
//               },
//	              error:function(err){
//	              	 _this.toast("网络异常，请稍后重试");
//	              }
//	         });
//	         return finalData;
	        api.ajax({
                url: td.domain+"/customer/desc/"+id+"?token="+td.token,
                method: "GET"
            },function(ret, err){
                if (ret) {
                    if(ret.code==200){
                        callback(ret.data,$dom);
                    }
                } else {
                    _this.toast("网络异常，请稍后重试");
                }
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
		personDetail : personDetail
	};
   

});