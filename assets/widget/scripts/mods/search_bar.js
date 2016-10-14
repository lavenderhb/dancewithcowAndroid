define(function(){

   function search_bar(opts) {
       this.opts = $.extend({}, search_bar.DEFAULTS, opts);
	   this.domSel = this.opts.domSel;
	   this.$dom=$(this.domSel);
	   this.haveBack=this.opts.haveBack;
       this.init();
   }
   search_bar.DEFAULTS = {
		domSel : '#search',
		haveBack:true
	};
   search_bar.prototype = {
        init:function(){
           var _this = this;
        	_this.bindEvt();
        },
        bindEvt : function() {
			var _this = this;
			var $close=_this.$dom.siblings(".close-input");
		    var $searchInput=_this.$dom;
		    var $back=undefined;
		    if(_this.haveBack){
		    	$back=$("#back");
		    }
		   $searchInput.on("focus",function(){
		     
		   	   var val = $.trim($(this).val());
				if (val=="") {
					$close.hide();
				}
				else{
					$close.show();
				}
		   });
		   $searchInput.on("keyup",function(){
		   	   var val = $.trim($(this).val());
				if (val=="") {
					$close.hide();
				}
				else{
					$close.show();
				}
		   });
//		   $searchInput.on("blur",function(){
//			    $close.hide();
//		   });
		   $close.on("click", function() {
				$searchInput.val("");
				$(this).hide();
				$searchInput.focus();
			});
		   if(_this.haveBack){
		    	 $back.on("click", function() {
					api.closeWin();
				});
		    }
		}
   }
   return {
		search_bar : search_bar
	};
   

});