define(['scripts/lib/template'], function(tpl) {

	function NavIscroll(opts) {
		this.opts = $.extend({}, NavIscroll.DEFAULTS, opts);
		this.myScroll = undefined;
		this.domSel = this.opts.domSel;
		this.menuOpened = false;
		this.frames = [];
		this.cid=this.opts.cid?this.opts.cid:-100;
		this.subCid=this.opts.subCid?this.opts.subCid:-100;
		this.firstEnterData=[{	updatedAt : new Date(), id : 1111 ,imgs : '',title : '首次登入，请联网请求数据!!!'}] ;
		this.init();
	}


	NavIscroll.DEFAULTS = {
		domSel : '#wrapper'
	};
	NavIscroll.prototype = {

		init : function() {
			this.bindEvt();
			this.initData();
		},

		loaded : function() {
			var _this = this;
			try {
				_this.myScroll = new IScroll(_this.domSel, {
					eventPassthrough : true,
					scrollX : true,
					scrollY : false,
					preventDefault : false
				});
			} catch (e) {
			    //alert(222+e);
			}
		},
		bindEvt : function() {
			var _this = this;
            var $dom=$("#nav"+_this.cid);
			$('#nav').on("click", ".arrow_down", function() {
				_this.openNav();
			});
			$dom.on("click", "li", function(e) {
				_this.openFrame(e.target);
			});

		},
		initData:function(){
		    var _this=this;
		    var cid=_this.cid
			var id="news_twomenu3"+"_"+cid;
			var folder = "cartype";
			var url=td.domain+"/news/twomenu/"+cid+"?token="+td.token;
			var method="get";
			var params="";
			var async=true;
			var notShowProgress=true;
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                	_this.renderTpl(finalData);
				}
				else{
					_this.toast(finalData);
				}
			},notShowProgress);
		},
	    renderTpl:function(ret){
	        if(ret.length==0) return;
	        var _this=this;
			try {
				var navHtml = '', data = {}, frames = [];
                var haveSubCid=false;
				var recData={"cid":"-1","title":"推荐"};
				var newArr=[];
				var displayIndex=0;
				//newArr.push(recData);
				for(var j=0;j<ret.length;j++){
				    ret[j].subNav=1;
					newArr.push(ret[j]);
				}
				//alert(_this.cid);
				if(_this.subCid!==-100){
					haveSubCid=true;
					//alert(4);
				}
				for (var i=0;i<newArr.length;i++) {
				    var frame = {};
					frame.name = 'frame_' +_this.cid+"_"+i;
					if(newArr[i].type=="Video"){
						frame.url = 'html/video/videolist.html';
					}
					else
					{
						frame.url = 'html/news/news.html';
					}
					if(haveSubCid){
						if(newArr[i].cid==_this.subCid){
							displayIndex=i;
						}
					}
					frame.pageParam = {
						cid : newArr[i].cid,
						index : i
					};
					frames.push(frame);
				}
				data = {
					data : newArr
				};
				//navHtml = tpl('news/nav-iscroll', data);
				//$("#nav").html(navHtml);
				if(_this.cid=="452"){
					navHtml=tpl("tpl452",data);
				    $("#nav452").html(navHtml);
				    $("#nav487").find("li").removeClass('nav_active').eq(displayIndex).addClass('nav_active');
				    $("#nav452").show();
				    if(newArr.length>4){
				        var widthStr=newArr.length*25+"%";
				        var liWidthStr=((1/newArr.length)*100).toFixed(2)+"%";
				    	$("#scroller452").css({width:widthStr});
				    	$("#scroller452").find("li").css({width:liWidthStr});
				    }
				    else{
				    	$("#scroller452").css({width:"100%"});
				    	$("#scroller452").find("li").css({width:"25%"});
				    }
				}
				else{
					navHtml=tpl("tpl487",data);
				    $("#nav487").html(navHtml);
				    $("#nav487").find("li").removeClass('nav_active').eq(displayIndex).addClass('nav_active');
				    $("#nav487").show();
				    if(newArr.length>4){
				        var widthStr=newArr.length*25+"%";
				        var liWidthStr=((1/newArr.length)*100).toFixed(2)+"%";
				    	$("#scroller487").css({width:widthStr});
				    	$("#scroller487").find("li").css({width:liWidthStr});
				    }
				    else{
				    	$("#scroller487").css({width:"100%"});
				    	$("#scroller487").find("li").css({width:"25%"});
				    }
				}
			    
				api.parseTapmode();
				this.loaded();
				this.openFrameGroup(frames,displayIndex);
				
			} catch (e) {
				_this.toast("系统异常，请稍后重试");
			}
			
		},
		openFrameGroup : function(frames,displayIndex) {
			var _this = this;
			
			var width = api.winWidth;
			var cid=_this.cid;
			var $dom=$("#nav"+cid);
		    var nav_h = $dom.height();
		    
		    var nav_bottom_h = $('#nav_bottom').height();
		    var header_h =$('#header').height();
		    var height = api.winHeight - header_h- nav_h-nav_bottom_h;
		    
//		    var n=api.systemType;
//		    if ("ios" == n){
//				var r = api.systemVersion, a = parseInt(r, 10);
//				if(a >= 7){
//				  nav_h=nav_h+20;
//				  height=height-20;
//				}
//			}
//			else if ("android" == n) {
//				var r = api.systemVersion;
//				r = parseFloat(r);
//				if(r >= 4.4){
//					nav_h=nav_h+25;
//					height=height-25;
//				}
//			}
			var groupname="group"+_this.cid;
			var len=frames.length;
			//alert("dd"+displayIndex);
			api.openFrameGroup({
				name : groupname,
				scrollEnabled : true,
				rect : {
					x : 0,
					y : header_h + nav_h,
					w : width,
					h : height
				},
				index : displayIndex,
				preload:0,
				frames : frames
			}, function(ret, err) {
			    var scrollLi;
			    if(_this.cid=="452"){
			    	scrollLi=$("#scroller452").find("li");
			    }
			    else{
			    	scrollLi=$("#scroller487").find("li");
			    }
			    
			    if(len>4){
			        if(ret.index+1==len){
			           _this.myScroll.scrollTo(-scrollLi.width() * (ret.index - 3), 0, 1000);
			        }
			    	else if (ret.index >= 3) {
						_this.myScroll.scrollTo(-scrollLi.width() * (ret.index - 2), 0, 1000);
					}
					else{
						_this.myScroll.scrollTo(0, 0, 1000);
					}
			    }
			    else{
			    
			    
			    }
				scrollLi.removeClass('nav_active').eq(ret.index).addClass('nav_active');
			});
		},
		openNav : function() {
			var $arrow_down = $('.arrow_down');
			if ($arrow_down.hasClass('arrow_up')) {
				$arrow_down.removeClass('arrow_up');
				$(".switchNav").addClass('hidden');
				$("#wrapper").removeClass('hidden');
				this._closeNav()
			} else {
				$(".switchNav").removeClass('hidden');
				$("#wrapper").addClass('hidden');
				$arrow_down.addClass('arrow_up');
				this._openNav();
			}
		},

		_openNav : function() {
			api.openFrame({
				name : 'frm_nav',
				url : 'html/frm_nav.html',
				rect : {
					x : 0,
					y : $("header").height() + $("nav").height(),
					w : $("body").width(),
					h : 'auto'
				},
				bounces : false
			});
		},

		_closeNav : function() {
			api.closeFrame({
				name : 'frm_nav'
			});
			api.closeFrame({
				name : 'frm_more_nav'
			})
		},
		openFrame : function(obj) {
		    var _this=this;
		    var groupname="group"+_this.cid;
			try {
				api.setFrameGroupIndex({
					name : groupname,
					index : $(obj).data('index'),
					scroll : true
				});
			} catch (e) {
				alert(e)
			}
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'bottom'
		    });
		}
		
		
	};

	return {
		NavIscroll : NavIscroll
	};
}); 