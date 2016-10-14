apiready = function() {
	$(function() {
		var msgObj = new msg({domSel:"#newsList"});
	});
};
function msg(opts) {
	this.opts = $.extend({}, msg.DEFAULTS, opts);
	this.domSel = this.opts.domSel;
	this.$dom = $(this.domSel);
	this.currIndex = 0;
	this.prevIndex = 0;
	this.curPage = 1;
	this.pageNum = 1;
	this.init();
}

msg.DEFAULTS = {
	domSel : '#orderList'
};
msg.prototype = {
	init : function() {
		var _this = this;
		_this.render();
		_this.bindEvt();
		_this.setRefreshHeader();
		_this.setInfiniteScroll();
	},
	render : function(isPrepend) {
		var _this = this;
		var cid=api.pageParam.cid;
		var id = "gaoshou_" + cid;
		var folder = "cartype";
		var url = td.domain + "/superior/"+cid + "/1?token=" + td.token;
		var method = "get";
		var params = "";
		var async = "true";
		doCache(folder, id, url, method, params, async, function(data) {
			//alert(JSON.stringify(data));
			var finalData = data.data;
			if (data.code == 200) {
				if (finalData.length > 0) {
					var html = template('tpl', {data:finalData});
					_this.$dom.html(html);
					_this.curPage = 1;
					_this.pageNum = data.pageinfo.pagenum;
					_this.lazyImg();
				}
			} else {
				toast(data.data);
			}
		});
	},
	appendRender : function() {
		var _this = this;
		var cid=api.pageParam.cid;
		var id = "gaoshou_" + cid + "_" + _this.curPage;
		var folder = "cartype";
		var url = td.domain + "/superior/"+ cid+ "/" + _this.curPage + "?token=" + td.token;
		var method = "get";
		var params = "";
		var async = "true";
		doCache(folder, id, url, method, params, async, function(data) {
			//alert(JSON.stringify(data));
			var finalData = data.data;
			if (data.code == 200) {
				if (finalData.length > 0) {
					var html = template('tpl', {data:finalData});
					_this.$dom.append(html);
					_this.lazyImg();
				}
			} else {
				toast(finalData);
			}
		});
	},
	bindEvt : function() {
		var _this = this;
	      _this.$dom.on("click",".item-link",function(){
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
	setRefreshHeader : function() {
		var _this = this;
		api.setRefreshHeaderInfo({
			visible : true,
			bgColor : '#fff',
			textColor : '#666',
			textDown : '下拉刷新...',
			textUp : '松开刷新...',
			textLoading:"努力加载中",
			showTime : false
		}, function(ret, err) {
			_this.render();
			api.refreshHeaderLoadDone();
		});
	},
	setInfiniteScroll : function() {
		var _this = this;
		api.addEventListener({
			name : 'scrolltobottom'
		}, function(ret, err) {
			if (_this.curPage >= _this.pageNum) {
				toast("已到最底部");
			} else {
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