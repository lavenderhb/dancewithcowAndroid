define(['scripts/lib/template'], function(tpl) {
	function slider_img(opts) {
		this.opts = $.extend({}, slider_img.DEFAULTS, opts);
		this.domSel = this.opts.domSel;
		this.$dom =null;
		this.init();
	}
	slider_img.DEFAULTS = {
		domSel:"#slider_img"//导航id
	};
	slider_img.prototype = {
		init : function() {
			var _this = this;
			_this.renderTpl();
			if(api.systemType=="ios"){
				_this.bindEvt();
			}
		},
		renderTpl : function(type) {
			var _this = this;
			_this.$dom = $(_this.domSel);
			if(_this.currSwiper){
				_this.currSwiper.destroy(true,true);
			}
			var id="main_slide";
			var folder = "cartype";
			var url=td.domain+"/slide/0/5?token="+td.token;
			var method="get";
			var params="";
			var async=true;
			doCache(folder,id,url,method,params,async,function(data){
				if(data.code==200){
				   _this.swipeData=data.data;
				   var data={data:_this.swipeData};
				   if(_this.swipeData.length>0){
		               if(api.systemType=="ios"){
		               	   _this.$dom.html(tpl("tpl-slide", data));
		               	   if(_this.swipeData.length&&_this.swipeData.length>1){
							  _this.currSwiper=_this.$dom.swiper({loop:true});
						   }
		               }
		               else{
		               	   _this.openScrollPicture(_this.swipeData);
		               }
					}
				}
			});
		},
		bindEvt : function() {
			var _this = this;
			_this.$dom.on("click", "a", function(e) {
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
		openNewsDetail:function(index){
		    var _this=this;
		    var currData=_this.swipeData[index];
		    var aid=currData.aid;
		    var cid=currData.cid;
			api.openWin({
              name: 'newsDetail_win',
              url: '../common/common-header-level2.html',
              slidBackEnabled:false,
              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
            });
		},
		getFrameYAndH:function(){
		    var _this=this;
			var fontSize = window.getComputedStyle(document.documentElement).fontSize;
			fontSize = fontSize.substring(0, fontSize.length - 2);
			_this.frameHgt=fontSize*5;
		},
		openScrollPicture:function(data){
		    var _this=this;
		    _this.getFrameYAndH();
		    var imgArr=[];
		    for(var i=0;i<data.length;i++){
		    	imgArr.push(data[i].image);
		    }
		    if(_this.UIScrollPicture){
		    	_this.UIScrollPicture.reloadData({
				    data: {
				        paths: imgArr
				    }
				});
		    }
		    else{
				_this.UIScrollPicture = api.require('UIScrollPicture');
				_this.UIScrollPicture.open({
				    rect: {
				        x: 0,
				        y: 0,
				        w: api.winWidth,
				        h:_this.frameHgt
				    },
				    data: {
				        paths: imgArr
				    },
				    styles: {
				        indicator: {
				            align: 'center',
				            color: '#87bae3',
				            activeColor: '#ef4b49'
				        }
				    },
				    placeholderImg: 'widget://images/lazyImg.jpg',
				    contentMode: 'scaleToFill',
				    interval: 3,
				    fixedOn: 'main',
				    auto:false,
				    loop: true,
				    fixed: false
				}, function(ret, err){
				    if( ret ){
				         if(ret.eventType=="click"){
				         	var index=ret.index;
				            _this.openNewsDetail(index);
				         }
				    }else{
				        // alert( JSON.stringify( err ) );
				    }
				});
			}
		}
	};

	return {
		slider_img : slider_img
	};

});
