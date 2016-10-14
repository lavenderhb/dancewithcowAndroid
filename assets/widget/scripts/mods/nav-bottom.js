define(function() {

	function Nav(opts) {

		this.opts = $.extend({}, Nav.DEFAULTS, opts);
		this.myScroll = undefined;
		this.domSel = this.opts.domSel;
		this.menuOpened = false;
		this.$dom = $(this.domSel);
		//previous page id, current page id
		this.prevPid = '', this.curPid = 'main';
		//save opened frame name
		this.frameArr = [];
		this.nav_bottom_h = 0;
		this.frameHgt=0;
		this.width = 0;
        this.$headerObj=$(this.opts.headerSel);
		this.headerH = this.$headerObj.height();
		this.headObj = this.opts.headObj;
		
		this.isLogined=true;
		this.hasService=true;
		
		this.init();

	}


	Nav.DEFAULTS = {
		domSel : "#nav_bottom", //导航id
		headerSel : "#header"
	};
	Nav.prototype = {
		init : function() {
			var _this = this;
			_this.$dom = $(_this.domSel);
			_this.getFrameYAndH();
			_this.bindEvt();
			_this.openDefaultTab('main');
		},
		getFrameYAndH:function(){
		    //计算从底部导航中打开的页面（frame）的高度
		    var _this=this;
		    //只有ios做沉浸式头部
			var fontSize = window.getComputedStyle(document.documentElement).fontSize;
			fontSize = fontSize.substring(0, fontSize.length - 2);
//			var y=fontSize*2.5;
//			var n = api.systemType;
//			if ("ios" == n) {
//				var r = api.systemVersion, a = parseInt(r, 10), i = api.fullScreen, o = api.iOS7StatusBarAppearance;
//				a >= 7 && !i && o && (y+=20)
//			}
//			_this.headerH=y;
			//底部导航高度
			var bottomNavH=fontSize*2.5;
			//默认高度，如果页面还有头部，再这个基础上再减去头的高度
			_this.frameHgt=api.winHeight-bottomNavH;
		},
		bindEvt : function() {
			var _this = this;
			_this.$dom.on("click", ".tab-item", function(e) {
				var type = $(this).attr("data-type");
				if(type=="customeService"){
					_this.openTabCusSer(type);
				}
				else if(type == 'my'){
//			    	var uid=$api.getStorage("uid");
//			    	if(!uid||uid==''){
//			    		_this.goLogin();
//			    	}
//			    	else{
			    		_this.openTab(type);
			    	//}
				}
				else if(type == 'hangqing'){
					api.sendEvent({
						name:'start fiveonefive activity'
					})
				}
				else{
					_this.openTab(type);
				}
			});
			api.addEventListener({
	            name:'toIndex'
            },function(ret,err){
            	_this.openTab("main");
            });

		},
		renderHeaderTpl : function(cid,subcid) {
			var _this = this;
			var headObj = _this.headObj;
//			var value = _this.datas[_this.curPid];
//			var data = {
//				data : value
//			};
			headObj.renderTpl(_this.curPid,cid,subcid);
		},
		//frame whether open
		isOpened : function(frmName) {
			var _frameArr = this.frameArr;
			var i = 0, len = _frameArr.length;
			var mark = false;
			for (i; i < len; i++) {
				if (_frameArr[i] === frmName) {
					mark = true;
					return mark;
				}
			}
			return mark;
		},
		openDefaultTab : function(type) {
			var _this = this;
			_this.curPid = type;
		    //main页面头部有子导航
		   var y=_this.headerH+$("#nav").height();
		   var h=_this.frameHgt-y;
		   //alert("y"+y+" h"+h+" frameHgt"+_this.frameHgt);
			_this.frameArr.push(type);
			api.openFrame({
				name : type,
				url : 'html/public/' + type + '.html',
				bounces : false,
				//						pageParam : {
				//							headerHeight : headerPos.h,
				//							tid : tid
				//						},
				rect : {
					x : 0,
					y : y,
					w : 'auto',
					h : h
				}
			});

		},
		//open tab
		openTab : function(type,cid,subcid) {
			//record page id
			var _this = this;
			_this.prevPid = _this.curPid;
			_this.curPid = type;
			
			if (_this.prevPid != _this.curPid) {
			   
			    if(_this.prevPid=="news"){
//			    	api.setFrameGroupAttr({
//					    name: 'group',
//					    hidden:true
//					});
//					api.setFrameGroupAttr({
//					    name: 'group452',
//					    hidden:true
//					});
//					api.setFrameGroupAttr({
//					    name: 'group487',
//					    hidden:true
//					});
					api.closeFrameGroup({
					    name: 'group452'
					});
					api.closeFrameGroup({
					    name: 'group487'
					});
					api.closeFrame({
		                name: "kuaixun"
	                });
	                _this.headObj.clearNewsSubNav();
			    
			    }else{
//			    	api.setFrameAttr({
//						name : _this.prevPid,
//						hidden : true
//					});
					 api.closeFrame({
		                name: _this.prevPid
	                });
			    }
				
				var actTab = _this.$dom.find('.active');
				actTab.removeClass('active');
				var currTab = this.$dom.find("[data-type='" + type + "']");
				currTab.addClass('active');
				if(type=="news"){
				   // alert("3:"+cid);
					_this.renderHeaderTpl(cid,subcid); 
				}else{
					_this.renderHeaderTpl();
				}
//				if (_this.isOpened(type)) {
//				    if(type=="news_rec"){
//				    	api.setFrameGroupAttr({
//						    name: 'group',
//						    hidden:false
//						});
//				    }
//				    else{
//				    	api.setFrameAttr({
//							name : type,
//							hidden : false
//						});
//				    }
//				} 
//				else 
				{
					//_this.frameArr.push(type);
					
			        var y=_this.headerH;
			        var h=_this.frameHgt-y;
			 		if(type=="news"){
						//y=_this.headerH+$("#nav").height();
//						//alert($("#nav").html()+"wwwwwwww:"+$("#nav").height()+"vgvh"+_this.headerH);
//						_this.height=_this.height-$("#nav").height();
						//alert(_this.height);
						return;
					}
					else if(type=="main"){
						y+=$("#nav").height();
						h-=$("#nav").height();
					}
				    
					{
						api.openFrame({
							name : type,
							url : 'html/public/' + type + '.html',
							bounces : type == 'my' ? false : true,
							rect : {
								x : 0,
								y : y,
								w : 'auto',
								h : h
							},
							reload:true,
							pageParam:{type:type}
						});
					}
				}

			}

		},
		goLogin:function(){
			api.openWin({
				name : 'login',
				url : 'html/common/login.html',
				bounces : false,
				animation : 'push',
				scrollToTop : true,
				rect : {
					x : 0,
					y : 0,
					w : 'auto',
					h : 'auto'
				}
			});
		},
		openTabCusSer:function(type){
		    var _this=this;
//			if(_this.isLogined&&_this.hasService){
//	        	api.openWin({
//					name : 'interact',
//					url : 'html/public/interact.html',
//					bounces : false,
//					delay : 200
//				});
//	        }
//	        else if(_this.isLogined){
//	        	alert("您还没有分配客服，没有权限");
//	        }
//	        else
	        {
	        	_this.openTab(type);
	        }
		}
	};

	return {
		Nav : Nav
	};

});
