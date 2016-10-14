define(['html/tpl/build/template'], function(tpl) {

	function header_level2(opts) {

		this.opts = $.extend({}, header_level2.DEFAULTS, opts);
		this.domSel = this.opts.domSel;
		this.$dom = $(this.domSel);
		this.$dom_ph = $(this.opts.dom_ph);
		this.curType = this.opts.type;
		//save opened frame name
		this.frameArr = [];
		this.preFrmName = null;
		this.curFrmName = null;
		this.heightH=0;
		this.toPage=this.opts.toPage;
		this.datas = {
			newsDetail : {
				"title" : "资讯正文",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '客服团队',
					"tabname2" : '互动大厅',
					"from1" : 'interact',
					"from2":'customeService'
				}
			},
			interactHall : {
				"title" : "互动大厅",
				header : {
					"h_display" : true,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}

			},
			customeServiceDetail : {
				"title" : "客服团队",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			login : {
				"title" : "用户登录",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			register : {
				"title" : "用户注册",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			contact_us:{
				"title" : "联系我们",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			setting:{
				"title" : "设置",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			msg:{
				"title" : "我的消息",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			msgDetail : {
				"title" : "消息详情",
				header : {
					"h_display" : false,
					"btn1_display" : false,
					"btnname1" : '',
					"btn1_icon_display" : true,
					"btn2_display" : true,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '客服团队',
					"tabname2" : '互动大厅',
					"from1" : 'interact',
					"from2":'customeService'
				}
			},
			userInfo:{
				"title" : "个人资料",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			tel:{
				"title" : "修改手机号",
				header : {
					"h_display" : false,
					"btn1_display" : true,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : false,
					"btnname2" : '',
					"btn2_icon_display" : true
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			fullName:{
				"title" : "修改姓名",
				header : {
					"h_display" : true,
					"btn1_display" : false,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : true,
					"btnname2" : '保存',
					"btn2_icon_display" : false,
					"from":"fullName"
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			qq:{
				"title" : "修改qq号",
				header : {
					"h_display" : true,
					"btn1_display" : false,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : true,
					"btnname2" : '保存',
					"btn2_icon_display" : false,
					"from":"qq"
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			introduce:{
				"title" : "修改个人说明",
				header : {
					"h_display" : true,
					"btn1_display" : false,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : true,
					"btnname2" : '保存',
					"btn2_icon_display" : false,
					"from":"introduce"
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			},
			online:{
				"title" : "在线直播",
				header : {
					"h_display" : false,
					"btn1_display" : false,
					"btnname1" : '',
					"btn1_icon_display" :  true,
					"btn2_display" : true,
					"btnname2" : '保存',
					"btn2_icon_display" : false,
					"from":"introduce"
				},
				headerTab : {
					"h_display" : false,
					"tabname1" : '正在热映',
					"tabname2" : '即将上映'
				}
			}
			
		};

		this.init();
	}


	header_level2.DEFAULTS = {
		dom_ph : '#header_placeholder', //位置占位符
		domSel : "#header",//导航id
	};
	header_level2.prototype = {
		init : function() {
			var _this = this;

			_this.renderTpl(_this.curType);
			_this.bindEvt();

		},
		renderTpl : function(type) {
			var _this = this;
			var $ph = _this.$dom_ph;
			_this.$dom = $(_this.domSel);
			var value = _this.datas[type];
			if(type=="customeServiceDetail"){
			    var page=api.pageParam.page;
			    if(page=="finaceCustomeService"){
			       value["title"]="理财团队";
			    }
			}
			var data = {
				data : value
			};
			if (_this.$dom.length > 0) {
				_this.$dom.after(tpl("public/header_level2", data));
				_this.$dom.remove();
				_this.$dom=$(_this.domSel);
			  //dom有变化，重新取一次
				_this.bindEvt();
			} else {

				$ph.after(tpl("public/header_level2", data));
				$ph.remove();
				_this.$dom = $(_this.domSel);
			}
			//默认样式展示
			_this.heightH=_this.$dom.height();
			var header = $api.byId("header");
			$api.fixIos7Bar(header);
			api.setStatusBarStyle({
			    style: 'light'
			});
			//_this.openDefaultTab();
		},
		bindEvt : function() {
			var _this = this;
			_this.$dom.on("click", "#back", function(e) {
			    if(_this.toPage){
			    	api.execScript({
	                     name: 'root',
						 script: 'openTab("'+_this.toPage+'");'
                    });
			    }
			    if(_this.curType=="newsDetail"){
//			    	api.execScript({
//			    	    name:"newsDetail",
//			    	    frameName:"native_video",
//	                    script: 'toCloseVideo();'
//                  });
//                  api.closeFrame({
//	                    name: 'native_video'
//                  });
//                  api.closeFrame({
//	                    name: 'native_video_progress'
//                  });
//					api.closeFrame({
//	                    name: 'newsDetail_frm'
//                  });
			    }
		    	api.closeWin({
					name : _this.curType
				});

			});
			_this.$dom.on("click", ".tab-link", function(e) {
//			    _this.preFrmName = _this.curFrmName;
//			    _this.curFrmName = $(this).data("from")+"_frm";
//			    
//			    if (_this.preFrmName != _this.curFrmName){
//			    	api.setFrameAttr({
//						name : _this.preFrmName,
//						hidden : true
//					});
//			    	$(this).addClass("active");
//				    $(this).siblings().removeClass("active");
//				    if(_this.isOpened(_this.curFrmName)){
//				    	api.setFrameAttr({
//							name : _this.curFrmName,
//							hidden : false
//						});
//				    }
//					else{
//						_this.frameArr.push(_this.curFrmName);
//						_this.openFrame(_this.curFrmName);
//					}
//			    }
			});
			$("#search").on("click",function(){
				api.openWin({
					name : 'search',
					url : '../common/search_window.html',
					bounces : false,
					delay : 200
				}); 
			
			});
//			$("#btn-save").on("click",function(){
//				var type = $(this).data("from");
//				if (type == "tel") {
//					api.toast({
//						msg : '保存成功',
//						duration : 2000,
//						location : 'middle'
//					});
//				} else if (type == "fullName") {
//					api.toast({
//						msg : '保存成功',
//						duration : 2000,
//						location : 'middle'
//					});
//				} else if (type == "qq") {
//					api.toast({
//						msg : '保存成功',
//						duration : 2000,
//						location : 'middle'
//					});
//				} else if (type == "introduce") {
//					api.toast({
//						msg : '保存成功',
//						duration : 2000,
//						location : 'middle'
//					});
//				}
//			
//			});
		},
		openDefaultTab : function() {
//			var _this = this;
//			_this.curFrmName=_this.defaultFrmName;
//			_this.$dom.find(".tab-link").each(function(){
//			   if($(this).data("from")==_this.curFrmName){
//			   		$(this).addClass("active");
//			   }
//			});
//			_this.frameArr.push(_this.curFrmName);
//			_this.openFrame(_this.frmName);
		},
		//frame whether open
		isOpened : function(frmName) {
//			var _frameArr = this.frameArr;
//			var i = 0, len = _frameArr.length;
//			var mark = false;
//			for (i; i < len; i++) {
//				if (_frameArr[i] === frmName) {
//					mark = true;
//					return mark;
//				}
//			}
//			return mark;
		},
		openFrame:function(name){
//		    var _this=this;
//			api.openFrame({
//			    name: name,
//			    url: '../frame/'+name+'.html',
//			    rect:{
//			        x:0,
//			        y:_this.headerH,
//			        w:'auto',
//			        h:api.winHeight-_this.headerH
//			    },
//			    bounces: true
//			});
		}
	};

	return {
		header_level2 : header_level2
	};

});
